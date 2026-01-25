package Backend;

import JDBC.DBConnection;
import java.sql.*;

public class EmployeeManager {

    private int getLookupId(Connection conn, String tableName, String idCol, String valCol, String value) throws SQLException {
        String sql = "SELECT " + idCol + " FROM " + tableName + " WHERE " + valCol + " = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Error: ID not found for table " + tableName + " with value: " + value);
    }

    public void hirePermanentTeaching(int id, String name, String address, String iban,
                                      String phone, boolean isMarried, String bank,
                                      String departmentName, Date startDate,
                                      int yearsOfEmployment, double researchAllowance) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int deptId = getLookupId(conn, "Department", "departmentID", "department_name", departmentName);
            int roleId = getLookupId(conn, "Role", "roleID", "role_name", "TEACHING");
            int typeId = getLookupId(conn, "Employment_type", "typeID", "type_name", "PERMANENT");

            String sqlEmployee = "INSERT INTO Employee (idEmployee, name, address, iban, phone_number, is_Married, bank, employment_starting_date, Department_departmentID, Role_roleID, Employment_type_typeID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlEmployee)) {
                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setString(3, address);
                ps.setString(4, iban);
                ps.setString(5, phone);
                ps.setInt(6, isMarried ? 1 : 0);
                ps.setString(7, bank);
                ps.setDate(8, startDate);
                ps.setInt(9, deptId);
                ps.setInt(10, roleId);
                ps.setInt(11, typeId);
                ps.executeUpdate();
            }

            String sqlAllow = "INSERT INTO Allowences (Employee_idEmployee, research_allowence, library_allowence) VALUES (?, ?, 0)";
            try (PreparedStatement ps = conn.prepareStatement(sqlAllow)) {
                ps.setInt(1, id);
                ps.setDouble(2, researchAllowance);
                ps.executeUpdate();
            }

            conn.commit();
            System.out.println("Success: Permanent Teaching Employee hired.");

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        }
    }

    public void hirePermanentAdministrative(int id, String name, String address, String iban,
                                            String phone, boolean isMarried, String bank,
                                            String departmentName, Date startDate,
                                            int yearsOfEmployment) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int deptId = getLookupId(conn, "Department", "departmentID", "department_name", departmentName);
            int roleId = getLookupId(conn, "Role", "roleID", "role_name", "ADMINISTRATIVE");
            int typeId = getLookupId(conn, "Employment_type", "typeID", "type_name", "PERMANENT");

            String sqlEmployee = "INSERT INTO Employee (idEmployee, name, address, iban, phone_number, is_Married, bank, employment_starting_date, Department_departmentID, Role_roleID, Employment_type_typeID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlEmployee)) {
                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setString(3, address);
                ps.setString(4, iban);
                ps.setString(5, phone);
                ps.setInt(6, isMarried ? 1 : 0);
                ps.setString(7, bank);
                ps.setDate(8, startDate);
                ps.setInt(9, deptId);
                ps.setInt(10, roleId);
                ps.setInt(11, typeId);
                ps.executeUpdate();
            }

            String sqlAllow = "INSERT INTO Allowences (Employee_idEmployee, research_allowence, library_allowence) VALUES (?, 0, 0)";
            try (PreparedStatement ps = conn.prepareStatement(sqlAllow)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            conn.commit();
            System.out.println("Success: Permanent Administrative Employee hired.");

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        }
    }

    public void hireContractorTeaching(int id, String name, String address, String iban,
                                       String phone, boolean isMarried, String bank,
                                       String departmentName, Date startDate,
                                       double libraryAllowance) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int deptId = getLookupId(conn, "Department", "departmentID", "department_name", departmentName);
            int roleId = getLookupId(conn, "Role", "roleID", "role_name", "TEACHING");
            int typeId = getLookupId(conn, "Employment_type", "typeID", "type_name", "CONTRACTOR");

            String sqlEmployee = "INSERT INTO Employee (idEmployee, name, address, iban, phone_number, is_Married, bank, employment_starting_date, Department_departmentID, Role_roleID, Employment_type_typeID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlEmployee)) {
                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setString(3, address);
                ps.setString(4, iban);
                ps.setString(5, phone);
                ps.setInt(6, isMarried ? 1 : 0);
                ps.setString(7, bank);
                ps.setDate(8, startDate);
                ps.setInt(9, deptId);
                ps.setInt(10, roleId);
                ps.setInt(11, typeId);
                ps.executeUpdate();
            }

            String sqlAllow = "INSERT INTO Allowences (Employee_idEmployee, research_allowence, library_allowence) VALUES (?, 0, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlAllow)) {
                ps.setInt(1, id);
                ps.setDouble(2, libraryAllowance);
                ps.executeUpdate();
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
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int deptId = getLookupId(conn, "Department", "departmentID", "department_name", departmentName);
            int roleId = getLookupId(conn, "Role", "roleID", "role_name", "ADMINISTRATIVE");
            int typeId = getLookupId(conn, "Employment_type", "typeID", "type_name", "CONTRACTOR");

            String sqlEmployee = "INSERT INTO Employee (idEmployee, name, address, iban, phone_number, is_Married, bank, employment_starting_date, Department_departmentID, Role_roleID, Employment_type_typeID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlEmployee)) {
                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setString(3, address);
                ps.setString(4, iban);
                ps.setString(5, phone);
                ps.setInt(6, isMarried ? 1 : 0);
                ps.setString(7, bank);
                ps.setDate(8, startDate);
                ps.setInt(9, deptId);
                ps.setInt(10, roleId);
                ps.setInt(11, typeId);
                ps.executeUpdate();
            }

            String sqlAllow = "INSERT INTO Allowences (Employee_idEmployee, research_allowence, library_allowence) VALUES (?, 0, 0)";
            try (PreparedStatement ps = conn.prepareStatement(sqlAllow)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            conn.commit();
            System.out.println("Success: Contractor Administrative Employee hired.");

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        }
    }

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
            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Success: Details updated.");
            else System.out.println("Error: Employee not found.");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void fireEmployee(int id) {
        String sql = "UPDATE Employee SET Is_Active = 0 WHERE idEmployee = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Success: Employee " + id + " is now inactive (fired).");
            else System.out.println("Error: Employee not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- 1. Ενημέρωση Επιδόματος Έρευνας (Για Μόνιμους Διδακτικούς) ---
    public void updateResearchAllowance(int empId, double newAmount) {
        String sql = "UPDATE Allowences SET research_allowence = ? WHERE Employee_idEmployee = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, newAmount);
            ps.setInt(2, empId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Success: Research allowance for Employee " + empId + " updated to " + newAmount + "€");
            } else {
                System.out.println("Error: Employee " + empId + " not found in Allowences table.");
            }
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
            System.out.println("Success: Child added.");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void renewContract(int employeeId, Date newStartDate, Date newEndDate, double newSalary) {
        String sql = "INSERT INTO Contract (start_Date, end_Date, salary, Employee_idEmployee) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, newStartDate);
            ps.setDate(2, newEndDate);
            ps.setDouble(3, newSalary);
            ps.setInt(4, employeeId);
            ps.executeUpdate();
            System.out.println("Success: Contract renewed/created.");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void ExecutePayroll() {
        DAO.EmployeeDAO employeeDAO = new DAO.EmployeeDAO();
        SalaryService salaryService = new SalaryService();
        java.util.List<Model.Employee> employees = employeeDAO.getAllEmployees();

        System.out.println("Starting Payroll Execution...");
        for (Model.Employee emp : employees) {
            salaryService.calculateMonthlySalary(emp.getId());
        }
        System.out.println("Payroll finished.");
    }

    // --- 2. Ενημέρωση Επιδόματος Βιβλιοθήκης (Για Συμβασιούχους Διδακτικούς) ---
    public void updateLibraryAllowance(int empId, double newAmount) {
        String sql = "UPDATE Allowences SET library_allowence = ? WHERE Employee_idEmployee = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, newAmount);
            ps.setInt(2, empId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Success: Library allowance for Employee " + empId + " updated to " + newAmount + "€");
            } else {
                System.out.println("Error: Employee " + empId + " not found in Allowences table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}