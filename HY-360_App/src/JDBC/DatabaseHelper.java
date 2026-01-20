package JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseHelper {

    /**
     * Διαγράφει όλα τα δεδομένα από τους πίνακες με τη σωστή σειρά
     * για να αποφευχθούν προβλήματα με τα Foreign Keys.
     */
    public static void clearDatabase() {
        System.out.println("[DatabaseHelper]: Starting database cleanup...");

        // Η σειρά διαγραφής έχει σημασία λόγω των Foreign Keys,
        // αλλά με το SET FOREIGN_KEY_CHECKS = 0 το παρακάμπτουμε για ευκολία.
        String[] tables = {
                "Payment",
                "Contract",
                "Child",
                "Contractor Teaching Employee",
                "Contractor Administrative Employee",
                "Permanent Teaching Employee",
                "Permanent Administrative Employee",
                "Permanent",
                "Contractor",
                "Employee",
                // Προσοχή: Δεν διαγράφουμε το SalaryRates ή το Department αν είναι σταθερά δεδομένα,
                // αλλά αν θέλεις πλήρες reset, βάλε τα και αυτά.
                "SalaryRates"
                // "Department" // Συνήθως τα τμήματα μένουν σταθερά
        };

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Απενεργοποίηση ελέγχων για να μπορούμε να διαγράψουμε γρήγορα
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");

            for (String table : tables) {
                // Χρησιμοποιούμε DELETE αντί για TRUNCATE για να είναι πιο ασφαλές με τα keys
                stmt.executeUpdate("DELETE FROM `" + table + "`");
            }

            // Επαναφορά ελέγχων
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");

            System.out.println("[DatabaseHelper]: Database cleared successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("[DatabaseHelper]: Failed to clear database.");
        }
    }

    public static void setupDatabaseData() {
        try (Connection conn = DBConnection.getConnection()) {
            // 1. Departments
            String[] depts = {"Computer Science", "Mathematics", "Physics", "Chemistry"};
            PreparedStatement psDept = conn.prepareStatement("INSERT IGNORE INTO Department VALUES (?)");
            for (String d : depts) {
                psDept.setString(1, d);
                psDept.executeUpdate();
            }

            // 2. Salary Rates (Roles)
            // ΣΗΜΑΝΤΙΚΟ: Πρέπει να εισάγουμε ΟΛΟΥΣ τους ρόλους, γιατί το 'Role' στον πίνακα Employee
            // είναι πλέον Foreign Key που δείχνει εδώ.
            String sqlRate = "INSERT IGNORE INTO SalaryRates (role_name, base_salary) VALUES (?, ?)";
            PreparedStatement psRate = conn.prepareStatement(sqlRate);

            // Μόνιμοι (έχουν βασικό μισθό)
            psRate.setString(1, "PERMANENT_TEACHING");
            psRate.setDouble(2, 1200.00);
            psRate.executeUpdate();

            psRate.setString(1, "PERMANENT_ADMINISTRATIVE");
            psRate.setDouble(2, 900.00);
            psRate.executeUpdate();

            // Συμβασιούχοι (Ο μισθός τους ορίζεται στη σύμβαση, βάζουμε 0 εδώ τυπικά για να υπάρχει το κλειδί)
            psRate.setString(1, "CONTRACTOR_TEACHING");
            psRate.setDouble(2, 0.0);
            psRate.executeUpdate();

            psRate.setString(1, "CONTRACTOR_ADMINISTRATIVE");
            psRate.setDouble(2, 0.0);
            psRate.executeUpdate();

            System.out.println("[System]: Database setup completed (Departments & Roles checked).");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}