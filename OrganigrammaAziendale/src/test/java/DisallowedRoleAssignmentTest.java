import org.junit.Test;
import static org.junit.Assert.*;
import org.organigramma.composite.Employee;
import org.organigramma.composite.Role;
import org.organigramma.composite.Unit;

public class DisallowedRoleAssignmentTest {

    @Test(expected = IllegalArgumentException.class)
    public void testAssignDisallowedRole() {

        Unit unit = new Unit("Reparto Vendite");
        // Creo un ruolo non aggiunto nei ruoli ammissibili ed un dipendente
        Role role = new Role("Venditore", "Gestisce le vendite", "Esperienza in vendita", 30000);

        Employee emp = new Employee("Anna", "Rossi", "Milano", "Via Milano 1", 29);

        // Tento di assegnare il ruolo non consentito e verifica che non venga assegnato
        emp.addRole(unit, role);

    }
}
