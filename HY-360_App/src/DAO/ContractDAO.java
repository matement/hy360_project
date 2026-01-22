package DAO;
import JDBC.DBConnection;
import java.sql.*;

public class ContractDAO {
    public double getSalaryByEmployeeId(int employeeId) {
        double salary = 0.0;
        // Direct link to Employee
        String sql = "SELECT salary FROM Contract WHERE Employee_idEmployee = ? ORDER BY start_Date DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) salary = rs.getDouble("salary");
        } catch (SQLException e) { e.printStackTrace(); }
        return salary;
    }
}