package DAO;

import JDBC.DBConnection;
import java.sql.*;

public class AllowanceDAO {

    // Βοηθητική μέθοδος για να βρίσκουμε το ID του τύπου (π.χ. "PERMANENT" -> 1)
    private int getTypeID(String typeName) {
        String sql = "SELECT typeID FROM Employment_type WHERE type_name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, typeName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    public double getResearchAllowance() {
        int typeId = getTypeID("PERMANENT");
        String sql = "SELECT research_allowence FROM Allowences WHERE typeID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, typeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    public double getLibraryAllowance() {
        int typeId = getTypeID("CONTRACTOR");
        String sql = "SELECT library_allowence FROM Allowences WHERE typeID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, typeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    public void updateResearchAllowance(double newAmount) {
        int typeId = getTypeID("PERMANENT");
        // Update μόνο αν βρούμε το ID
        String sql = "UPDATE Allowences SET research_allowence = ? WHERE typeID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newAmount);
            ps.setInt(2, typeId);
            ps.executeUpdate();
            System.out.println("Success: Research Allowance updated to " + newAmount);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void updateLibraryAllowance(double newAmount) {
        int typeId = getTypeID("CONTRACTOR");
        String sql = "UPDATE Allowences SET library_allowence = ? WHERE typeID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newAmount);
            ps.setInt(2, typeId);
            ps.executeUpdate();
            System.out.println("Success: Library Allowance updated to " + newAmount);
        } catch (SQLException e) { e.printStackTrace(); }
    }
}