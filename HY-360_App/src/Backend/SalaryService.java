package Backend;

import DAO.*;
import Model.Employee;
import JDBC.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class SalaryService {

    public double calculateMonthlySalary(int employeeId) {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        ContractDAO contractDAO = new ContractDAO();
        SalaryRateDAO salaryRateDAO = new SalaryRateDAO();
        DAO.AllowanceDAO allowanceDAO = new DAO.AllowanceDAO();

        Employee emp = employeeDAO.getEmployeeById(employeeId);

        // Έλεγχος 1: Αν είναι ανενεργός (απολυμένος), δεν πληρώνεται
        if (emp == null || !emp.isActive()) {
            return 0;
        }

        // Έλεγχος 2: Αν η ημερομηνία πρόσληψης είναι στο μέλλον, δεν πληρώνεται
        LocalDate today = LocalDate.now();
        if (emp.getEmploymentDate().toLocalDate().isAfter(today)) {
            System.out.println("Employee " + employeeId + " starts in the future. No pay yet.");
            return 0;
        }

        double finalSalary = 0.0;
        double baseAmount = 0.0;

        // Υπολογισμός ετών υπηρεσίας (από την ημ. πρόσληψης μέχρι σήμερα)
        int yearsOfService = Period.between(emp.getEmploymentDate().toLocalDate(), today).getYears();
        if (yearsOfService < 0) yearsOfService = 0;

        String role = emp.getRole();
        String type = emp.getEmploymentType();

        // --- ΥΠΟΛΟΓΙΣΜΟΣ ΒΑΣΙΚΟΥ ΜΙΣΘΟΥ ---
        if ("PERMANENT".equals(type)) {
            baseAmount = salaryRateDAO.getBaseSalary(role);

            // Προσαύξηση 15% για κάθε χρόνο μετά τον πρώτο
            if (yearsOfService > 1) {
                double experienceBonus = baseAmount * 0.15 * (yearsOfService - 1);
                finalSalary += experienceBonus;
            }
            finalSalary += baseAmount;

        } else if ("CONTRACTOR".equals(type)) {
            java.sql.Date payrollDate = java.sql.Date.valueOf(today);
            baseAmount = contractDAO.getActiveContractSalary(employeeId, payrollDate);

            if (baseAmount == 0.0) {
                System.out.println("Warning: Contractor " + employeeId + " has no active contract for today. Skipping payment.");
                return 0; // Δεν πληρώνεται
            }
            finalSalary += baseAmount;
        }

        // --- ΕΠΙΔΟΜΑΤΑ ---
        // (Προστίθενται μόνο αν υπάρχει βασικός μισθός > 0)
        if (baseAmount > 0) {
            // Οικογενειακό
            if (emp.isMarried()) {
                finalSalary += (baseAmount * 0.05);
            }
            int childCount = employeeDAO.getChildCount(employeeId);
            if (childCount > 0) {
                finalSalary += (baseAmount * 0.05 * childCount);
            }

            // Ειδικά Επιδόματα
            if ("TEACHING".equals(role)) {
                if ("PERMANENT".equals(type)) {
                    finalSalary += allowanceDAO.getResearchAllowance();
                } else if ("CONTRACTOR".equals(type)) {
                    finalSalary += allowanceDAO.getLibraryAllowance();
                }
            }

            savePayment(employeeId, finalSalary);
            System.out.println("Salary paid for ID " + employeeId + ": " + finalSalary + "€");
        }

        return finalSalary;
    }

    private void savePayment(int empId, double amount) {
        String sql = "INSERT INTO Payment (date, amount, Employee_idEmployee) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            ps.setDouble(2, amount);
            ps.setInt(3, empId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}