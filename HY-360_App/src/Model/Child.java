package Model;

public class Child {
    private int employeeId;
    private String name;
    private int age;

    public Child() {
    }

    public Child(int age, String name, int employeeId) {
        this.age = age;
        this.name = name;
        this.employeeId = employeeId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
