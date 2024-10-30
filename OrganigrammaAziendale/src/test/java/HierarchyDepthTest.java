import org.junit.Test;
import static org.junit.Assert.*;
import org.organigramma.composite.Unit;

public class HierarchyDepthTest {

    @Test
    public void testHierarchyDepth() {
        Unit root = new Unit("CEO");
        Unit level1 = new Unit("Operations");
        Unit level2 = new Unit("Logistics");
        Unit level3 = new Unit("Inventory");

        root.addSubUnit(level1);
        level1.addSubUnit(level2);
        level2.addSubUnit(level3);

        // Verifico che ogni livello contenga correttamente il successivo
        assertEquals(level1, root.getSubUnits().getFirst());
        assertEquals(level2, level1.getSubUnits().getFirst());
        assertEquals(level3, level2.getSubUnits().getFirst());
    }
}
