package DAO;

import Model.Employee;
import JDBC.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public Employee getEmployeeById(int id) {
        Employee emp = null;
        // ΔΙΟΡΘΩΣΗ: Προσθήκη JOIN με Employment_type και σωστά ονόματα στηλών
        String sql = "SELECT e.*, r.role_name, d.department_name, et.type_name " +
                "FROM Employee e " +
                "JOIN Role r ON e.Role_roleID = r.roleID " +
                "JOIN Department d ON e.Department_departmentID = d.departmentID " +
                "JOIN Employment_type et ON e.Employment_type_typeID = et.typeID " +
                "WHERE e.idEmployee = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                emp = mapResultSetToEmployee(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return emp;
    }

    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        // ΔΙΟΡΘΩΣΗ: Προσθήκη JOINS και εδώ
        String sql = "SELECT e.*, r.role_name, d.department_name, et.type_name " +
                "FROM Employee e " +
                "JOIN Role r ON e.Role_roleID = r.roleID " +
                "JOIN Department d ON e.Department_departmentID = d.departmentID " +
                "JOIN Employment_type et ON e.Employment_type_typeID = et.typeID";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Βοηθητική μέθοδος για να μην γράφουμε τον κώδικα 2 φορές
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setId(rs.getInt("idEmployee"));
        emp.setName(rs.getString("name"));
        emp.setAddress(rs.getString("address"));
        emp.setIban(rs.getString("iban"));
        emp.setPhoneNumber(rs.getString("phone_number"));
        emp.setMarried(rs.getInt("is_Married") == 1); // Το TINYINT επιστρέφει int
        emp.setBank(rs.getString("bank"));
        emp.setEmploymentDate(rs.getDate("employment_starting_date"));
        emp.setDepartmentName(rs.getString("department_name")); // Όχι "Department_Department_name"
        emp.setActive(rs.getInt("Is_Active") == 1);

        // ΚΡΙΣΙΜΕΣ ΑΛΛΑΓΕΣ:
        emp.setRole(rs.getString("role_name"));       // Διάβασε το σωστό όνομα ρόλου (TEACHING/ADMINISTRATIVE)
        emp.setEmploymentType(rs.getString("type_name")); // Διάβασε τον τύπο (PERMANENT/CONTRACTOR)
        return emp;
    }

    public int getChildCount(int employeeId) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Child WHERE Employee_idEmployee = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return count;
    }

    public double getResearchAllowance(int employeeId) {
        double amount = 0.0;
        String sql = "SELECT research_allowence FROM Allowences WHERE Employee_idEmployee = ?";
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
        String sql = "SELECT library_allowence FROM Allowences WHERE Employee_idEmployee = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) amount = rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return amount;
    }
}