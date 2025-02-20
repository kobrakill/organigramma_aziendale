import org.junit.Test;
import static org.junit.Assert.*;
import org.organigramma.composite.Unit;

public class DuplicateSubUnitTest {

    @Test(expected = RuntimeException.class)
    public void testAddDuplicateSubUnit() {

        Unit mainUnit = new Unit("Dipartimento Tecnico");


        Unit subUnit = new Unit("Supporto Tecnico");


        mainUnit.addSubUnit(subUnit);

        // aggiungo 2 volte la stessa sottoUnità aspettandomi un errore
        mainUnit.addSubUnit(subUnit);
    }
}
