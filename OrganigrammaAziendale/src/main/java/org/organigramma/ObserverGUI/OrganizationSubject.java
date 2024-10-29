
package org.organigramma.ObserverGUI;


import org.organigramma.composite.Unit;


import java.util.ArrayList;
import java.util.List;

public class OrganizationSubject implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private Unit rootNode; // Utilizzo di DefaultMutableTreeNode
    private String state; // Stato dell'organizzazione

    public OrganizationSubject() {
        this.rootNode = new Unit("Azienda");
        state = "Stato iniziale"; // Stato di esempio iniziale
    }

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
