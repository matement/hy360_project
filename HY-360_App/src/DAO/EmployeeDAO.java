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
                emp.setDepartmentName(rs.getString("Department_Department_name"));
                emp.setActive(rs.getBoolean("is_Active"));
                emp.setRole(rs.getString("Role"));
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
                        rs.getString("Department_Department_name"),
                        rs.getBoolean("is_Active"),
                        rs.getString("Role")
                );
                list.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getChildCount(int employeeId) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Child WHERE Employee_idEmployee = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return count;
    }

    public double getResearchAllowance(int employeeId) {
        double amount = 0.0;
        String sql = "SELECT research_allowence FROM `Permanent Teaching Employee` WHERE Permenant_Employee_idEmployee = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) amount = rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return amount;
    }

    public double getLibraryAllowance(int employeeId) {
        double amount = 0.0;
        String sql = "SELECT library_allowence FROM `Contractor Teaching Employee` WHERE Contractor_Employee_idEmployee = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) amount = rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return amount;
    }
}