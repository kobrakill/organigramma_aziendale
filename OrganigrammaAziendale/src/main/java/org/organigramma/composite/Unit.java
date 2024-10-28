package org.organigramma.composite;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.*;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
@JsonIgnoreProperties("allowedRoleNames")
public class Unit implements UnitComponent {
    private String name;
    @JsonIdentityReference
    private List<Employee> employees;
    private List<Unit> subUnits;
    private List<Role> allowedRoles;

    public Unit() {
    }

    public Unit(String name) {
        this.name = name;
        employees = new ArrayList<>();
        subUnits = new ArrayList<>();
        allowedRoles = new ArrayList<>();
    }
    public void addEmployee(Employee employee) {
        if (!employees.contains(employee)) {
            employees.add(employee);
            if(allowedRoles.contains(employee.getRole(this)) && employee.getRole(this)!=null){
                employee.getRolebyUnits().put(this, employee.getRole(this));
            } else if(!allowedRoles.contains(employee.getRole(this))){
                employee.getRolebyUnits().put(this,new Role("Nessun Ruolo","none","none",0));
            }
            else throw new RuntimeException("Employee " + employee.getName() + " has no role in unit "+this.getName());
        }else throw new RuntimeException("Employee " + employee.getName() + " has already been added to unit "+this.getName());
    }

    public void removeEmployee(Employee employee) {
        if (employees.contains(employee)) {
            employees.remove(employee);
            employee.getRolebyUnits().remove(this);
        }
    }

    public void addSubUnit(Unit subUnit) {
        if (!subUnits.contains(subUnit) && !this.equals(subUnit)) {
            subUnits.add(subUnit);
        }
        else throw new RuntimeException("Unit " + subUnit.getName() + " already present");
    }

    public void removeSubUnit(Unit subUnit) {
        if (subUnits.contains(subUnit)) {
            subUnits.remove(subUnit);
        }
    }



    public void addAllowedRole(Role role) {
        if (!allowedRoles.contains(role)) {
            allowedRoles.add(role);
        }
    }

    public void removeAllowedRole(Role role) {
        if (allowedRoles.contains(role)) {
            allowedRoles.remove(role);

            ListIterator<Employee> iterator = employees.listIterator();
            System.out.println(employees.size());
            while (iterator.hasNext()) {
                Employee e = iterator.next();
                Role r= e.getRolebyUnits().get(this);

                if (r.equals(role)) {
                    e.getRolebyUnits().put(this,new Role("Nessun Ruolo","none","none",0));
                }

                //iterator.remove();

            }
        }
    }





    public void printStructure() {
        printStructure("");
    }

    private void printStructure(String prefix) {
        System.out.println(prefix + name);

        for (Unit subUnit : subUnits) {
            subUnit.printStructure(prefix + "--");
        }

        for (Employee employee : employees) {
            System.out.println(prefix + "--" + employee.getName() + " ruolo: "+employee.getRole(this).getName());
        }
    }



    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Employee> getEmployees() {
        return employees;
    }



    public List<Unit> getSubUnits() {
        return subUnits;
    }


    public List<Role> getAllowedRoles() {
        return allowedRoles;
    }



    // equals and hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Unit unit)) return false;
        return Objects.equals(name, unit.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    //toString

    @Override
    public String toString() {
        return "Unit{" +
                "name='" + name + '\'' +
                '}';
    }
}