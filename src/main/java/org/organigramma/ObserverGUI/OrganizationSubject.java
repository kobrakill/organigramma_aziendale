
package org.organigramma.ObserverGUI;


import org.organigramma.composite.Unit;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrganizationSubject implements Subject, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private transient List<Observer> observers = new ArrayList<>(); // Rendo transient
    private Unit rootNode;
    private String state;



    public OrganizationSubject() {
        this.rootNode = new Unit("Azienda");
        state = "Stato iniziale"; // Stato di esempio iniziale
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); // Legge tutti i campi non transient
        observers = new ArrayList<>(); // Reinizializza observers a una nuova lista vuota
    }
//per modificare in modo coerente le modifiche ai dipendenti
    public List<Unit> getAllUnits() {
        List<Unit> allUnits = new ArrayList<>();
        collectAllUnits(rootNode, allUnits);
        return allUnits;
    }

    private void collectAllUnits(Unit unit, List<Unit> allUnits) {
        if (unit != null) {
            allUnits.add(unit);
            for (Unit subUnit : unit.getSubUnits()) {
                collectAllUnits(subUnit, allUnits);
            }
        }
    }
    /////////////

    // Metodi per gestire lo stato
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
        notifyObservers();
    }

    public Unit getRoot() {
        return rootNode;
    }

    public void setRoot(Unit root) {
        this.rootNode = root;
        setState("Radice organizzativa modificata"); // Imposta un nuovo stato
    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.updateMessageArea();
        }
    }


}
