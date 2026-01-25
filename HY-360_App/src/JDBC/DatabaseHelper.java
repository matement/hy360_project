package JDBC;

import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

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
            String[] depts = {
                    "Mathematics and Applied Mathematics",
                    "Physics",
                    "Computer Science",
                    "Biology",
                    "Chemistry",
                    "Materials Science and Technology",
                    "Medicine",
                    "Philology",
                    "History and Archaeology",
                    "Philosophy",
                    "Primary Education",
                    "Preschool Education",
                    "Sociology",
                    "Economics",
                    "Political Science",
                    "Psychology"
            };
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

    public static DefaultTableModel executeQuery(String sqlQuery) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQuery)) {

            // 1. Παίρνουμε πληροφορίες για τις στήλες (Metadata)
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 2. Δημιουργία των ονομάτων των στηλών
            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }

            // 3. Δημιουργία των δεδομένων (Γραμμές)
            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                data.add(row);
            }

            // 4. Επιστροφή έτοιμου μοντέλου για το JTable
            return new DefaultTableModel(data, columnNames);
        }
    }

    // Ελέγχει αν ο πίνακας Role έχει δεδομένα.
    // Αν επιστρέψει false, σημαίνει ότι η βάση είναι άδεια και θέλει setup.
    public static boolean isDatabaseInitialized() {
        String sql = "SELECT COUNT(*) FROM Role";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                // Αν το count > 0, σημαίνει ότι υπάρχουν ρόλοι, άρα η βάση έχει αρχικοποιηθεί
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            // Αν πετάξει λάθος (π.χ. δεν υπάρχει ο πίνακας), θεωρούμε ότι δεν είναι initialized
            return false;
        }
        return false;
    }
}