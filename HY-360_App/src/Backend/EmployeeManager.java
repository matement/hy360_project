package Backend;

import JDBC.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

public class EmployeeManager {

    public void hirePermanentTeaching(int id, String name, String address, String iban,
                                      String phone, boolean isMarried, String bank,
                                      String departmentName, Date startDate,
                                      int yearsOfEmployment, double researchAllowance) { // Added allowance param

        Connection conn = null;

        // 1. Generic Employee Table
        String sqlEmployee = "INSERT INTO Employee (idEmployee, name, address, iban, phone_number, is_Married, bank, employment_starting_date, Deparment_Department_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // 2. Permanent Table (Middle Layer)
        String sqlPermanent = "INSERT INTO Permanent (Employee_idEmployee, Years_of_employment) VALUES (?, ?)";

        // 3. Permanent Teaching Table (Leaf Layer)
        String sqlTeaching = "INSERT INTO `Permanent Teaching Employee` (`Permenant_Employee_idEmployee`, `research_allowence`) VALUES (?, ?)";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start Transaction

            // --- INSERT 1: Employee ---
            try (PreparedStatement ps1 = conn.prepareStatement(sqlEmployee)) {
                ps1.setInt(1, id);
                ps1.setString(2, name);
                ps1.setString(3, address);
                ps1.setString(4, iban);
                ps1.setString(5, phone);
                ps1.setInt(6, isMarried ? 1 : 0);
                ps1.setString(7, bank);
                ps1.setDate(8, startDate);
                ps1.setString(9, departmentName);
                ps1.executeUpdate();
            }

            // --- INSERT 2: Permanent ---
            try (PreparedStatement ps2 = conn.prepareStatement(sqlPermanent)) {
                ps2.setInt(1, id); // Uses the same ID
                ps2.setInt(2, yearsOfEmployment);
                ps2.executeUpdate();
            }

            // --- INSERT 3: Permanent Teaching ---
            try (PreparedStatement ps3 = conn.prepareStatement(sqlTeaching)) {
                ps3.setInt(1, id); // Uses the same ID again
                ps3.setDouble(2, researchAllowance); // The extra field
                ps3.executeUpdate();
            }

            conn.commit(); // Save all 3 tables together
            System.out.println("Success: Permanent Teaching Employee hired.");

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        }
    }

    public void hirePermanentAdministrative(int id, String name, String address, String iban,
                                            String phone, boolean isMarried, String bank,
                                            String departmentName, Date startDate,
                                            int yearsOfEmployment) {

        Connection conn = null;
        String sqlEmployee = "INSERT INTO Employee (idEmployee, name, address, iban, phone_number, is_Married, bank, employment_starting_date, Deparment_Department_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlPermanent = "INSERT INTO Permanent (Employee_idEmployee, Years_of_employment) VALUES (?, ?)";
        String sqlAdmin = "INSERT INTO `Permanent Administrative Employee` (Permenant_Employee_idEmployee) VALUES (?)";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start Transaction

            // 1. Employee Table
            try (PreparedStatement ps1 = conn.prepareStatement(sqlEmployee)) {
                ps1.setInt(1, id);
                ps1.setString(2, name);
                ps1.setString(3, address);
                ps1.setString(4, iban);
                ps1.setString(5, phone);
                ps1.setInt(6, isMarried ? 1 : 0);
                ps1.setString(7, bank);
                ps1.setDate(8, startDate);
                ps1.setString(9, departmentName);
                ps1.executeUpdate();
            }

            // 2. Permanent Table
            try (PreparedStatement ps2 = conn.prepareStatement(sqlPermanent)) {
                ps2.setInt(1, id);
                ps2.setInt(2, yearsOfEmployment);
                ps2.executeUpdate();
            }

            // 3. Administrative Table (Only needs ID)
            try (PreparedStatement ps3 = conn.prepareStatement(sqlAdmin)) {
                ps3.setInt(1, id);
                ps3.executeUpdate();
            }

            conn.commit();
            System.out.println("Success: Permanent Administrative Employee hired.");

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        }
    }

    public void hireContractorTeaching(int id, String name, String address, String iban,
                                       String phone, boolean isMarried, String bank,
                                       String departmentName, Date startDate,
                                       double libraryAllowance) { // specific to teaching

        Connection conn = null;
        // 1. Base Employee
        String sqlEmployee = "INSERT INTO Employee (idEmployee, name, ...) VALUES (?, ...)";
        // 2. Middle Type
        String sqlContractor = "INSERT INTO Contractor (Employee_idEmployee) VALUES (?)";
        // 3. Specific Leaf Type
        String sqlTeaching = "INSERT INTO `Contractor Teaching Employee` (Contractor_Employee_idEmployee, `Library Allowence`) VALUES (?, ?)";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start Transaction

            // --- INSERT 1: Employee ---
            try (PreparedStatement ps1 = conn.prepareStatement(sqlEmployee)) {
                ps1.setInt(1, id);
                // ... set other fields ...
                ps1.executeUpdate();
            }

            // --- INSERT 2: Contractor ---
            try (PreparedStatement ps2 = conn.prepareStatement(sqlContractor)) {
                ps2.setInt(1, id);
                ps2.executeUpdate();
            }

            // --- INSERT 3: Contractor Teaching ---
            try (PreparedStatement ps3 = conn.prepareStatement(sqlTeaching)) {
                ps3.setInt(1, id);
                ps3.setDouble(2, libraryAllowance);
                ps3.executeUpdate();
            }

            conn.commit(); // Save all 3
            System.out.println("Success: Contractor Teaching Employee hired.");

        } catch (SQLException e) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ex) {
            }
            e.printStackTrace();
        }
    }

    public void hireContractorAdministrative(int id, String name, String address, String iban,
                                             String phone, boolean isMarried, String bank,
                                             String departmentName, Date startDate) {

        Connection conn = null;
        String sqlEmployee = "INSERT INTO Employee (idEmployee, name, address, iban, phone_number, is_Married, bank, employment_starting_date, Deparment_Department_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlContractor = "INSERT INTO Contractor (Employee_idEmployee) VALUES (?)";
        String sqlAdmin = "INSERT INTO `Contractor Administrative Employee` (Contractor_Employee_idEmployee) VALUES (?)";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Employee
            try (PreparedStatement ps1 = conn.prepareStatement(sqlEmployee)) {
                ps1.setInt(1, id);
                ps1.setString(2, name);
                ps1.setString(3, address);
                ps1.setString(4, iban);
                ps1.setString(5, phone);
                ps1.setInt(6, isMarried ? 1 : 0);
                ps1.setString(7, bank);
                ps1.setDate(8, startDate);
                ps1.setString(9, departmentName);
                ps1.executeUpdate();
            }

            // 2. Contractor
            try (PreparedStatement ps2 = conn.prepareStatement(sqlContractor)) {
                ps2.setInt(1, id);
                ps2.executeUpdate();
            }

            // 3. Admin Contractor
            try (PreparedStatement ps3 = conn.prepareStatement(sqlAdmin)) {
                ps3.setInt(1, id);
                ps3.executeUpdate();
            }

            conn.commit();
            System.out.println("Success: Contractor Administrative Employee hired.");

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        }
    }

    public void updateEmployeeDetails(int id, String newAddress, String newPhone, String newIban, String newBank, boolean newIsMarried) {
        Connection conn = null;
        PreparedStatement ps = null;

        // The SQL command does the "finding" (WHERE clause) and "updating" (SET clause) at the same time.
        String sql = "UPDATE Employee SET address = ?, phone_number = ?, iban = ?, bank = ?, is_Married = ? WHERE idEmployee = ?";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Transaction start

            ps = conn.prepareStatement(sql);

            // Fill in the NEW values
            ps.setString(1, newAddress);
            ps.setString(2, newPhone);
            ps.setString(3, newIban);
            ps.setString(4, newBank);
            ps.setInt(5, newIsMarried ? 1 : 0); // Convert boolean to MySQL TINYINT

            // Fill in the ID so SQL knows which row to update
            ps.setInt(6, id);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                conn.commit();
                System.out.println("Success: Details updated for Employee ID " + id);
            } else {
                System.out.println("Fail: Employee ID " + id + " not found.");
            }

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
            // Optional: close conn if you aren't using a pool
        }
    }

    public void fireEmployee(int id) {
        Connection conn = null;
        PreparedStatement ps = null;

        String sql = "UPDATE Employee SET Is_Active = 0 WHERE idEmployee = ?";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if(rows > 0) {
                System.out.println("Success: Employee " + id + " is now inactive (fired).");
            } else {
                System.out.println("Error: Employee not found.");
            }

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
        }
    }

    public void ExecutePayroll() {
        Connection conn = null;
        PreparedStatement ps = null;

    }
}