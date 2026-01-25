package Backend;

import DAO.EmployeeDAO;
import JDBC.DBConnection;
import Model.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

//handles payment recording and execution
public class PayrollManager {

    // runs payroll for a specific period for all employees
    public void runMonthlyPayroll(int month, int year) {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        SalaryService salaryService = new SalaryService();

        List<Employee> activeEmployees = employeeDAO.getAllEmployees();

        // determines the date
        LocalDate paymentDate = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);
        java.sql.Date sqlDate = java.sql.Date.valueOf(paymentDate);

        System.out.println("Starting Payroll for: " + paymentDate);

        int count = 0;// holds successful payments

        try (Connection conn = DBConnection.getConnection()) {

            String sqlInsert = "INSERT INTO Payment (date, amount, Employee_idEmployee) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sqlInsert);

            for (Employee emp : activeEmployees) {

                if (!emp.isActive())// skip inactive employees
                    continue;

                double salaryAmount = salaryService.calculateMonthlySalary(emp.getId());// calculates salary

                ps.setDate(1, sqlDate);
                ps.setDouble(2, salaryAmount);
                ps.setInt(3, emp.getId());

                ps.executeUpdate();// inserts record into database
                count++;
            }
            System.out.println("Payroll completed. Paid " + count + " employees.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}