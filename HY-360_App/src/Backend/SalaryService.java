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

        Employee emp = employeeDAO.getEmployeeById(employeeId);
        if (emp == null || !emp.isActive()) {
            System.out.println("Employee " + employeeId + " not found or inactive.");
            return 0;
        }

        double finalSalary = 0.0;
        double baseAmount = 0.0;

        LocalDate startDate = emp.getEmploymentDate().toLocalDate();
        LocalDate today = LocalDate.now();
        int yearsOfService = Period.between(startDate, today).getYears();

        String role = emp.getRole();             // π.χ. "TEACHING"
        String type = emp.getEmploymentType();   // π.χ. "PERMANENT"

        // 1. Υπολογισμός Βασικού Μισθού
        if ("PERMANENT".equals(type)) {
            // Οι μόνιμοι παίρνουν μισθό βάσει του Ρόλου τους (TEACHING ή ADMINISTRATIVE)
            baseAmount = salaryRateDAO.getBaseSalary(role);

            if (yearsOfService > 1) {
                double experienceBonus = baseAmount * 0.15 * (yearsOfService - 1);
                finalSalary += experienceBonus;
            }
            finalSalary += baseAmount;

        } else if ("CONTRACTOR".equals(type)) {
            // Οι συμβασιούχοι παίρνουν μισθό από τη Σύμβαση
            baseAmount = contractDAO.getSalaryByEmployeeId(employeeId);
            finalSalary += baseAmount;
        }

        // 2. Οικογενειακό Επίδομα
        if (emp.isMarried()) {
            finalSalary += (baseAmount * 0.05);
        }
        int childCount = employeeDAO.getChildCount(employeeId);
        if (childCount > 0) {
            finalSalary += (baseAmount * 0.05 * childCount);
        }

        // 3. Ειδικά Επιδόματα (Ελέγχουμε τον Ρόλο και τον Τύπο)
        if ("TEACHING".equals(role)) {
            if ("PERMANENT".equals(type)) {
                // Επίδομα Έρευνας
                finalSalary += employeeDAO.getResearchAllowance(employeeId);
            } else if ("CONTRACTOR".equals(type)) {
                // Επίδομα Βιβλιοθήκης
                finalSalary += employeeDAO.getLibraryAllowance(employeeId);
            }
        }

        savePayment(employeeId, finalSalary);
        System.out.println("Salary calculated & paid for ID " + employeeId + " (" + type + " " + role + "): " + finalSalary + "€");
        return finalSalary;
    }

    private void savePayment(int empId, double amount) {
        // Χωρίς idPayment αν είναι AUTO_INCREMENT, αλλιώς όπως το είχες
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