package Controller;

import Backend.EmployeeManager;
import Backend.PayrollManager;
import DAO.*;
import JDBC.DatabaseHelper;
import Model.Employee;

import java.sql.Date;
import java.time.LocalDate;
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

            int childCount = childrenData.size();

            list.add(new Object[]{
                e.getId(),              // 0
                e.getName(),            // 1
                e.isMarried() ? "Έγγαμος" : "Άγαμος", // 2
                childCount,             // 3
                namesBuilder.toString(),// 4
                agesBuilder.toString(), // 5
                e.getRole() + " " + e.getEmploymentType(), // 6
                e.getDepartmentName(),  // 7
                e.getEmploymentDate(),  // 8
                e.getAddress(),         // 9
                e.getPhoneNumber(),     // 10
                e.getBank(),            // 11
                e.getIban(),            // 12
                e.isActive() ? "Ενεργός" : "Απολυμένος" // 13
            });
        }
        return list;
    }

    public boolean addEmployee(String name, String marital, int kids, String kNames, String kAges,
                               String cat, String dept, String dateStr, String addr, String phone,
                               String bank, String iban) {
        try {
            Date startDate = Date.valueOf(dateStr);
            boolean isMarried = "Έγγαμος".equals(marital);
            
            
            int nextId = new Random().nextInt(80000) + 10000; 

            if (cat.contains("PERMANENT") && cat.contains("ADMINISTRATIVE")) {
                manager.hirePermanentAdministrative(nextId, name, addr, iban, phone, isMarried, bank, dept, startDate, 0);
            
            } else if (cat.contains("PERMANENT") && cat.contains("TEACHING")) {
                manager.hirePermanentTeaching(nextId, name, addr, iban, phone, isMarried, bank, dept, startDate, 0, 200.0);
            
            } else if (cat.contains("CONTRACTOR") && cat.contains("TEACHING")) {
                manager.hireContractorTeaching(nextId, name, addr, iban, phone, isMarried, bank, dept, startDate, 150.0);
                manager.renewContract(nextId, startDate, Date.valueOf(startDate.toLocalDate().plusMonths(6)), 900.0);
            
            } else if (cat.contains("CONTRACTOR") && cat.contains("ADMINISTRATIVE")) {
                manager.hireContractorAdministrative(nextId, name, addr, iban, phone, isMarried, bank, dept, startDate);
                manager.renewContract(nextId, startDate, Date.valueOf(startDate.toLocalDate().plusMonths(6)), 800.0);
            }

            if (employeeDAO.getEmployeeById(nextId) != null) {
                
                if (kids > 0 && !kNames.isEmpty()) {
                    String[] names = kNames.split(",");
                    String[] ages = kAges.split(",");
                    
                    for (int i = 0; i < names.length; i++) {
                        String childName = names[i].trim();
                        int childAge = 0;
                        if (i < ages.length) {
                            try { childAge = Integer.parseInt(ages[i].trim()); } catch (Exception e) {}
                        }
                        manager.addChild(nextId, childName, childAge);
                    }
                }
                return true; 
            } else {
                System.err.println("Controller: Η εγγραφή απέτυχε στο Backend (πιθανόν λάθος Τμήμα/Category).");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

   
    public boolean updateEmployee(int id, String name, String marital, int kids, String kNames, String kAges,
                                  String cat, String dept, String date, String addr, String phone,
                                  String bank, String iban) {
        try {
            boolean isMarried = "Έγγαμος".equals(marital);
            manager.updateEmployeeDetails(id, addr, phone, iban, bank, isMarried);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean fireEmployee(int id) {
        try {
            manager.fireEmployee(id);
            return true;
        } catch (Exception e) { return false; }
    }

    public void renewContract(int id, Date start, Date end, double salary) {
        manager.renewContract(id, start, end, salary);
    }

    public void runPayroll(int month, int year) {
        payrollManager.runMonthlyPayroll(month, year);
    }

    public Map<String, Double> getAllSalaryRates() {
        return salaryRateDAO.getAllSalaryRates();
    }

    public void updateBaseSalary(String roleName, double newSalary) {
        salaryRateDAO.UpdateBaseSalary(roleName, newSalary);
    }
}