package View;

import Controller.controller; 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.Map;

public class PayrollPanel extends JPanel {

    private controller controller;
    
    // Μοντέλα πινάκων
    private DefaultTableModel salaryModel;
    private DefaultTableModel allowanceModel;
    
    private JComboBox<String> monthBox;
    private JSpinner yearSpinner;

    public PayrollPanel(controller controller) {
        this.controller = controller;
        
        // Layout: 3 γραμμές (Μισθοί - Επιδόματα - Ενέργειες)
        setLayout(new GridLayout(3, 1, 10, 10)); 
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createSalaryPanel());    // Row 1
        add(createAllowancePanel()); // Row 2
        add(createActionPanel());    // Row 3

        loadData(); 
    }

    // --- 1. SALARY PANEL ---
    private JPanel createSalaryPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("A. Βασικοί Μισθοί (Ανά Ρόλο)"));

        String[] cols = {"Ρόλος", "Βασικός Μισθός (€)"};
        salaryModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(salaryModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);

        JButton editBtn = new JButton("Επεξεργασία Μισθού");
        editBtn.addActionListener(e -> onEditItem(table, salaryModel));

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(editBtn, BorderLayout.SOUTH);
        return panel;
    }

    // --- 2. ALLOWANCE PANEL ---
    private JPanel createAllowancePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("B. Γενικά Επιδόματα & Παροχές"));

        String[] cols = {"Περιγραφή Επιδόματος", "Ποσό (€)"};
        allowanceModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(allowanceModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);

        JButton editBtn = new JButton("Επεξεργασία Επιδόματος");
        editBtn.addActionListener(e -> onEditItem(table, allowanceModel));

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(editBtn, BorderLayout.SOUTH);
        return panel;
    }

    // --- 3. ACTION PANEL ---
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Γ. Εκτέλεση Μισθοδοσίας"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] months = {
            "1 - Ιανουάριος", "2 - Φεβρουάριος", "3 - Μάρτιος", "4 - Απρίλιος", 
            "5 - Μάιος", "6 - Ιούνιος", "7 - Ιούλιος", "8 - Αύγουστος", 
            "9 - Σεπτέμβριος", "10 - Οκτώβριος", "11 - Νοέμβριος", "12 - Δεκέμβριος"
        };
        monthBox = new JComboBox<>(months);
        
        int currentYear = LocalDate.now().getYear();
        yearSpinner = new JSpinner(new SpinnerNumberModel(currentYear, 2020, 2050, 1));
        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));

        JButton runBtn = new JButton("Εκτέλεση");
        runBtn.setBackground(new Color(220, 255, 220));
        runBtn.setFont(new Font("Arial", Font.BOLD, 14));
        runBtn.addActionListener(e -> onRunPayroll());

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Μήνας:"), gbc);
        gbc.gridx = 1; panel.add(monthBox, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Έτος:"), gbc);
        gbc.gridx = 1; panel.add(yearSpinner, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; panel.add(new JSeparator(), gbc);
        gbc.gridy = 3; panel.add(runBtn, gbc);

        return panel;
    }

    // --- HELPER: Load Data ---
    private void loadData() {
        salaryModel.setRowCount(0);
        allowanceModel.setRowCount(0);

        Map<String, Double> allRates = controller.getAllSalaryRates();

        for (Map.Entry<String, Double> entry : allRates.entrySet()) {
            String key = entry.getKey();
            Double val = entry.getValue();

            // Διαχωρισμός: Ρόλοι πάνω, Επιδόματα κάτω
            if (key.equalsIgnoreCase("TEACHING") || key.equalsIgnoreCase("ADMINISTRATIVE")) {
                salaryModel.addRow(new Object[]{key, val});
            } else {
                allowanceModel.addRow(new Object[]{key, val});
            }
        }
    }

    // --- HELPER: Editing Logic (ΕΔΩ ΕΓΙΝΕ Η ΑΛΛΑΓΗ) ---
    private void onEditItem(JTable table, DefaultTableModel model) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Επίλεξε μια γραμμή πρώτα.");
            return;
        }

        String keyName = (String) model.getValueAt(row, 0);
        double currentVal = (Double) model.getValueAt(row, 1);

        String input = JOptionPane.showInputDialog(this, 
            "Νέα τιμή για '" + keyName + "':", currentVal);

        if (input != null) {
            try {
                double newVal = Double.parseDouble(input);
                if (newVal < currentVal) {
                    JOptionPane.showMessageDialog(this, "Δεν επιτρέπεται η μείωση!", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // --- ΛΟΓΙΚΗ ΑΝΑΛΟΓΑ ΜΕ ΤΟ ΚΛΕΙΔΙ ---
                if (keyName.equalsIgnoreCase("LIBRARY_ALLOWANCE")) {
                    // Καλούμε τη νέα μέθοδο για Library
                    controller.updateLibraryAllowance(newVal);
                
                } else if (keyName.equalsIgnoreCase("RESEARCH_ALLOWANCE")) {
                    // Καλούμε τη νέα μέθοδο για Research
                    controller.updateResearchAllowance(newVal);
                
                } else {
                    // Για τους μισθούς (TEACHING / ADMINISTRATIVE) ή άλλα
                    controller.updateBaseSalary(keyName, newVal);
                }

                // Ανανέωση πινάκων
                loadData();
                JOptionPane.showMessageDialog(this, "Ενημερώθηκε!");

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Λάθος αριθμός.");
            }
        }
    }

    // --- SUCCESS PANEL ---
    private JPanel createCompletedPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel label = new JLabel("Completed");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(new Color(0, 100, 0));
        panel.add(label);
        return panel;
    }

    // --- RUN PAYROLL ---
    private void onRunPayroll() {
        int m = monthBox.getSelectedIndex() + 1;
        int y = (Integer) yearSpinner.getValue();
        
        controller.runPayroll(m, y);
        
        JOptionPane.showMessageDialog(this, createCompletedPanel(), "Status", JOptionPane.PLAIN_MESSAGE);
    }
}