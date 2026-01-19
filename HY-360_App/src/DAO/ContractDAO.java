package DAO;

import JDBC.DBConnection;
import java.sql.*;

public class ContractDAO {

    // Returns the salary for a specific contractor
    public double getSalaryByEmployeeId(int employeeId) {
        double salary = 0.0;
        // Notice we join Contractor -> Contract to find the active contract
        String sql = "SELECT c.salary FROM Contract c " +
                "JOIN Contractor cnt ON c.Contractor_Employee_idEmployee = cnt.Employee_idEmployee " +
                "WHERE cnt.Employee_idEmployee = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                salary = rs.getDouble("salary");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salary;
    }
}