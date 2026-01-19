package DAO;

import Model.Employee;
import JDBC.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // 1. Find a single employee (Useful for the "Update Details" GUI)
    public Employee getEmployeeById(int id) {
        Employee emp = null;
        String sql = "SELECT * FROM Employee WHERE idEmployee = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                emp = new Employee();
                emp.setId(rs.getInt("idEmployee"));
                emp.setName(rs.getString("name"));
                emp.setAddress(rs.getString("address"));
                emp.setIban(rs.getString("iban"));
                emp.setPhoneNumber(rs.getString("phone_number"));
                emp.setMarried(rs.getBoolean("is_Married"));
                emp.setBank(rs.getString("bank"));
                emp.setEmploymentDate(rs.getDate("employment_starting_date"));
                emp.setDepartmentName(rs.getString("Deparment_Department_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emp;
    }

    // 2. Get ALL employees (Crucial for the Payroll Loop!)
    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM Employee";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Employee emp = new Employee(
                        rs.getInt("idEmployee"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("iban"),
                        rs.getString("phone_number"),
                        rs.getBoolean("is_Married"),
                        rs.getString("bank"),
                        rs.getDate("employment_starting_date"),
                        rs.getString("Deparment_Department_name"),
                        rs.getBoolean("is_Active")
                );
                list.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}