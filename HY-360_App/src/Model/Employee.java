package Model;
import java.sql.Date;

public class Employee {
    private int id;
    private String name;
    private String address;
    private String iban;
    private String phoneNumber;
    private boolean isMarried;
    private String bank;
    private Date employmentDate;
    private String departmentName;
    private boolean isActive;
    private String role;

    public Employee() {}

    public Employee(int id, String name, String address, String iban, String phoneNumber,
                    boolean isMarried, String bank, Date employmentDate, String departmentName, boolean isActive, String role) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.iban = iban;
        this.phoneNumber = phoneNumber;
        this.isMarried = isMarried;
        this.bank = bank;
        this.employmentDate = employmentDate;
        this.departmentName = departmentName;
        this.isActive = isActive;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isMarried() {
        return isMarried;
    }

    public void setMarried(boolean married) {
        isMarried = married;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public Date getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(Date employmentDate) {
        this.employmentDate = employmentDate;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
