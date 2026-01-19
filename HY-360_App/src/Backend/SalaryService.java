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
            System.out.println("Employee not found or inactive.");
            return 0;
        }

        double finalSalary = 0.0;
        double baseAmount = 0.0;

        LocalDate startDate = emp.getEmploymentDate().toLocalDate();
        LocalDate today = LocalDate.now();
        int yearsOfService = Period.between(startDate, today).getYears();

        String role = emp.getRole();

        if (role.startsWith("PERMANENT")) {
            baseAmount = salaryRateDAO.getBaseSalary(role);

            if (yearsOfService > 1) {
                double experienceBonus = baseAmount * 0.15 * (yearsOfService - 1); // Αφαιρούμε τον 1ο χρόνο
                finalSalary += experienceBonus;
            }
            finalSalary += baseAmount;

        } else if (role.startsWith("CONTRACTOR")) {
            baseAmount = contractDAO.getSalaryByEmployeeId(employeeId);
            finalSalary += baseAmount;
        }

        if (emp.isMarried()) {
            finalSalary += (baseAmount * 0.05);
        }
        int childCount = employeeDAO.getChildCount(employeeId); // Νέα μέθοδος που πρέπει να φτιάξεις
        if (childCount > 0) {
            finalSalary += (baseAmount * 0.05 * childCount);
        }


        if (role.equals("PERMANENT_TEACHING")) {
            double researchAllowance = employeeDAO.getResearchAllowance(employeeId); // Νέα μέθοδος
            finalSalary += researchAllowance;
        }
        else if (role.equals("CONTRACTOR_TEACHING")) {
            double libraryAllowance = employeeDAO.getLibraryAllowance(employeeId); // Νέα μέθοδος
            finalSalary += libraryAllowance;
        }

        savePayment(employeeId, finalSalary);

        System.out.println("Salary calculated & paid for ID " + employeeId + ": " + finalSalary + "€");
        return finalSalary;
    }

    private void savePayment(int empId, double amount) {
        String sql = "INSERT INTO Payment (date, amount, Employee_idEmployee, idPayment) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            ps.setDouble(2, amount);
            ps.setInt(3, empId);
            ps.setInt(4, (int)(System.currentTimeMillis() % 100000)); // Πρόχειρο ID generator για τώρα

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}