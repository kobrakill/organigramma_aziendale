

import org.junit.Test;
import org.organigramma.ObserverGUI.*;
import org.organigramma.composite.*;

import static org.junit.Assert.assertTrue;

public class ObserverPatternTest {

    @Test
    public void testObserverReceivesNotification() {
        OrganizationSubject subject = new OrganizationSubject();
        OrganizationChartGUI observer = new OrganizationChartGUI();

        // Aggiungo l'osservatore
        subject.attach(observer);

        // Cambio il rootNode e notifica gli osservatori
        Unit newRoot = new Unit("Dipartimento Marketing");
        subject.setRoot(newRoot);  //cambia lo stato
        subject.notifyObservers(); //se notifyObservers() genera eccezioni il test non passa
    }
}
