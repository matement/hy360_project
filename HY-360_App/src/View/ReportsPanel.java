package View;

import Controller.controller; 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class ReportsPanel extends JPanel {

    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> reportSelector;
    private JTextField paramField;
    private JLabel paramLabel;
    private JTextArea sqlArea;
    
    private controller controller;


    public ReportsPanel(controller controller) {
        this.controller = controller; 

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Έτοιμες Αναφορές", createPredefinedTab());
        tabbedPane.addTab("SQL Console (Advanced)", createSqlTab());
        
        add(tabbedPane, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        resultTable = new JTable(tableModel);
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
        
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Αποτελέσματα"));
        
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createPredefinedTab() {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        String[] reports = {
            "1. Κατάσταση μισθοδοσίας ανά κατηγορία προσωπικού",
            "2. Μέγιστος, Ελάχιστος και Μέσος μισθός ανά κατηγορία",
            "3. Μέση αύξηση μισθών ανά χρονική περίοδο",
            "4. Στοιχεία και μισθοδοσία συγκεκριμένου υπαλλήλου", 
            "5. Συνολικό ύψος μισθοδοσίας ανά κατηγορία"
        };

        reportSelector = new JComboBox<>(reports);
        
        JPanel paramPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        paramLabel = new JLabel("Παράμετρος (π.χ. Όνομα):");
        paramField = new JTextField(15);
        paramField.setEnabled(false); 
        paramPanel.add(paramLabel);
        paramPanel.add(paramField);

        JButton runBtn = new JButton("Εκτέλεση Αναφοράς");

        reportSelector.addActionListener(e -> {
            int idx = reportSelector.getSelectedIndex();
            if (idx == 3) { 
                paramField.setEnabled(true);
                paramLabel.setText("Όνομα Υπαλλήλου:");
                paramField.requestFocus();
            } else if (idx == 2) {
                paramField.setEnabled(true);
                paramLabel.setText("Έτος (π.χ. 2025):");
            } else {
                paramField.setEnabled(false);
                paramLabel.setText("Δεν απαιτείται παράμετρος");
                paramField.setText("");
            }
        });

        runBtn.addActionListener(e -> executePredefinedQuery());

        JPanel topBox = new JPanel(new GridLayout(2,1,5,5));
        topBox.add(reportSelector);
        topBox.add(paramPanel);

        panel.add(topBox, BorderLayout.CENTER);
        panel.add(runBtn, BorderLayout.EAST);

        return panel;
    }

    private void executePredefinedQuery() {
        int selectedIndex = reportSelector.getSelectedIndex();
        String param = paramField.getText();

        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        switch (selectedIndex) {
            case 0: 
                setColumns("Κατηγορία", "Σύνολο Υπαλλήλων", "Πληρωμένοι", "Εκκρεμείς");
                tableModel.addRow(new Object[]{"Μόνιμοι Διοικητικοί", 15, 15, 0});
                tableModel.addRow(new Object[]{"Μόνιμοι Διδακτικοί", 40, 38, 2});
                tableModel.addRow(new Object[]{"Συμβασιούχοι", 10, 10, 0});
                break;

            case 1: 

                setColumns("Κατηγορία", "Μέγιστος (€)", "Ελάχιστος (€)", "Μέσος (€)");
                tableModel.addRow(new Object[]{"Διοικητικοί", "1.800", "900", "1.250"});
                tableModel.addRow(new Object[]{"Διδακτικοί", "2.500", "1.300", "1.900"});
                break;

            case 2: 
                setColumns("Έτος", "Μέση Αύξηση (%)", "Σύνολο Αυξήσεων (€)");
                tableModel.addRow(new Object[]{"2023", "2.5%", "15.000€"});
                tableModel.addRow(new Object[]{"2024", "4.1%", "28.000€"});
                tableModel.addRow(new Object[]{"2025", "1.8%", "12.500€"});
                break;

            case 3: 
                if (param.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Παρακαλώ δώστε όνομα υπαλλήλου!");
                    return;
                }
                setColumns("Ημερομηνία", "Όνομα", "Τύπος Πληρωμής", "Ποσό");
                tableModel.addRow(new Object[]{"2025-01-31", param, "Μισθοδοσία", "1.200€"});
                tableModel.addRow(new Object[]{"2024-12-31", param, "Μισθοδοσία", "1.200€"});
                tableModel.addRow(new Object[]{"2024-12-20", param, "Δώρο Χριστ.", "600€"});
                break;

            case 4: 
                setColumns("Κατηγορία", "Συνολικό Κόστος Μισθοδοσίας");
                tableModel.addRow(new Object[]{"Διοικητικοί", "25.000€"});
                tableModel.addRow(new Object[]{"Διδακτικοί", "68.000€"});
                tableModel.addRow(new Object[]{"Σύνολο", "93.000€"});
                break;
        }
    }

    private JPanel createSqlTab() {
        JPanel panel = new JPanel(new BorderLayout(5,5));
        panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        JLabel label = new JLabel("Εισάγετε εντολή SQL (SELECT...):");
        sqlArea = new JTextArea(4, 40);
        sqlArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        JButton execBtn = new JButton("Execute SQL");
        execBtn.addActionListener(e -> executeRawSql());

        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(sqlArea), BorderLayout.CENTER);
        panel.add(execBtn, BorderLayout.SOUTH);

        return panel;
    }

    private void executeRawSql() {
//        String query = sqlArea.getText().trim();
//        if (query.isEmpty()) return;
//
//        // MOCK RESPONSE
//        if (query.toLowerCase().startsWith("select")) {
//            tableModel.setRowCount(0);
//            tableModel.setColumnCount(0);
//            setColumns("Result A", "Result B", "Result C");
//            tableModel.addRow(new Object[]{"Data 1", "Data 2", "Data 3"});
//            tableModel.addRow(new Object[]{"Data 4", "Data 5", "Data 6"});
//            JOptionPane.showMessageDialog(this, "Query Executed Successfully (Mock Mode)");
//        } else {
//            JOptionPane.showMessageDialog(this, "Παρακαλώ εισάγετε εντολή SELECT", "Warning", JOptionPane.WARNING_MESSAGE);
//        }
        String query = sqlArea.getText().trim();
        if (query.isEmpty()) return;

        // Επιτρέπουμε μόνο SELECT για ασφάλεια (προαιρετικό)
        if (!query.toLowerCase().startsWith("select")) {
            JOptionPane.showMessageDialog(this,
                    "Επιτρέπονται μόνο εντολές SELECT σε αυτό το παράθυρο.",
                    "Προσοχή", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Καλούμε τη νέα μέθοδο από το DatabaseHelper
            DefaultTableModel newModel = JDBC.DatabaseHelper.executeQuery(query);

            // Ενημερώνουμε τον πίνακα με τα νέα δεδομένα
            resultTable.setModel(newModel);

            // Κρατάμε αναφορά στο νέο μοντέλο (αν το χρησιμοποιείς αλλού)
            this.tableModel = newModel;

            JOptionPane.showMessageDialog(this, "Το ερώτημα εκτελέστηκε επιτυχώς!");

        } catch (java.sql.SQLException ex) {
            // Εμφάνιση σφάλματος αν το SQL είναι λάθος
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Σφάλμα SQL: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setColumns(String... cols) {
        for (String col : cols) {
            tableModel.addColumn(col);
        }
    }
}