import org.junit.Test;
import static org.junit.Assert.*;
import org.organigramma.composite.Unit;

public class RemoveUnitWithSubUnitsTest {

    @Test
    public void testRemoveUnitWithSubUnits() {
        Unit root = new Unit("Company");
        Unit department = new Unit("Sales Department");
        Unit team = new Unit("Sales Team");

        department.addSubUnit(team);
        root.addSubUnit(department);

        // Rimuovo il dipartimento e verifico che l'intera struttura interna sia rimossa
        root.removeSubUnit(department);
        assertTrue(root.getSubUnits().isEmpty());
    }
}
