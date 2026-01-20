package Backend;

import JDBC.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.ResultSet;

public class EmployeeManager {

    public void hirePermanentTeaching(int id, String name, String address, String iban,
                                      String phone, boolean isMarried, String bank,
                                      String departmentName, Date startDate,
                                      int yearsOfEmployment, double researchAllowance) {

        Connection conn = null;
        // Διόρθωση Ημερομηνίας: Πάντα 1η του μήνα
        java.time.LocalDate localStart = startDate.toLocalDate();
        startDate = Date.valueOf(localStart.withDayOfMonth(1));

        // Διόρθωση 1: Προσθήκη Role και 10ου ερωτηματικού (?)
        String sqlEmployee = "INSERT INTO Employee (idEmployee, name, address, iban, phone_number, is_Married, bank, employment_starting_date, Department_Department_name, Role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlPermanent = "INSERT INTO Permanent (Employee_idEmployee, Years_of_employment) VALUES (?, ?)";
        String sqlTeaching = "INSERT INTO `Permanent Teaching Employee` (`Permenant_Employee_idEmployee`, `research_allowence`) VALUES (?, ?)";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

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
                ps1.setString(10, "PERMANENT_TEACHING"); // Αυτό χτυπούσε πριν
                ps1.executeUpdate();
            }

            // --- INSERT 2: Permanent ---
            try (PreparedStatement ps2 = conn.prepareStatement(sqlPermanent)) {
                ps2.setInt(1, id);
                ps2.setInt(2, yearsOfEmployment);
                ps2.executeUpdate();
            }

            // --- INSERT 3: Permanent Teaching ---
            try (PreparedStatement ps3 = conn.prepareStatement(sqlTeaching)) {
                ps3.setInt(1, id);
                ps3.setDouble(2, researchAllowance);
                ps3.executeUpdate();
            }

            conn.commit();
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
        java.time.LocalDate localStart = startDate.toLocalDate();
        startDate = Date.valueOf(localStart.withDayOfMonth(1));

