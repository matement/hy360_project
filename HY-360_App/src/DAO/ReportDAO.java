package DAO;

import JDBC.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportDAO {

    // 1. History (Simple Select)
    public List<String> getPaymentHistory(int employeeId) {
        List<String> history = new ArrayList<>();
        String sql = "SELECT date, amount FROM Payment WHERE Employee_idEmployee = ? ORDER BY date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                history.add("Date: " + rs.getDate("date") + " | Amount: " + rs.getDouble("amount") + "€");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return history;
    }

    // 2. Statistics (Complex Join - This one was actually correct in your file)
    public Map<String, String> getSalaryStatsPerRole() {
        Map<String, String> stats = new HashMap<>();
        String sql = "SELECT r.role_name as role, MAX(p.amount) as max_s, MIN(p.amount) as min_s, AVG(p.amount) as avg_s " +
                "FROM Payment p " +
                "JOIN Employee e ON p.Employee_idEmployee = e.idEmployee " +
                "JOIN Role r ON e.Role_roleID = r.roleID " +
                "WHERE p.date = (SELECT MAX(date) FROM Payment) " +
                "GROUP BY r.role_name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String role = rs.getString("role");
                String info = String.format("Max: %.2f | Min: %.2f | Avg: %.2f",
                        rs.getDouble("max_s"), rs.getDouble("min_s"), rs.getDouble("avg_s"));
                stats.put(role, info);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return stats;
    }

    // 3. Total Cost (Simple Sum)
    public double getTotalPayrollCost(int month, int year) {
        double total = 0;
        String sql = "SELECT SUM(amount) FROM Payment WHERE MONTH(date) = ? AND YEAR(date) = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) total = rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return total;
    }

    // 4. Cost Per Role (Group By Sum)
    public Map<String, Double> getTotalCostPerRole(int month, int year) {
        Map<String, Double> costs = new HashMap<>();
        String sql = "SELECT r.role_name as role, SUM(p.amount) as total " +
                "FROM Payment p " +
                "JOIN Employee e ON p.Employee_idEmployee = e.idEmployee " +
                "JOIN Role r ON e.Role_roleID = r.roleID " +
                "WHERE MONTH(p.date) = ? AND YEAR(p.date) = ? " +
                "GROUP BY r.role_name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                costs.put(rs.getString("role"), rs.getDouble("total"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return costs;
    }
}