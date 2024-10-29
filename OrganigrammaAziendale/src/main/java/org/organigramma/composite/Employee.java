package org.organigramma.composite;



import java.io.*;
import java.util.*;



public class Employee implements UnitComponent, Serializable {
    // IN UNA SINGOLA UNITA IL DIPENDENTE DEVE RICOPRIRE UN SOLO RUOLO
    private HashMap<String, Role> rolebyUnits = new HashMap<>();
    @Serial
    private static final long serialVersionUID = 1L;

    private String name, lastName, city, address;
    private int age;


    public Employee(String firstName, String lastName, String city, String address, int age) {
        this.name = firstName;
        this.lastName = lastName;
        this.city = city;
        this.address = address;
        this.age = age;
    }



    public void addRole(Unit unit, Role role) {
       if(unit.getAllowedRoles().contains(role)){
            rolebyUnits.put(unit.getName(), role);
       }else throw new IllegalArgumentException("Role "+role.getName()+" for employee "+ this.name+ " of unit "+unit.getName()+ " not allowed");

    }

    public void removeRole(Unit unit) {
        rolebyUnits.remove(unit.getName());
    }

    //getters and setters
    public HashMap<String, Role> getRolebyUnits() {
        return rolebyUnits;
    }

    public Role getRole(Unit unit){
        return rolebyUnits.get(unit.getName());
    }

    public Role getUnit(String name){
        return rolebyUnits.get(name);
    }

    public void setRole(Unit unit, Role role) {
        if (unit.getAllowedRoles().contains(role)) {
            rolebyUnits.put(unit.getName(), role);
        } else {
            throw new IllegalArgumentException("Role " + role.getName() + " for employee " + this.name +
                    " of unit " + unit.getName() + " not allowed");
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return age == employee.age && Objects.equals(name, employee.name) && Objects.equals(lastName, employee.lastName) && Objects.equals(city, employee.city) && Objects.equals(address, employee.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastName, city, address, age);
    }

    public void update(Employee emp) {
        this.name=emp.name;
        this.lastName=emp.lastName;
        this.city=emp.city;
        this.address=emp.address;
        this.age=emp.age;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", age=" + age +
                '}';
    }
}