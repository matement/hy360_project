package Backend;

import JDBC.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

public class EmployeeManager {

    public void hirePermanentEmployee(int id, String name, String address, String iban,
                                      String phone, boolean isMarried, String bank,
                                      String departmentName, Date startDate,
                                      int yearsOfEmployment) {

        Connection conn = null;
        String sqlEmployee = "INSERT INTO Employee (idEmployee, name, address, iban, phone_number, is_Married, bank, employment_starting_date, Deparment_Department_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlPermanent = "INSERT INTO Permanent (Employee_idEmployee, Years_of_employment) VALUES (?, ?)";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start Transaction

            // 1. Insert Generic Employee
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

            // 2. Insert Permanent Specifics
            try (PreparedStatement ps2 = conn.prepareStatement(sqlPermanent)) {
                ps2.setInt(1, id);
                ps2.setInt(2, yearsOfEmployment);
                ps2.executeUpdate();
            }

            conn.commit(); // Save
            System.out.println("Success: Permanent Employee hired.");

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        }
    }
}