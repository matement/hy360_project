package Controller;

import Backend.EmployeeManager;
import Backend.PayrollManager;
import DAO.*;
import JDBC.DatabaseHelper;
import Model.Employee;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class controller {

    private EmployeeManager manager;
    private PayrollManager payrollManager;
    
    private EmployeeDAO employeeDAO;
    private ReportDAO reportDAO;
    private ContractDAO contractDAO;
    private SalaryRateDAO salaryRateDAO;

    public controller() {
        this.manager = new EmployeeManager();
        this.payrollManager = new PayrollManager(); 
        
        this.employeeDAO = new EmployeeDAO();
        this.reportDAO = new ReportDAO();
        this.contractDAO = new ContractDAO();
        this.salaryRateDAO = new SalaryRateDAO();
    }

    public void initializeDatabase() {
        DatabaseHelper.clearDatabase();
        DatabaseHelper.setupDatabaseData();
    }

    // --- 1. ΛΙΣΤΑ ΥΠΑΛΛΗΛΩΝ ---
    public List<Object[]> getAllEmployees() {
        List<Object[]> list = new ArrayList<>();
        List<Employee> employees = employeeDAO.getAllEmployees();

        for (Employee e : employees) {
            List<String[]> childrenData = employeeDAO.getChildren(e.getId());
            StringBuilder namesBuilder = new StringBuilder();
            StringBuilder agesBuilder = new StringBuilder();

            for (int i = 0; i < childrenData.size(); i++) {
                String[] child = childrenData.get(i);
                namesBuilder.append(child[0]); 
                agesBuilder.append(child[1]);  
                if (i < childrenData.size() - 1) {
                    namesBuilder.append(", ");
                    agesBuilder.append(", ");
                }
            }

            list.add(new Object[]{
                e.getId(), e.getName(), e.isMarried() ? "Έγγαμος" : "Άγαμος",
                childrenData.size(), namesBuilder.toString(), agesBuilder.toString(),
                e.getRole() + " " + e.getEmploymentType(), e.getDepartmentName(),
                e.getEmploymentDate(), e.getAddress(), e.getPhoneNumber(),
                e.getBank(), e.getIban(), e.isActive() ? "Ενεργός" : "Απολυμένος"
            });
        }
        return list;
    }

    public String getContractEndDate(int employeeId) {
        return contractDAO.getLatestContractEndDate(employeeId);
    }

    // --- 2. ΔΙΑΧΕΙΡΙΣΗ ΥΠΑΛΛΗΛΩΝ ---
    public boolean addEmployee(String name, String marital, int kids, String kNames, String kAges,
                               String cat, String dept, String dateStr, String addr, String phone,
                               String bank, String iban, String endDateStr) { 
        try {
            Date startDate = Date.valueOf(dateStr);
            boolean isMarried = "Έγγαμος".equals(marital) || "Married".equals(marital);
            int nextId = new Random().nextInt(80000) + 10000; 

            if (cat.contains("ADMINISTRATIVE") && cat.contains("PERMANENT")) {
                manager.hirePermanentAdministrative(nextId, name, addr, iban, phone, isMarried, bank, dept, startDate, 0);
            } else if (cat.contains("TEACHING") && cat.contains("PERMANENT")) {
                manager.hirePermanentTeaching(nextId, name, addr, iban, phone, isMarried, bank, dept, startDate, 0, 200.0);
            } else if (cat.contains("TEACHING") && cat.contains("CONTRACTOR")) {
                manager.hireContractorTeaching(nextId, name, addr, iban, phone, isMarried, bank, dept, startDate, 150.0);
                Date end = (endDateStr != null) ? Date.valueOf(endDateStr) : Date.valueOf(startDate.toLocalDate().plusMonths(6));
                manager.renewContract(nextId, startDate, end, 900.0);
            } else if (cat.contains("ADMINISTRATIVE") && cat.contains("CONTRACTOR")) {
                manager.hireContractorAdministrative(nextId, name, addr, iban, phone, isMarried, bank, dept, startDate);
                Date end = (endDateStr != null) ? Date.valueOf(endDateStr) : Date.valueOf(startDate.toLocalDate().plusMonths(6));
                manager.renewContract(nextId, startDate, end, 800.0);
            } else { return false; }

            if (employeeDAO.getEmployeeById(nextId) != null) {
                 if (kids > 0 && !kNames.isEmpty()) {
                    String[] names = kNames.split(",");
                    String[] ages = kAges.split(",");
                    for (int i = 0; i < names.length; i++) {
                        String childName = names[i].trim();
                        int childAge = 0;
                        try { childAge = Integer.parseInt(ages[i].trim()); } catch (Exception e) {}
                        manager.addChild(nextId, childName, childAge);
                    }
                }
                return true;
            } else { return false; }
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean updateEmployee(int id, String name, String marital, int kids, String kNames, String kAges,
                                  String cat, String dept, String date, String addr, String phone,
                                  String bank, String iban) {
        try {
            boolean isMarried = "Έγγαμος".equals(marital);
            manager.updateEmployeeDetails(id, addr, phone, iban, bank, isMarried);
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean fireEmployee(int id) {
        try { manager.fireEmployee(id); return true; } catch (Exception e) { return false; }
    }

    public void renewContract(int id, Date start, Date end, double salary) {
        manager.renewContract(id, start, end, salary);
    }

    // --- 3. ΜΙΣΘΟΔΟΣΙΑ ---
    public void runPayroll(int month, int year) {
        payrollManager.runMonthlyPayroll(month, year);
    }

    // --- 4. REPORTING (ΟΙ ΜΕΘΟΔΟΙ ΠΟΥ ΕΛΕΙΠΑΝ) ---

    // Μέθοδος για το κείμενο στατιστικών (επιστρέφει String)
    public String getAnalyticsReportText() {
        StringBuilder sb = new StringBuilder();
        int currentMonth = java.time.LocalDate.now().getMonthValue();
        int currentYear = java.time.LocalDate.now().getYear();

        sb.append("=== ΑΝΑΛΥΤΙΚΗ ΑΝΑΦΟΡΑ ΜΙΣΘΟΔΟΣΙΑΣ ===\n\n");

        // 1. Στατιστικά ανά Ρόλο
        sb.append(">> Στατιστικά Μισθών ανά Κατηγορία:\n");
        sb.append("------------------------------------------------\n");
        Map<String, String> stats = reportDAO.getSalaryStatsPerRole();
        for (String role : stats.keySet()) {
            sb.append(String.format("%-25s : %s\n", role, stats.get(role)));
        }
        sb.append("\n");

        // 2. Συνολικό Κόστος
        double totalCost = reportDAO.getTotalPayrollCost(currentMonth, currentYear);
        sb.append(">> Συνολικό Κόστος (" + currentMonth + "/" + currentYear + "):\n");
        sb.append("------------------------------------------------\n");
        sb.append(String.format("ΣΥΝΟΛΟ: %.2f €\n\n", totalCost));

        // 3. Κόστος ανά Ρόλο
        sb.append(">> Ανάλυση Κόστους ανά Ρόλο:\n");
        sb.append("------------------------------------------------\n");
        Map<String, Double> costPerRole = reportDAO.getTotalCostPerRole(currentMonth, currentYear);
        for (String role : costPerRole.keySet()) {
            sb.append(String.format("%-25s : %.2f €\n", role, costPerRole.get(role)));
        }
        
        sb.append("\n================================================");
        return sb.toString();
    }

    // --- 5. SALARY RATES ---
    public Map<String, Double> getAllSalaryRates() {
        return salaryRateDAO.getAllSalaryRates();
    }

    public void updateBaseSalary(String roleName, double newSalary) {
        salaryRateDAO.UpdateBaseSalary(roleName, newSalary);
    }
}