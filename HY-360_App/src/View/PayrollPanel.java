package View;

import Controller.controller; 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PayrollPanel extends JPanel {

    private controller controller;
    private DefaultTableModel ratesModel;
    private JComboBox<String> monthBox;
    private JSpinner yearSpinner;

    public PayrollPanel(controller controller) {
        this.controller = controller;
        
        // Απλό Layout: 2 γραμμές (Πάνω: Ρυθμίσεις, Κάτω: Ενέργειες)
        setLayout(new GridLayout(2, 1, 15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(createRatesPanel());
        add(createActionPanel());

        loadRates(); // Φόρτωμα δεδομένων κατά την εκκίνηση
    }

    // --- 1. ΠΑΝΕΛ ΡΥΘΜΙΣΕΩΝ ΜΙΣΘΩΝ ---
    private JPanel createRatesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Βασικοί Μισθοί & Επιδόματα"));

        // Πίνακας
        String[] cols = {"Κατηγορία / Ρόλος", "Ποσό (€)"};
        ratesModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(ratesModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);

        // Κουμπί Επεξεργασίας
        JButton editBtn = new JButton("Αλλαγή Μισθού");
        editBtn.addActionListener(e -> onEditRate(table));

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(editBtn, BorderLayout.SOUTH);
        
        return panel;
    }

    // --- 2. ΠΑΝΕΛ ΕΚΤΕΛΕΣΗΣ (ΧΩΡΙΣ ΤΟ ΚΟΥΜΠΙ ΣΤΑΤΙΣΤΙΚΩΝ) ---
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Διαχείριση Μισθοδοσίας"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Επιλογές Ημερομηνίας
        String[] months = {
            "1 - Ιανουάριος", "2 - Φεβρουάριος", "3 - Μάρτιος", "4 - Απρίλιος", 
            "5 - Μάιος", "6 - Ιούνιος", "7 - Ιούλιος", "8 - Αύγουστος", 
            "9 - Σεπτέμβριος", "10 - Οκτώβριος", "11 - Νοέμβριος", "12 - Δεκέμβριος"
        };
        monthBox = new JComboBox<>(months);
        
        int currentYear = LocalDate.now().getYear();
        yearSpinner = new JSpinner(new SpinnerNumberModel(currentYear, 2020, 2050, 1));
        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));

        // Κουμπί
        JButton runBtn = new JButton("Εκτέλεση Μισθοδοσίας");
        runBtn.setBackground(new Color(220, 255, 220)); // Ελαφρύ πράσινο
        runBtn.setFont(new Font("Arial", Font.BOLD, 14));
        runBtn.addActionListener(e -> onRunPayroll());

        // Τοποθέτηση στο Grid
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Επιλογή Μήνα:"), gbc);
        
        gbc.gridx = 1;
        panel.add(monthBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Επιλογή Έτους:"), gbc);
        
        gbc.gridx = 1;
        panel.add(yearSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(new JSeparator(), gbc);

        gbc.gridy = 3;
        panel.add(runBtn, gbc);

        return panel;
    }

    // --- LOGIC: ΦΟΡΤΩΜΑ ΔΕΔΟΜΕΝΩΝ ---
    private void loadRates() {
        ratesModel.setRowCount(0);
        Map<String, Double> rates = controller.getAllSalaryRates();
        for (Map.Entry<String, Double> entry : rates.entrySet()) {
            ratesModel.addRow(new Object[]{ entry.getKey(), entry.getValue() });
        }
    }

    // --- LOGIC: ΕΠΕΞΕΡΓΑΣΙΑ ΜΙΣΘΟΥ ---
    private void onEditRate(JTable table) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Επίλεξε μια κατηγορία πρώτα.");
            return;
        }

        String role = (String) ratesModel.getValueAt(row, 0);
        double currentVal = (Double) ratesModel.getValueAt(row, 1);

        String input = JOptionPane.showInputDialog(this, 
            "Νέος μισθός για " + role + ":", currentVal);

        if (input != null) {
            try {
                double newVal = Double.parseDouble(input);
                
                // Έλεγχος: Απαγόρευση μείωσης
                if (newVal < currentVal) {
                    JOptionPane.showMessageDialog(this, 
                        "Δεν επιτρέπεται η μείωση μισθού!", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                controller.updateBaseSalary(role, newVal);
                loadRates(); // Ανανέωση πίνακα
                JOptionPane.showMessageDialog(this, "Ο μισθός ενημερώθηκε!");

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Λάθος αριθμός.");
            }
        }
    }

    // --- LOGIC: ΕΚΤΕΛΕΣΗ ΜΙΣΘΟΔΟΣΙΑΣ ---
    private void onRunPayroll() {
        int month = monthBox.getSelectedIndex() + 1;
        int year = (Integer) yearSpinner.getValue();

        // 1. Εκτέλεση
        controller.runPayroll(month, year);
    }
}