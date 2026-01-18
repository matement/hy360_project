package Model;

public class Permanent {
    private int employeeID;
    private int yearsOfEmployment;
    private double basicSalary;

    public Permanent(int employeeID, int yearsOfEmployment, double basicSalary) {
        this.employeeID = employeeID;
        this.yearsOfEmployment = yearsOfEmployment;
        this.basicSalary = basicSalary;
    }

    public Permanent() {
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public int getYearsOfEmployment() {
        return yearsOfEmployment;
    }

    public void setYearsOfEmployment(int yearsOfEmployment) {
        this.yearsOfEmployment = yearsOfEmployment;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }
}
