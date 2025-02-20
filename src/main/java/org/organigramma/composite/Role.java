package org.organigramma.composite;





import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


public class Role implements UnitComponent, Serializable {

    private String name, description, requirements;
    private double salary;
    @Serial
    private static final long serialVersionUID = 1L;
    public Role(String name, String description, String requirements, double salary) {
        this.name = name;
        this.description = description;
        this.requirements = requirements;
        this.salary = salary;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public Number getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, requirements, salary);
    }

    @Override
    public String toString() {
        return name + ": " + "\n" +
                " description=" + description + " " + "\n" +
                ", requirements=" + requirements + " " + "\n" +
                ", salary=" + salary;
    }

    public String getName() {
        return name;
    }
}
