package org.organigramma;

import org.organigramma.composite.Employee;
import org.organigramma.composite.Role;
import org.organigramma.composite.Unit;
import org.organigramma.composite.UnitComponent;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Creazione di alcuni ruoli
        Role manager = new Role("Manager", "Gestisce il team", "Esperienza di leadership", 5000);
        Role developer = new Role("Developer", "Sviluppa software", "Conoscenza di Java", 3000);
        Role tester = new Role("Tester", "Testa il software", "Attenzione ai dettagli", 2500);

        // Creazione di unità organizzative
        Unit headquarters = new Unit("Headquarters");
        Unit developmentTeam = new Unit("Development Team");
        Unit qaTeam = new Unit("QA Team");

        // Definizione dei ruoli permessi per le unità
        headquarters.getAllowedRoles().add(manager);
        developmentTeam.getAllowedRoles().add(developer);
        developmentTeam.getAllowedRoles().add(manager);
        qaTeam.getAllowedRoles().add(tester);

        // Creazione di dipendenti
        Employee alice = new Employee("Alice", "Rossi", "Milano", "Via Roma 10", 30);
        Employee bob = new Employee("Bob", "Bianchi", "Roma", "Piazza Venezia 5", 25);
        Employee charlie = new Employee("Charlie", "Verdi", "Napoli", "Corso Umberto 2", 28);

        // Assegnazione dei ruoli ai dipendenti
        alice.addRole(headquarters, manager);
        bob.addRole(developmentTeam, developer);
        charlie.addRole(qaTeam, tester);
        charlie.addRole(developmentTeam,manager);
        developmentTeam.removeAllowedRole(manager);

        // Aggiunta dei dipendenti alle unità
        headquarters.addEmployee(alice);
        developmentTeam.addEmployee(bob);
        developmentTeam.addEmployee(charlie);
        qaTeam.addEmployee(charlie);


        // Stampa della struttura organizzativa
        headquarters.addSubUnit(developmentTeam);
      //  headquarters.removeSubUnit(developmentTeam);
        headquarters.addSubUnit(qaTeam);
        headquarters.printStructure();



    }

  }