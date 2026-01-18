package Model;

import java.util.Date;

public class Contractor {
    private int employeeId;
    private Date contractStart;
    private Date contractEnd;
    private double contractSalary;

    public Contractor() {
    }

    public Contractor(int employeeId, Date contractStart, Date contractEnd, double contractSalary) {
        this.employeeId = employeeId;
        this.contractStart = contractStart;
        this.contractEnd = contractEnd;
        this.contractSalary = contractSalary;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getContractStart() {
        return contractStart;
    }

    public void setContractStart(Date contractStart) {
        this.contractStart = contractStart;
    }

    public Date getContractEnd() {
        return contractEnd;
    }

    public void setContractEnd(Date contractEnd) {
        this.contractEnd = contractEnd;
    }

    public double getContractSalary() {
        return contractSalary;
    }

    public void setContractSalary(double contractSalary) {
        this.contractSalary = contractSalary;
    }
}
