package JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseHelper {

    public static void clearDatabase() {
        System.out.println("[DatabaseHelper]: Cleaning database...");
        String[] tables = {
                "Payment", "Contract", "Child", "Allowences", "Employee",
                "Salary_rates", "Department", "Role", "Employment_type"
        };

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
            for (String table : tables) {
                stmt.executeUpdate("DELETE FROM `" + table + "`");
                // Reset Auto Increment για να ξεκινάνε τα IDs από το 1
                stmt.executeUpdate("ALTER TABLE `" + table + "` AUTO_INCREMENT = 1");
            }
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
            System.out.println("[DatabaseHelper]: Database cleared.");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void setupDatabaseData() {
        try (Connection conn = DBConnection.getConnection()) {
            // 1. Roles (ID 1=TEACHING, ID 2=ADMINISTRATIVE)
            String sqlRole = "INSERT INTO Role (role_name) VALUES (?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlRole)) {
                ps.setString(1, "TEACHING"); ps.executeUpdate();
                ps.setString(1, "ADMINISTRATIVE"); ps.executeUpdate();
            }

            // 2. Employment Types (ID 1=PERMANENT, ID 2=CONTRACTOR)
            String sqlType = "INSERT INTO Employment_type (type_name) VALUES (?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlType)) {
                ps.setString(1, "PERMANENT"); ps.executeUpdate();
                ps.setString(1, "CONTRACTOR"); ps.executeUpdate();
            }

            // 3. Departments
            String sqlDept = "INSERT INTO Department (department_name) VALUES (?)";
            String[] depts = {"Computer Science", "Mathematics", "Physics", "Chemistry"};
            try (PreparedStatement ps = conn.prepareStatement(sqlDept)) {
                for (String d : depts) {
                    ps.setString(1, d);
                    ps.executeUpdate();
                }
            }

            // 4. Salary Rates (Link to Role IDs)
            String sqlRate = "INSERT INTO Salary_rates (Role_roleID, base_salary) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlRate)) {
                ps.setInt(1, 1); ps.setDouble(2, 1200.00); // Teaching
                ps.executeUpdate();
                ps.setInt(1, 2); ps.setDouble(2, 900.00);  // Administrative
                ps.executeUpdate();
            }

            System.out.println("[System]: Database setup completed (3NF Ready).");
        } catch (Exception e) { e.printStackTrace(); }
    }
}