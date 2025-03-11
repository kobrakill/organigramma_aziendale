package org.organigramma.ObserverGUI;

import org.organigramma.composite.Employee;
import org.organigramma.composite.Role;
import org.organigramma.composite.Unit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;
import java.util.List;

public class OrganizationChartGUI extends JFrame implements Observer {
    private OrganizationSubject organizationSubject;
    private JTextArea messageArea;
    private JPanel chartPanel;
    private Unit selectedUnit; // Variabile per tracciare l'unità selezionata
    private JScrollPane chartScrollPane; // Aggiungo uno JScrollPane per lo scorrimento dell'organigramma

    // Pulsanti per le funzionalità
    private JButton addSubUnitButton;
    private JButton addEmployeeButton;
    private JButton addAllowedRoleButton;
    private JButton modifyUnitNameButton;
    private JButton removeUnitButton;
    private JButton showInfoButton;
    private JButton saveOrganizationChartButton;
    private JButton loadOrganizationChartButton;
    private JButton resetOrganizationChartButton;
    private JButton showUsageGuideButton;
    private JButton manageEmployeesButton;

    public OrganizationChartGUI() {
        setTitle("Organigramma Aziendale");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Creo il soggetto che rappresenta la struttura organizzativa come Unità principale
        organizationSubject = new OrganizationSubject();
        Unit companyUnit = new Unit("Azienda");
        organizationSubject.setRoot(companyUnit);
        selectedUnit = companyUnit;
        organizationSubject.attach(this);

        // Creo l'area di messaggi
        messageArea = new JTextArea(10, 30);
        messageArea.setEditable(false);
        JScrollPane messageScrollPane = new JScrollPane(messageArea);

        // Pannello per l'organigramma grafico
        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                chartPanel.removeAll(); // Rimuove tutti i componenti prima di disegnare
                drawOrganizationChart(g, organizationSubject.getRoot(), getWidth() / 2, 50, 100,30);
                chartPanel.revalidate(); // Aggiorna il layout dopo aver ridisegnato ( g, organizationSubject.getRoot(), getWidth() / 2, 50, 100, 30);
            }
        };
        chartPanel.setLayout(null);


        // Aggiungo il pannello all'interno di uno JScrollPane
        chartScrollPane = new JScrollPane(chartPanel);
        chartScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        chartScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Pannello per i pulsanti a sinistra
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        // Pulsanti principali
        modifyUnitNameButton = createButton("Modifica Nome Unità", e -> modifyUnitName());
        addSubUnitButton = createButton("Aggiungi Sotto Unità", e -> addSubUnit());
        removeUnitButton = createButton("Rimuovi Unità", e -> removeUnit());
        addEmployeeButton = createButton("Aggiungi Dipendente ad Unità", e -> addEmployeeToUnit());
        addAllowedRoleButton = createButton("Crea Ruolo Ammissibile", e -> createRole());

        // Aggiungo al pannello i pulsanti
        leftPanel.add(modifyUnitNameButton);
        leftPanel.add(addSubUnitButton);
        leftPanel.add(removeUnitButton);
        leftPanel.add(addEmployeeButton);
        leftPanel.add(addAllowedRoleButton);


        // Pannello per i pulsanti a destra
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        showInfoButton = createButton("Visualizza info Unità", e -> showInfo());
        manageEmployeesButton = createButton("Gestisci Tutti i Dipendenti", e -> showEmployeeManagement());
        saveOrganizationChartButton = createButton("Salva Organigramma", e -> saveOrganizationChart());
        loadOrganizationChartButton = createButton("Carica Organigramma", e -> loadOrganizationChart());
        resetOrganizationChartButton = createButton("Azzera Organigramma", e -> resetOrganizationChart());
        showUsageGuideButton = createButton("Guida all'Utilizzo", e -> showUsageGuide());



        rightPanel.add(showInfoButton);
        rightPanel.add(manageEmployeesButton);
        rightPanel.add(saveOrganizationChartButton);
        rightPanel.add(loadOrganizationChartButton);
        rightPanel.add(resetOrganizationChartButton);
        rightPanel.add(showUsageGuideButton);

        // Aggiungo componenti al frame principale
        add(chartScrollPane, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        add(messageScrollPane, BorderLayout.SOUTH);

        // Visualizza la finestra
        setVisible(true);
    }



    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(actionListener);
        return button;
    }
    private void drawOrganizationChart(Graphics g, Unit unit, int x, int y, int yOffset, int minSpacing) {
        // Disegno l'unità corrente e aggiungo un MouseListener
        UnitRectanglePanel unitRect = new UnitRectanglePanel(unit, x - 50, y, 100, 50);
        unitRect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedUnit = unit;
                organizationSubject.setState("Selezionata unità: " + unit.getName());
            }
        });
        chartPanel.add(unitRect);

        // Ricorsione per disegnare tutte le sotto-unità
        List<Unit> subUnits = unit.getSubUnits();
        if (subUnits.isEmpty()) {
            return; // Se non ci sono sotto-unità, termina il disegno
        }

        // Calcolo la larghezza totale necessaria per il sotto-albero
        int subtreeWidth = 0;
        List<Integer> subUnitWidths = new ArrayList<>();
        for (Unit subUnit : subUnits) {
            int width = calculateSubtreeWidth(subUnit, minSpacing);
            subUnitWidths.add(width);  // Memorizzo la larghezza calcolata per ogni sotto-unità
            subtreeWidth += width + minSpacing;
        }
        subtreeWidth -= minSpacing; // Rimuove lo spazio aggiuntivo alla fine

        // Posiziono il primo nodo figlio al centro del sotto-albero
        int nextX = x - subtreeWidth / 2;

        for (int i = 0; i < subUnits.size(); i++) {
            Unit subUnit = subUnits.get(i);
            int subUnitWidth = subUnitWidths.get(i);

            int startX = x;
            int startY = y + 50; // Centro della base del rettangolo corrente

            int endX = nextX + subUnitWidth / 2;
            int endY = y + yOffset; // Posizione y del nodo figlio

            // Disegno la linea tra le unità
            g.drawLine(startX, startY, endX, endY);

            // Chiamata ricorsiva per disegnare il sotto-albero
            drawOrganizationChart(g, subUnit, endX, endY, yOffset, minSpacing);

            // Sposto la posizione orizzontale per il prossimo sotto-albero, includendo lo spazio minimo tra le unità
            nextX += subUnitWidth + minSpacing;
        }

        // Calcolo le dimensioni massime raggiunte per aggiornare il pannello se necessario
        int maxWidth = Math.max(chartPanel.getPreferredSize().width, nextX + minSpacing);
        int maxHeight = Math.max(chartPanel.getPreferredSize().height, y + yOffset + minSpacing*2);

        // Aggiorno la dimensione del pannello per abilitare le scrollbar se necessarie
        chartPanel.setPreferredSize(new Dimension(maxWidth, maxHeight));
        chartPanel.revalidate(); // Forza l'aggiornamento del pannello per abilitare lo scorrimento
    }

    // Metodo di supporto per calcolare la larghezza totale del sotto-albero
    private int calculateSubtreeWidth(Unit unit, int minSpacing) {
        List<Unit> subUnits = unit.getSubUnits();
        if (subUnits.isEmpty()) {
            return 100; // Larghezza minima per un singolo nodo
        }

        int width = 0;
        for (Unit subUnit : subUnits) {
            width += calculateSubtreeWidth(subUnit, minSpacing) + minSpacing;
        }
        width -= minSpacing; // Rimuove l'offset aggiuntivo alla fine

        return width;
    }


    public void updateMessageArea() {
        String newMessage = organizationSubject.getState();
        String currentText = messageArea.getText();
        if (!currentText.endsWith(newMessage + "\n")) {
            messageArea.append(newMessage + "\n");

            // Imposto la posizione della barra di scorrimento in fondo
            messageArea.setCaretPosition(messageArea.getDocument().getLength());
        }
    }


    private void modifyUnitName() {
        if (selectedUnit != null) {
            String newName = JOptionPane.showInputDialog(this,"Inserisci il nuovo nome dell'unità:");
            if (newName != null && !newName.trim().isEmpty()) {
                // Verifico se il nuovo nome è già in uso nell'intero albero
                if (isUnitNameDuplicate(organizationSubject.getRoot(), newName)) {
                    JOptionPane.showMessageDialog(this,
                            "Esiste già un'unità con questo nome in tutto l'organigramma. Scegli un nome diverso.",
                            "Errore: Unità duplicata",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }


                Unit oldUnitReference = selectedUnit;

// Modifica il nome dell'unità (aggiorno direttamente l'oggetto esistente)
                selectedUnit.setName(newName);

// Aggiorno i riferimenti dei dipendenti
                List<Employee> employees = oldUnitReference.getEmployees();

                for (Employee employee : employees) {
                    HashMap<String, Role> rolebyUnits = employee.getRolebyUnits();

                    // Mappa temporanea per aggiornare il riferimento dell'unità senza interferire con l'iterazione
                    Map<String, Role> updatedRolebyUnits = new HashMap<>(rolebyUnits);

                    for (Map.Entry<String, Role> entry : rolebyUnits.entrySet()) {

                        Role role = entry.getValue();

                        if (selectedUnit == oldUnitReference) { // Controllo per riferimento esatto
                            updatedRolebyUnits.remove(selectedUnit.getName());
                            updatedRolebyUnits.put(selectedUnit.getName(), role);
                        }
                    }

                    // Aggiorno la mappa originale solo dopo il ciclo per evitare ConcurrentModificationException
                    employee.getRolebyUnits().clear();
                    employee.getRolebyUnits().putAll(updatedRolebyUnits);
                }

                organizationSubject.setState("Modificata unità: " + newName);
                chartPanel.repaint(); // Aggiorna la grafica
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un'unità per modificarne il nome.");
        }
    }


    private void addSubUnit() {
        if (selectedUnit != null) {
            String unitName = JOptionPane.showInputDialog(this,"Inserisci il nome della sotto unità:");
            if (unitName != null && !unitName.trim().isEmpty()) {
                // Verifico se il nome è già in uso in tutto l'albero
                if (isUnitNameDuplicate(organizationSubject.getRoot(), unitName)) {
                    JOptionPane.showMessageDialog(this,
                            "Esiste già un'unità con questo nome in tutto l'organigramma. Scegli un nome diverso.",
                            "Errore: Unità duplicata",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Unit newUnit = new Unit(unitName);
                selectedUnit.addSubUnit(newUnit);
                organizationSubject.setState("Aggiunta sotto unità: " + unitName);
                chartPanel.repaint();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un'unità per aggiungere una sotto unità.");
        }
    }
    private boolean isUnitNameDuplicate(Unit root, String name) {
        // Controllo se il nome dell'unità corrente coincide con quello che vogliamo aggiungere
        if (root.getName().equalsIgnoreCase(name)) {
            return true;
        }
        // Ricorsione sulle sotto-unità per verificare se il nome è già utilizzato
        for (Unit subUnit : root.getSubUnits()) {
            if (isUnitNameDuplicate(subUnit, name)) {
                return true;
            }
        }
        return false;
    }


    private void removeUnit() {
        if (selectedUnit != null && selectedUnit != organizationSubject.getRoot()) {
            Unit parentUnit = findParentUnit(organizationSubject.getRoot(), selectedUnit);
            if (parentUnit != null) {
                parentUnit.removeSubUnit(selectedUnit);
                organizationSubject.setState("Rimossa unità: " + selectedUnit.getName());
                selectedUnit = null; // Deseleziona l'unità dopo la rimozione
                chartPanel.repaint(); // Aggiorna la grafica
            }
        } else {
            JOptionPane.showMessageDialog(this, "Non è possibile rimuovere l'unità principale.");
        }
    }

    private Unit findParentUnit(Unit current, Unit target) {
        for (Unit subUnit : current.getSubUnits()) {
            if (subUnit == target) {
                return current;
            }
            Unit parent = findParentUnit(subUnit, target);
            if (parent != null) {
                return parent;
            }
        }
        return null;
    }

    private void addEmployeeToUnit() {
        if (selectedUnit != null) {
            // Verifico che l'unità abbia ruoli ammissibili
            List<Role> allowedRoles = selectedUnit.getAllowedRoles();
            if (allowedRoles.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Non è possibile aggiungere dipendenti a questa unità senza ruoli ammissibili.",
                        "Errore: Nessun ruolo ammissibile",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean isValid = false;
            JTextField nameField = new JTextField();
            JTextField lastNameField = new JTextField();
            JTextField cityField = new JTextField();
            JTextField addressField = new JTextField();
            JTextField ageField = new JTextField();

            // Continua a mostrare il pannello finché i dati non sono validi
            while (!isValid) {
                JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
                panel.add(new JLabel("Nome:"));
                panel.add(nameField);
                panel.add(new JLabel("Cognome:"));
                panel.add(lastNameField);
                panel.add(new JLabel("Città:"));
                panel.add(cityField);
                panel.add(new JLabel("Indirizzo:"));
                panel.add(addressField);
                panel.add(new JLabel("Età:"));
                panel.add(ageField);

                int result = JOptionPane.showConfirmDialog(this, panel, "Aggiungi Dipendente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result != JOptionPane.OK_OPTION) {
                    return;
                }

                String employeeName = nameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String city = cityField.getText().trim();
                String address = addressField.getText().trim();
                String ageText = ageField.getText().trim();

                // Controllo che tutti i campi siano compilati
                if (employeeName.isEmpty() || lastName.isEmpty() || city.isEmpty() || address.isEmpty() || ageText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Tutti i campi devono essere compilati.", "Errore", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                int age;
                try {
                    age = Integer.parseInt(ageText);
                    if (age <= 0) {
                        throw new NumberFormatException();
                    }
                    if (age < 18) {
                        JOptionPane.showMessageDialog(this, "Il dipendente deve essere maggiorenne (almeno 18 anni).", "Errore", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "L'età deve essere un numero valido e maggiore di 0.", "Errore", JOptionPane.ERROR_MESSAGE);
                    continue;
                }



                Employee newEmployee = new Employee(employeeName, lastName, city, address, age);

                // Verifico se il dipendente esiste già
                if (selectedUnit.getEmployees().contains(newEmployee)) {
                    JOptionPane.showMessageDialog(this, "Dipendente già esistente: " + employeeName + " " + lastName,
                            "Errore di Duplicato", JOptionPane.WARNING_MESSAGE);
                    return;
                }


                Role assignedRole = selectRoleForEmployee(allowedRoles);
                if (assignedRole == null) {
                    JOptionPane.showMessageDialog(this,
                            "Il dipendente deve avere un ruolo ammissibile per questa unità.",
                            "Errore: Ruolo non assegnato",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Se i dati sono validi, aggiungo il dipendente ed esco dal ciclo
                isValid = true;
                newEmployee.addRole(selectedUnit, assignedRole);
                selectedUnit.addEmployee(newEmployee);
                organizationSubject.setState("Aggiunto dipendente: " + employeeName + " "+ lastName+ " con ruolo " + assignedRole.getName());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un'unità per aggiungere un dipendente.");
        }
    }

    private Role selectRoleForEmployee(List<Role> allowedRoles) {
        String[] roleNames = allowedRoles.stream().map(Role::getName).toArray(String[]::new);
        String selectedRoleName = (String) JOptionPane.showInputDialog(
                this,
                "Seleziona un ruolo per il dipendente:",
                "Assegna Ruolo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                roleNames,
                roleNames[0]
        );

        return allowedRoles.stream().filter(role -> role.getName().equals(selectedRoleName)).findFirst().orElse(null);
    }




    private void createRole() {
        if (selectedUnit != null) {
            boolean isValid = false;

            JTextField roleNameField = new JTextField();
            JTextField descriptionField = new JTextField();
            JTextField requirementsField = new JTextField();
            JTextField salaryField = new JTextField();

            // Continuo a mostrare il pannello finché i dati non sono validi
            while (!isValid) {
                JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
                panel.add(new JLabel("Nome Ruolo:"));
                panel.add(roleNameField);
                panel.add(new JLabel("Descrizione:"));
                panel.add(descriptionField);
                panel.add(new JLabel("Requisiti:"));
                panel.add(requirementsField);
                panel.add(new JLabel("Salario:"));
                panel.add(salaryField);

                int result = JOptionPane.showConfirmDialog(this, panel, "Crea Ruolo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result != JOptionPane.OK_OPTION) {
                    return;
                }

                String roleName = roleNameField.getText().trim();
                String description = descriptionField.getText().trim();
                String requirements = requirementsField.getText().trim();
                String salaryText = salaryField.getText().trim();

                // Verifico che tutti i campi siano compilati e che lo stipendio sia un numero valido
                if (roleName.isEmpty() || description.isEmpty() || requirements.isEmpty() || salaryText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Tutti i campi devono essere compilati.", "Errore", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                double salary;
                try {
                    salary = Double.parseDouble(salaryText);
                    if (salary <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Il salario deve essere un numero valido e maggiore di 0.", "Errore", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                Role newRole = new Role(roleName, description, requirements, salary);

                // Verifico se il ruolo esiste già
                if (selectedUnit.getAllowedRoles().contains(newRole)) {
                    JOptionPane.showMessageDialog(this, "Ruolo già esistente: " + roleName,
                            "Errore di Duplicato", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Se i dati sono validi, si esce dal ciclo e si aggiunge il ruolo
                isValid = true;
                selectedUnit.addAllowedRole(newRole);
                organizationSubject.setState("Creato nuovo ruolo ammissibile: " + roleName);

            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un'unità per creare un ruolo.");
        }
    }




    private void showInfo() {
        if (selectedUnit != null) {
            // Crea un nuovo dialogo per mostrare le informazioni dell'unità
            JDialog infoDialog = new JDialog(this, "Informazioni Unità " + selectedUnit.getName(), true);
            infoDialog.setLayout(new BorderLayout());

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

            // Pannello per la sezione dipendenti
            JPanel employeesSectionPanel = new JPanel();
            employeesSectionPanel.setLayout(new BorderLayout());
            employeesSectionPanel.add(new JLabel("Dipendenti:"), BorderLayout.NORTH);

            JPanel employeesPanel = new JPanel();
            employeesPanel.setLayout(new BoxLayout(employeesPanel, BoxLayout.Y_AXIS));

            // Mappa per tracciare i pannelli dei dipendenti
            Map<Employee, JPanel> employeePanelsMap = new HashMap<>();

            // Aggiungo i dipendenti al pannello employeesPanel
            for (Employee emp : selectedUnit.getEmployees()) {
                JPanel employeePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel employeeLabel = new JLabel(emp.getName() +" "+ emp.getLastName() + " - Ruolo: " + emp.getRole(selectedUnit).getName());
                employeePanel.add(employeeLabel);

                // Bottone per mostrare informazioni del dipendente
                JButton modifyButton = new JButton("Mostra/Modifica info personali");
                modifyButton.addActionListener(e -> {
                    modifyEmployee(emp);
                    employeeLabel.setText(emp.getName() +" "+ emp.getLastName() + " - Ruolo: " + emp.getRole(selectedUnit).getName());
                });
                employeePanel.add(modifyButton);

                // Bottone per modificare il ruolo del dipendente
                JButton modifyRoleButton = new JButton("Modifica Ruolo");
                modifyRoleButton.addActionListener(e -> {
                    modifyRoleEmployee(emp);
                    employeeLabel.setText(emp.getName() +" "+ emp.getLastName() + " - Ruolo: " + emp.getRole(selectedUnit).getName());
                });
                employeePanel.add(modifyRoleButton);

                // Bottone per eliminare il dipendente
                JButton deleteButton = new JButton("Elimina");
                deleteButton.addActionListener(e -> {
                    removeEmployee(emp); // Rimuove il dipendente dalla logica
                    employeesPanel.remove(employeePanel); // Rimuove solo il pannello del dipendente dall'interfaccia
                    employeesPanel.revalidate();
                    employeesPanel.repaint();
                });
                employeePanel.add(deleteButton);

                employeesPanel.add(employeePanel);
                employeePanelsMap.put(emp, employeePanel); // Salva il pannello nella mappa
            }

            JScrollPane employeesScrollPane = new JScrollPane(employeesPanel);
            employeesScrollPane.setPreferredSize(new Dimension(500, 200));
            employeesSectionPanel.add(employeesScrollPane, BorderLayout.CENTER);

            // Pannello per la sezione dei ruoli
            JPanel rolesSectionPanel = new JPanel();
            rolesSectionPanel.setLayout(new BorderLayout());
            rolesSectionPanel.add(new JLabel("Ruoli Ammissibili:"), BorderLayout.NORTH);

            JPanel rolesPanel = new JPanel();
            rolesPanel.setLayout(new BoxLayout(rolesPanel, BoxLayout.Y_AXIS));

            // Aggiungo i ruoli al pannello rolesPanel
            for (Role role : selectedUnit.getAllowedRoles()) {
                JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel roleLabel = new JLabel(role.getName());
                rolePanel.add(roleLabel);

                // Bottone per mostrare informazioni del ruolo
                JButton showDetailsButton = new JButton("Mostra Dettagli");
                showDetailsButton.addActionListener(e -> showRoleDetails(role));
                rolePanel.add(showDetailsButton);

                // Bottone per modificare il ruolo
                JButton modifyButton = new JButton("Modifica");
                modifyButton.addActionListener(e -> {
                    modifyRole(role, employeesPanel);
                    roleLabel.setText(role.getName());
                });
                rolePanel.add(modifyButton);

                // Bottone per eliminare il ruolo e tutti i dipendenti con quel ruolo
                JButton deleteButton = new JButton("Elimina");
                deleteButton.addActionListener(e -> {
                    removeRole(role); // Rimuove il ruolo dalla logica
                    rolesPanel.remove(rolePanel); // Rimuove il pannello del ruolo dall'interfaccia

                    // Rimuove tutti i dipendenti con quel ruolo dall'unità
                    for (Employee emp : new ArrayList<>(selectedUnit.getEmployees())) { // Usa una copia per evitare problemi di modifica concorrente
                        if (emp.getRole(selectedUnit).equals(role)) {
                            removeEmployee(emp); // Rimuove il dipendente dalla logica
                            JPanel empPanelToRemove = employeePanelsMap.get(emp); // Trova il pannello specifico
                            if (empPanelToRemove != null) {
                                employeesPanel.remove(empPanelToRemove); // Rimuove solo il pannello del dipendente interessato
                            }
                        }
                    }
                    infoDialog.dispose();
                    showInfo();
                });
                rolePanel.add(deleteButton);

                rolesPanel.add(rolePanel);
            }

            JScrollPane rolesScrollPane = new JScrollPane(rolesPanel);
            rolesScrollPane.setPreferredSize(new Dimension(500, 200));
            rolesSectionPanel.add(rolesScrollPane, BorderLayout.CENTER);

            // Aggiungo le sezioni dipendenti e ruoli al pannello principale
            infoPanel.add(employeesSectionPanel);
            infoPanel.add(Box.createVerticalStrut(10));
            infoPanel.add(rolesSectionPanel);

            infoDialog.add(infoPanel, BorderLayout.CENTER);
            infoDialog.setSize(600, 500);
            infoDialog.setLocationRelativeTo(this);
            infoDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un'unità per visualizzare le informazioni.");
        }
    }


    //metodi per i dipendenti

    // Metodo per modificare il ruolo del dipendente
    private void modifyRoleEmployee(Employee emp) {
        // Crea un dialogo per la selezione del ruolo
        JDialog roleDialog = new JDialog(this, "Seleziona Nuovo Ruolo", true);
        roleDialog.setLayout(new BorderLayout());

        // ComboBox con i ruoli disponibili
        JComboBox<Role> roleComboBox = new JComboBox<>();
        for (Role role : selectedUnit.getAllowedRoles()) {
            roleComboBox.addItem(role);
        }

        // Imposto un renderer per mostrare solo il nome del ruolo
        roleComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Role) {
                    setText(((Role) value).getName()); // Mostra solo il nome del ruolo
                }
                return this;
            }
        });


        // Pulsante per confermare la selezione del ruolo
        JButton confirmButton = new JButton("Conferma");
        confirmButton.addActionListener(e -> {
            Role selectedRole = (Role) roleComboBox.getSelectedItem();
            if (selectedRole != null) {
                emp.addRole(selectedUnit, selectedRole);
                roleDialog.dispose(); // Chiudi il dialogo
            }
        });

        // Aggiungo elementi al dialogo
        roleDialog.add(new JLabel("Seleziona un nuovo ruolo per " + emp.getName()), BorderLayout.NORTH);
        roleDialog.add(roleComboBox, BorderLayout.CENTER);
        roleDialog.add(confirmButton, BorderLayout.SOUTH);

        roleDialog.pack();
        roleDialog.setLocationRelativeTo(this);
        roleDialog.setVisible(true); // Mostra il dialogo per la selezione del ruolo
    }



    private void modifyEmployee(Employee emp) {
        boolean validInput = false;
        while (!validInput) {
            JTextField nameField = new JTextField(emp.getName());
            JTextField lastNameField = new JTextField(emp.getLastName());
            JTextField cityField = new JTextField(emp.getCity());
            JTextField addressField = new JTextField(emp.getAddress());
            JTextField ageField = new JTextField(String.valueOf(emp.getAge()));

            while (!validInput) {
                JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
                panel.add(new JLabel("Nome:"));
                panel.add(nameField);
                panel.add(new JLabel("Cognome:"));
                panel.add(lastNameField);
                panel.add(new JLabel("Città:"));
                panel.add(cityField);
                panel.add(new JLabel("Indirizzo:"));
                panel.add(addressField);
                panel.add(new JLabel("Età:"));
                panel.add(ageField);

                int result = JOptionPane.showConfirmDialog(this, panel, "Modifica Dipendente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result != JOptionPane.OK_OPTION) {
                    return; // Esce se l'utente annulla
                }

                try {
                    // Salva i nuovi valori
                    String newName = nameField.getText().trim();
                    String newLastName = lastNameField.getText().trim();
                    String newCity = cityField.getText().trim();
                    String newAddress = addressField.getText().trim();
                    String ageText = ageField.getText().trim();

                    // Controllo che nessun campo sia vuoto
                    if (newName.isEmpty() || newLastName.isEmpty() || newCity.isEmpty() || newAddress.isEmpty() || ageText.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Tutti i campi devono essere compilati.", "Errore", JOptionPane.ERROR_MESSAGE);
                        continue; // Riapre il form di modifica con gli stessi valori
                    }

                    int newAge = Integer.parseInt(ageText);

                    // Controllo validità dell'età
                    if (newAge <= 0) {
                        JOptionPane.showMessageDialog(this, "L'età deve essere un numero valido e maggiore di 0.", "Errore", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                    if (newAge < 18) {
                        JOptionPane.showMessageDialog(this, "Il dipendente deve essere maggiorenne (almeno 18 anni).", "Errore", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    // Aggiorna in tutte le unità associate a questo dipendente
                    for (Unit unit : organizationSubject.getAllUnits()) {
                        if (unit.getEmployees().contains(emp)) {
                            unit.updateEmployee(emp, new Employee(newName, newLastName, newCity, newAddress, newAge));
                        }
                    }

                    // Aggiorna direttamente l'oggetto emp per riflettere le modifiche globali
                    emp.setName(newName);
                    emp.setLastName(newLastName);
                    emp.setCity(newCity);
                    emp.setAddress(newAddress);
                    emp.setAge(newAge);

                    organizationSubject.setState("Modificato dipendente: " + emp.getName() + " " + emp.getLastName());
                    chartPanel.repaint(); // Aggiorna la grafica
                    validInput = true; // Esce dal loop solo dopo una modifica valida
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Errore: L'età deve essere un numero valido.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }


    private void removeEmployee(Employee emp) {
        int confirmation = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler eliminare il dipendente " + emp.getName() + "?", "Conferma Eliminazione", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            selectedUnit.removeEmployee(emp);
            organizationSubject.setState("Rimosso dipendente: " + emp.getName());
            chartPanel.repaint(); // Aggiorna la grafica
        }
    }
//Metodi per i ruoli
private void showRoleDetails(Role role) {
    StringBuilder details = new StringBuilder();
    details.append("Nome Ruolo: ").append(role.getName()).append("\n");
    details.append("Descrizione: ").append(role.getDescription()).append("\n");
    details.append("Requisiti: ").append(role.getRequirements()).append("\n");
    details.append("Salario: ").append(role.getSalary()).append("\n");

    JOptionPane.showMessageDialog(this, details.toString(), "Dettagli Ruolo", JOptionPane.INFORMATION_MESSAGE);
}

    private void modifyRole(Role role, JPanel employeesPanel) {
        JTextField roleNameField = new JTextField(role.getName());
        JTextField descriptionField = new JTextField(role.getDescription());
        JTextField requirementsField = new JTextField(role.getRequirements());
        JTextField salaryField = new JTextField(String.valueOf(role.getSalary()));

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Nome Ruolo:"));
        panel.add(roleNameField);
        panel.add(new JLabel("Descrizione:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Requisiti:"));
        panel.add(requirementsField);
        panel.add(new JLabel("Salario:"));
        panel.add(salaryField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Modifica Ruolo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Aggiorno i dettagli del ruolo
                role.setName(roleNameField.getText().trim());
                role.setDescription(descriptionField.getText().trim());
                role.setRequirements(requirementsField.getText().trim());
                role.setSalary(Double.parseDouble(salaryField.getText().trim()));

                organizationSubject.setState("Modificato ruolo: " + role.getName());
                chartPanel.repaint();

                // Aggiorno graficamente tutti i dipendenti che hanno questo ruolo
                for (Component component : employeesPanel.getComponents()) {
                    if (component instanceof JPanel) {
                        JPanel employeePanel = (JPanel) component;
                        for (Component subComponent : employeePanel.getComponents()) {
                            if (subComponent instanceof JLabel) {
                                JLabel employeeLabel = (JLabel) subComponent;
                                String employeeName = employeeLabel.getText().split(" - ")[0].trim();
                                Employee emp = selectedUnit.findEmployeeByName(employeeName);

                                if (emp != null && emp.getRole(selectedUnit).equals(role)) {
                                    employeeLabel.setText(emp.getName() + " - Ruolo: " + role.getName());
                                }
                            }
                        }
                    }
                }

                employeesPanel.revalidate();
                employeesPanel.repaint();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Errore: Il salario deve essere un numero valido.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void removeRole(Role role) {
        int confirmation = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler eliminare il ruolo " + role.getName() + "?", "Conferma Eliminazione", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            selectedUnit.removeAllowedRole(role);
            organizationSubject.setState("Rimosso ruolo: " + role.getName());
            chartPanel.repaint();
        }
    }
    private void showEmployeeManagement() {
        JDialog employeeDialog = new JDialog(this, "Gestione Dipendenti", true);
        employeeDialog.setLayout(new BorderLayout());

        JPanel employeePanel = new JPanel();
        employeePanel.setLayout(new BoxLayout(employeePanel, BoxLayout.Y_AXIS));

        JPanel employeesSectionPanel = new JPanel(new BorderLayout());
        employeesSectionPanel.add(new JLabel("Dipendenti presenti nell'organigramma:"), BorderLayout.NORTH);

        JPanel employeesListPanel = new JPanel();
        employeesListPanel.setLayout(new BoxLayout(employeesListPanel, BoxLayout.Y_AXIS));

        // Mappa per memorizzare i dipendenti con le unità e i rispettivi ruoli
        Map<Employee, Map<String, Role>> employeeRolesMap = new HashMap<>();

        for (Unit unit : organizationSubject.getAllUnits()) {
            for (Employee emp : unit.getEmployees()) {
                // Se il dipendente non è ancora presente nella mappa, lo aggiungo con una nuova mappa interna
                employeeRolesMap.putIfAbsent(emp, new HashMap<>());
                // Aggiungo l'unità e il ruolo occupato dal dipendente
                employeeRolesMap.get(emp).put(unit.getName(), emp.getRole(unit));
            }
        }

        // Creiamo una lista degli impiegati e la ordiniamo alfabeticamente per nome e cognome
        List<Employee> sortedEmployees = new ArrayList<>(employeeRolesMap.keySet());
        sortedEmployees.sort(Comparator.comparing(Employee::getName).thenComparing(Employee::getLastName));

        // Costruisco l'interfaccia utente basandomi sulla lista ordinata
        for (Employee emp : sortedEmployees) {
            Map<String, Role> unitRoles = employeeRolesMap.get(emp);

            JPanel employeeInfoPanel = new JPanel();
            employeeInfoPanel.setLayout(new BorderLayout());

            // Intestazione con il nome del dipendente
            JLabel employeeLabel = new JLabel(emp.getName() + " " + emp.getLastName());
            employeeLabel.setFont(new Font("Arial", Font.BOLD, 14));
            employeeInfoPanel.add(employeeLabel, BorderLayout.NORTH);

            // Pannello per visualizzare le unità e i ruoli del dipendente
            JPanel unitInfoPanel = new JPanel();
            unitInfoPanel.setLayout(new BoxLayout(unitInfoPanel, BoxLayout.Y_AXIS));

            for (Map.Entry<String, Role> unitEntry : unitRoles.entrySet()) {
                String unitName = unitEntry.getKey();
                Role role = unitEntry.getValue();

                JLabel unitLabel = new JLabel("🔹 Unità: " + unitName + " - Ruolo: " + role.getName());
                unitInfoPanel.add(unitLabel);
            }

            // Scroll panel per unità nel caso di molti elementi
            JScrollPane unitScrollPane = new JScrollPane(unitInfoPanel);
            unitScrollPane.setPreferredSize(new Dimension(400, 60));

            employeeInfoPanel.add(unitScrollPane, BorderLayout.CENTER);

            // Pannello per i bottoni di gestione
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            JButton modifyButton = createButton("Modifica Dati", e -> {
                modifyEmployee(emp);
                employeeLabel.setText(emp.getName() + " " + emp.getLastName());
            });

            JButton deleteButton = createButton("Licenzia", e -> {
                int confirm = JOptionPane.showConfirmDialog(employeeDialog, "Sei sicuro di voler licenziare " + emp.getName() + " " + emp.getLastName() + "?", "Conferma licenziamento", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Rimuove il dipendente da tutte le unità
                    for (Unit unit : organizationSubject.getAllUnits()) {
                        unit.removeEmployee(emp);
                    }
                    employeesListPanel.remove(employeeInfoPanel);
                    employeesListPanel.revalidate();
                    employeesListPanel.repaint();
                    organizationSubject.setState("Licenziato Dipendente: " + emp.getName()+ " " + emp.getLastName());
                }
            });

            buttonPanel.add(modifyButton);
            buttonPanel.add(deleteButton);
            employeeInfoPanel.add(buttonPanel, BorderLayout.SOUTH);

            employeesListPanel.add(employeeInfoPanel);
        }

        JScrollPane employeesScrollPane = new JScrollPane(employeesListPanel);
        employeesScrollPane.setPreferredSize(new Dimension(600, 400));
        employeesSectionPanel.add(employeesScrollPane, BorderLayout.CENTER);

        employeePanel.add(employeesSectionPanel);
        employeeDialog.add(employeePanel, BorderLayout.CENTER);
        employeeDialog.setSize(700, 500);
        employeeDialog.setLocationRelativeTo(this);
        employeeDialog.setVisible(true);
    }
    private void saveOrganizationChart() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileChooser.getSelectedFile()))) {
                out.writeObject(organizationSubject); // Salva l'intero oggetto
                organizationSubject.setState("Organigramma salvato con successo.");
                JOptionPane.showMessageDialog(this, "Organigramma salvato correttamente.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Errore durante il salvataggio dell'organigramma: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadOrganizationChart() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileChooser.getSelectedFile()))) {
                organizationSubject = (OrganizationSubject) in.readObject(); // Carica l'intero oggetto
                organizationSubject.attach(this);
                selectedUnit = organizationSubject.getRoot(); // Imposta la radice come unità selezionata
                organizationSubject.setState("Organigramma caricato con successo.");
                JOptionPane.showMessageDialog(this, "Organigramma caricato correttamente.");

                // Aggiorna la grafica per mostrare l'organigramma caricato
                chartPanel.removeAll();
                chartPanel.repaint();
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Errore durante il caricamento dell'organigramma: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void resetOrganizationChart() {
        Unit newRootUnit = new Unit("Azienda");
        organizationSubject.setRoot(newRootUnit);
        selectedUnit = newRootUnit; // Imposto la nuova unità principale come selezionata
        organizationSubject.setState("Organigramma azzerato.");
        JOptionPane.showMessageDialog(this, "Organigramma azzerato.");

        // Ripristino le dimensioni del chartPanel alle originali e rimuovo tutti i componenti
        chartPanel.setPreferredSize(null); // Rimuovo dimensioni personalizzate (per resettare le scrollbar dell'organigramma)
        chartPanel.removeAll();
        chartPanel.revalidate();
        chartPanel.repaint();

        // Reimposta le dimensioni della finestra principale per tornare alle dimensioni iniziali
        setPreferredSize(new Dimension(1200, 700));
        pack(); // Adatta la finestra alle dimensioni 
    }



    private void showUsageGuide() {
        organizationSubject.setState("Visualizzata guida all'utilizzo.");
        JOptionPane.showMessageDialog(this, "Guida all'utilizzo del software è nel file README.TXT associato all'applicazione .");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OrganizationChartGUI::new);
    }
}
