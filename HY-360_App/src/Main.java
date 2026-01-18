import Backend.EmployeeManager;
import JDBC.DBConnection; // Import this
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.SQLException; // Import this

public class Main {
    public static void main(String[] args) {

        // --- STEP 1: Quick helper to ensure Department exists ---
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT IGNORE INTO Department VALUES ('Computer Science')")) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // --------------------------------------------------------

        EmployeeManager manager = new EmployeeManager();
        Date today = new Date(System.currentTimeMillis());

        // Test hiring ID 101
        manager.hirePermanentTeaching(101, "Test User", "Test Address", "GR123", "69999", false, "Test Bank", "Computer Science", today, 0);
    }
}