        // Διόρθωση 1: Προσθήκη Role και 10ου ερωτηματικού
        String sqlEmployee = "INSERT INTO Employee (idEmployee, name, address, iban, phone_number, is_Married, bank, employment_starting_date, Department_Department_name, Role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlPermanent = "INSERT INTO Permanent (Employee_idEmployee, Years_of_employment) VALUES (?, ?)";
        String sqlAdmin = "INSERT INTO `Permanent Administrative Employee` (Permenant_Employee_idEmployee) VALUES (?)";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

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
                ps1.setString(10, "PERMANENT_ADMINISTRATIVE");
                ps1.executeUpdate();
            }

            // 2. Permanent Table
            try (PreparedStatement ps2 = conn.prepareStatement(sqlPermanent)) {
                ps2.setInt(1, id);
                ps2.setInt(2, yearsOfEmployment);
                ps2.executeUpdate();
            }

            // 3. Administrative Table
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
                                       double libraryAllowance) {

        Connection conn = null;
        java.time.LocalDate localStart = startDate.toLocalDate();
        startDate = Date.valueOf(localStart.withDayOfMonth(1));

        // FIXED: Changed `Library Allowence` to `library_allowence`
        String sqlEmployee = "INSERT INTO Employee (idEmployee, name, address, iban, phone_number, is_Married, bank, employment_starting_date, Department_Department_name, Role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlContractor = "INSERT INTO Contractor (Employee_idEmployee) VALUES (?)";
        String sqlTeaching = "INSERT INTO `Contractor Teaching Employee` (Contractor_Employee_idEmployee, library_allowence) VALUES (?, ?)";

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
                ps1.setString(10, "CONTRACTOR_TEACHING");
                ps1.executeUpdate();
            }

            // 2. Contractor
            try (PreparedStatement ps2 = conn.prepareStatement(sqlContractor)) {
                ps2.setInt(1, id);
                ps2.executeUpdate();
            }

            // 3. Contractor Teaching
            try (PreparedStatement ps3 = conn.prepareStatement(sqlTeaching)) {
                ps3.setInt(1, id);
                ps3.setDouble(2, libraryAllowance);
                ps3.executeUpdate();
            }

            conn.commit();
            System.out.println("Success: Contractor Teaching Employee hired.");

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        }
    }

    public void hireContractorAdministrative(int id, String name, String address, String iban,
                                             String phone, boolean isMarried, String bank,
                                             String departmentName, Date startDate) {

        Connection conn = null;
        java.time.LocalDate localStart = startDate.toLocalDate();
        startDate = Date.valueOf(localStart.withDayOfMonth(1));

        // Διόρθωση: Προσθήκη Role
        String sqlEmployee = "INSERT INTO Employee (idEmployee, name, address, iban, phone_number, is_Married, bank, employment_starting_date, Department_Department_name, Role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                ps1.setString(10, "CONTRACTOR_ADMINISTRATIVE");
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

    // --- ΟΙ ΥΠΟΛΟΙΠΕΣ ΜΕΘΟΔΟΙ ---

    public void updateEmployeeDetails(int id, String newAddress, String newPhone, String newIban, String newBank, boolean newIsMarried) {
        String sql = "UPDATE Employee SET address = ?, phone_number = ?, iban = ?, bank = ?, is_Married = ? WHERE idEmployee = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newAddress);
            ps.setString(2, newPhone);
            ps.setString(3, newIban);
            ps.setString(4, newBank);
            ps.setInt(5, newIsMarried ? 1 : 0);
            ps.setInt(6, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) System.out.println("Success: Details updated for Employee ID " + id);
            else System.out.println("Fail: Employee ID " + id + " not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fireEmployee(int id) {
        String sql = "UPDATE Employee SET Is_Active = 0 WHERE idEmployee = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if(rows > 0) System.out.println("Success: Employee " + id + " is now inactive (fired).");
            else System.out.println("Error: Employee not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ExecutePayroll() {
        DAO.EmployeeDAO employeeDAO = new DAO.EmployeeDAO();
        SalaryService salaryService = new SalaryService();
        java.util.List<Model.Employee> employees = employeeDAO.getAllEmployees();

        System.out.println("Starting Payroll Execution...");
        int count = 0;
        for (Model.Employee emp : employees) {
            if (emp.isActive()) {
                salaryService.calculateMonthlySalary(emp.getId());
                count++;
            }
        }
        System.out.println("Payroll finished. Processed " + count + " employees.");
    }

    public void updateResearchAllowance(int empId, double newAllowance) {
        String sql = "UPDATE `Permanent Teaching Employee` SET research_allowence = ? WHERE Permenant_Employee_idEmployee = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newAllowance);
            ps.setInt(2, empId);
            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Success: Research allowance updated for Employee ID " + empId);
            else System.out.println("Error: Employee " + empId + " not found or is not a Permanent Teaching Employee.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addChild(int employeeId, String childName, int age) {
        String sql = "INSERT INTO Child (age, name, Employee_idEmployee) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, age);
            ps.setString(2, childName);
            ps.setInt(3, employeeId);
            ps.executeUpdate();
            System.out.println("Success: Child added for employee " + employeeId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Διόρθωση 3: Ελέγχει αν υπάρχει συμβόλαιο. Αν όχι -> INSERT, Αν ναι -> UPDATE
    public void renewContract(int employeeId, Date newStartDate, Date newEndDate, double newSalary) {
        String checkSql = "SELECT idContract FROM Contract WHERE Contractor_Employee_idEmployee = ?";
        String updateSql = "UPDATE Contract SET start_Date = ?, end_Date = ?, salary = ? WHERE Contractor_Employee_idEmployee = ?";
        String insertSql = "INSERT INTO Contract (start_Date, end_Date, salary, Contractor_Employee_idEmployee, idContract) VALUES (?, ?, ?, ?, ?)";

        java.time.LocalDate localStart = newStartDate.toLocalDate().withDayOfMonth(1);
        Date fixedStart = java.sql.Date.valueOf(localStart);

        try (Connection conn = DBConnection.getConnection()) {
            boolean exists = false;
            try (PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
                psCheck.setInt(1, employeeId);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) exists = true;
                }
            }

            if (exists) {
                // UPDATE
                try (PreparedStatement psUpd = conn.prepareStatement(updateSql)) {
                    psUpd.setDate(1, fixedStart);
                    psUpd.setDate(2, newEndDate);
                    psUpd.setDouble(3, newSalary);
                    psUpd.setInt(4, employeeId);
                    psUpd.executeUpdate();
                    System.out.println("Success: Contract updated (renewed) for " + employeeId);
                }
            } else {
                // INSERT (Generate a random ID for contract since it's not auto-increment)
                int newContractId = (int)(System.currentTimeMillis() % 1000000) + employeeId;
                try (PreparedStatement psIns = conn.prepareStatement(insertSql)) {
                    psIns.setDate(1, fixedStart);
                    psIns.setDate(2, newEndDate);
                    psIns.setDouble(3, newSalary);
                    psIns.setInt(4, employeeId);
                    psIns.setInt(5, newContractId);
                    psIns.executeUpdate();
                    System.out.println("Success: New contract created for " + employeeId);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}