package org.organigramma.composite;



import java.io.Serial;
import java.io.Serializable;
import java.util.*;



public class Unit implements UnitComponent, Serializable {
    private String name;
    private List<Employee> employees;
    private List<Unit> subUnits;
    private List<Role> allowedRoles;
    @Serial
    private static final long serialVersionUID = 1L;
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
                employee.getRolebyUnits().put(this.getName(), employee.getRole(this));
            } else if(!allowedRoles.contains(employee.getRole(this))){
                employee.getRolebyUnits().put(this.getName(),new Role("Nessun Ruolo","none","none",0));
            }
            else throw new RuntimeException("Employee " + employee.getName() + " has no role in unit "+this.getName());
        }else throw new RuntimeException("Employee " + employee.getName() + " has already been added to unit "+this.getName());
    }

    public void removeEmployee(Employee employee) {
        if (employees.contains(employee)) {
            employees.remove(employee);
            employee.getRolebyUnits().remove(this.getName());
        }
    }

    public void addSubUnit(Unit subUnit) {
        if (!subUnits.contains(subUnit) && !this.equals(subUnit)) {
            subUnits.add(subUnit);
        }
        else throw new RuntimeException("Unit " + subUnit.getName() + " already present");
    }
    public void updateEmployee(Employee Oldemp,Employee emp) {
        for (int i = 0; i < employees.size(); i++) {
            Employee existingEmployee = employees.get(i);
            // Controllo se l'oggetto esistente è lo stesso del dipendente da aggiornare
            if (existingEmployee.equals(Oldemp)) {
                System.out.println("cane");
                // Aggiorno le informazioni del dipendente
                emp.setRole(this,existingEmployee.getRole(this));
                employees.set(i, emp);
                existingEmployee.update(emp);
                break;
            }
        }
    }

    //le subUnità sono tutte le unità diverse dalla radice
    public void removeSubUnit(Unit subUnit) {
        if (this.subUnits.contains(subUnit)) {
            // Rimuovo prima i dipendenti associati a questa unità
            List<Employee> employeesToRemove = new ArrayList<>(subUnit.getEmployees()); // Usa una copia per evitare ConcurrentModificationException
            for (Employee employee : employeesToRemove) {
                employee.getRolebyUnits().remove(subUnit.getName()); // rimuovo l'unità dal dipendente

            }
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
                Role r = e.getRolebyUnits().get(this.getName());

                if (r.equals(role)) {
                    e.getRolebyUnits().remove(this.getName()); // Rimuove il ruolo dall'unità specifica
                    iterator.remove(); // Rimuove il dipendente dalla lista degli impiegati dell'unità in modo sicuro
                }
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

    // Metodo per trovare un'unità per nome
    public Unit findUnitByName(String unitName) {
        // Controlla se il nome dell'unità corrente corrisponde
        if (this.name.equalsIgnoreCase(unitName)) {
            return this; // Restituisci l'unità corrente se il nome corrisponde
        }

        // Ricerca ricorsiva nelle sotto-unità
        for (Unit subUnit : subUnits) {
            Unit foundUnit = subUnit.findUnitByName(unitName);
            if (foundUnit != null) {
                return foundUnit; // Restituisci l'unità trovata
            }
        }

        return null; // Nessuna unità trovata con il nome specificato
    }


    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employee findEmployeeByName(String name) {
        for (Employee emp : employees) {
            if (emp.getName().equals(name)) {
                return emp;
            }
        }
        return null;
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