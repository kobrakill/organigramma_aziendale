package org.organigramma.composite;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.*;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Employee implements UnitComponent {
    // IN UNA SINGOLA UNITA IL DIPENDENTE DEVE RICOPRIRE UN SOLO RUOLO
    @JsonIdentityReference
    private HashMap<Unit, Role> rolebyUnits = new HashMap<>();


    private String name, lastName, city, address;
    private int age;
    private int id;//final?

    //tiene traccia del prossimo nextId da assegnare
    private static int nextId = 1;


    public Employee(String firstName, String lastName, String city, String address, int age) {
        this.name = firstName;
        this.lastName = lastName;
        this.city = city;
        this.address = address;
        this.age = age;
        this.id = nextId++; // Assegna il valore corrente di nextId e dopo lo incrementa. Con l'id consento omonimie
    }

    public void addRole(Unit unit, Role role) {
        if(unit.getAllowedRoles().contains(role)){
            rolebyUnits.put(unit, role);
        }else throw new IllegalArgumentException("Role "+role.getName()+" for employee "+ this.name+ " of unit "+unit.getName()+ " not allowed");

    }

    public void removeRole(Unit unit) {
        rolebyUnits.remove(unit);
    }

    //getters and setters
    public HashMap<Unit, Role> getRolebyUnits() {
        return rolebyUnits;
    }

    public Role getRole(Unit unit){
        return rolebyUnits.get(unit);
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

    public int getId() {
        return id;
    }


    //equals and hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


}