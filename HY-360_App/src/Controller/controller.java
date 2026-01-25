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

//connects ui with backend
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

    //initializes the database
    public void initializeDatabase() {
        DatabaseHelper.clearDatabase();
        DatabaseHelper.setupDatabaseData();
    }

    //retrieves all employees info
    public List<Object[]> getAllEmployees() {
        List<Object[]> list = new ArrayList<>();
        List<Employee> employees = employeeDAO.getAllEmployees();

        for (Employee e : employees) {
            List<String[]> childrenData = employeeDAO.getChildren(e.getId());
            StringBuilder namesBuilder = new StringBuilder();
            StringBuilder agesBuilder = new StringBuilder();

            //divides kids
            for (int i = 0; i < childrenData.size(); i++) {
                String[] child = childrenData.get(i);
                namesBuilder.append(child[0]); 
                agesBuilder.append(child[1]);  
                if (i < childrenData.size() - 1) {
                    namesBuilder.append(", ");
                    agesBuilder.append(", ");
                }
            }

            //info for married and status
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

    //retrieves the end of contract
    public String getContractEndDate(int employeeId) {
        return contractDAO.getLatestContractEndDate(employeeId);
    }

    //adds new Employee
    public boolean addEmployee(String name, String marital, int kids, String kNames, String kAges,
                               String cat, String dept, String dateStr, String addr, String phone,
                               String bank, String iban, String endDateStr) { 
        try {
            Date startDate = Date.valueOf(dateStr);
            boolean isMarried = "Έγγαμος".equals(marital) || "Married".equals(marital);
            int nextId = new Random().nextInt(80000) + 10000; 

            //chooses category
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

            //retrives employees kids
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

    //updates employee details
    public boolean updateEmployee(int id, String name, String marital, int kids, String kNames, String kAges,
                                  String cat, String dept, String date, String addr, String phone,
                                  String bank, String iban) {
        try {
            boolean isMarried = "Έγγαμος".equals(marital);
            manager.updateEmployeeDetails(id, addr, phone, iban, bank, isMarried);
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    //fires employee
    public boolean fireEmployee(int id) {
        try { manager.fireEmployee(id); return true; } catch (Exception e) { return false; }
    }

    //new contract
    public void renewContract(int id, Date start, Date end, double salary) {
        manager.renewContract(id, start, end, salary);
    }

    //pays all employees
    public void runPayroll(int month, int year) {
        payrollManager.runMonthlyPayroll(month, year);
    }

    //payment report
    public List<String> getPaymentHistory(int employeeId) {
        return reportDAO.getPaymentHistory(employeeId);
    }

    //salary per category
    public Map<String, String> getSalaryStatsPerRole() {
        return reportDAO.getSalaryStatsPerRole();
    }

    //total cost
    public double getTotalPayrollCost(int month, int year) {
        return reportDAO.getTotalPayrollCost(month, year);
    }

    //total cost per category
    public Map<String, Double> getTotalCostPerRole(int month, int year) {
        return reportDAO.getTotalCostPerRole(month, year);
    }

    //retrieves salary rates
    public Map<String, Double> getAllSalaryRates() {
        return salaryRateDAO.getAllSalaryRates();
    }

    //updates base salary
    public void updateBaseSalary(String roleName, double newSalary) {
        salaryRateDAO.UpdateBaseSalary(roleName, newSalary);
    }

    //update library allowance
    public void updateLibraryAllowance(double newAmount) {
        manager.updateGlobalLibraryAllowance(newAmount);
    }

    //update researchAllowance
    public void updateResearchAllowance(double newAmount){
        manager.updateGlobalResearchAllowance(newAmount);
    }

}