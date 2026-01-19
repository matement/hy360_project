package DAO;

import JDBC.DBConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SalaryRateDAO {

    public double getBaseSalary(String roleName) {
        double salary = 0.0;
        String sql = "SELECT base_salary FROM SalaryRates WHERE role_name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roleName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                salary = rs.getDouble("base_salary");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salary;
    }

    public void UpdateBaseSalary(String roleName, double newSalary) {
        String sql = "UPDATE SalaryRates SET base_salary = ? WHERE role_name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, newSalary);
            ps.setString(2, roleName);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Success: Salary for " + roleName + " updated to " + newSalary);
            } else {
                System.out.println("Error: Role " + roleName + " not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Double> getAllSalaryRates() {
        Map<String, Double> rates = new HashMap<>();
        String sql = "SELECT * FROM SalaryRates";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rates.put(
                        rs.getString("role_name"),
                        rs.getDouble("base_salary")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rates;
    }
}
