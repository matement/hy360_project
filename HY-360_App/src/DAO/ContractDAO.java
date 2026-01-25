package DAO;
import JDBC.DBConnection;
import java.sql.*;

public class ContractDAO {
    /**
     * Επιστρέφει τον μισθό ΜΟΝΟ αν υπάρχει ενεργή σύμβαση για τη δοθείσα ημερομηνία.
     */
    public double getActiveContractSalary(int employeeId, Date paymentDate) {
        double salary = 0.0;
        // Ελέγχουμε αν η ημερομηνία πληρωμής είναι ΜΕΣΑ στο διάστημα [start_Date, end_Date]
        String sql = "SELECT salary FROM Contract " +
                "WHERE Employee_idEmployee = ? " +
                "AND ? BETWEEN start_Date AND end_Date";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            ps.setDate(2, paymentDate); // Η ημερομηνία της μισθοδοσίας

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                salary = rs.getDouble("salary");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return salary; // Αν δεν βρεθεί ενεργή σύμβαση, επιστρέφει 0.0
    }

    public String getLatestContractEndDate(int employeeId) {
        String sql = "SELECT end_Date FROM Contract WHERE Employee_idEmployee = ? ORDER BY end_Date DESC LIMIT 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, employeeId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDate("end_Date").toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "---"; 
    }
}