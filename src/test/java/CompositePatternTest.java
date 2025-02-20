

import org.junit.Test;
import org.organigramma.composite.*;

import static org.junit.Assert.*;

public class CompositePatternTest {

    @Test
    public void testAddAndRemoveComponents() {
        Unit mainUnit = new Unit("Dipartimento Principale");

        Role managerRole = new Role("Manager", "Gestisce il team", "Esperienza di gestione", 50000);
        Role analystRole = new Role("Analista", "Analizza i dati", "Esperienza con i dati", 40000);
        mainUnit.addAllowedRole(managerRole);
        mainUnit.addAllowedRole(analystRole);
        Employee emp1 = new Employee("Mario", "Rossi", "Roma", "Via Roma 1", 40);
        Employee emp2 = new Employee("Giulia", "Verdi", "Milano", "Via Milano 2", 30);

        emp1.addRole(mainUnit, managerRole);
        emp2.addRole(mainUnit, analystRole);
        
        mainUnit.addEmployee(emp1);
        mainUnit.addEmployee(emp2);

        // Verifica che i dipendenti siano stati aggiunti
        assertEquals(2, mainUnit.getEmployees().size());
        assertTrue(mainUnit.getEmployees().contains(emp1));
        assertTrue(mainUnit.getEmployees().contains(emp2));
        
        // Rimuovi un dipendente e verifica
        mainUnit.removeEmployee(emp1);
        assertEquals(1, mainUnit.getEmployees().size());
        assertFalse(mainUnit.getEmployees().contains(emp1));
    }
}
