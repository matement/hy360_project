package Backend;

import DAO.EmployeeDAO;
import JDBC.DBConnection;
import Model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PayrollManager {


    public void runMonthlyPayroll(int month, int year) {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        SalaryService salaryService = new SalaryService();


        List<Employee> activeEmployees = employeeDAO.getAllEmployees();


        LocalDate paymentDate = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);
        java.sql.Date sqlDate = java.sql.Date.valueOf(paymentDate);

        System.out.println("Starting Payroll for: " + paymentDate);

        int count = 0;
        try (Connection conn = DBConnection.getConnection()) {

            String sqlInsert = "INSERT INTO Payment (date, amount, Employee_idEmployee) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sqlInsert);

            for (Employee emp : activeEmployees) {

                if (!emp.isActive()) continue;


                double salaryAmount = salaryService.calculateMonthlySalary(emp.getId());


                ps.setDate(1, sqlDate);
                ps.setDouble(2, salaryAmount);
                ps.setInt(3, emp.getId());

                ps.executeUpdate();
                count++;
            }
            System.out.println("Payroll completed. Paid " + count + " employees.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}