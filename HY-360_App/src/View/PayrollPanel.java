package View;

import Controller.controller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.Map;

/**
 * PayrollPanel: Οθόνη διαχείρισης μισθοδοσίας.
 * Περιλαμβάνει 3 τμήματα:
 * 1. Πίνακας Βασικών Μισθών (Ανά Ρόλο)
 * 2. Πίνακας Γενικών Επιδομάτων (Global Allowances)
 * 3. Πάνελ Εκτέλεσης Μισθοδοσίας (Επιλογή Μήνα/Έτους)
 */
public class PayrollPanel extends JPanel {

    private controller controller;

    // Μοντέλα δεδομένων για τους πίνακες (JTable)
    private DefaultTableModel salaryModel;
    private DefaultTableModel allowanceModel;

    // Components επιλογής ημερομηνίας
    private JComboBox<String> monthBox;
    private JSpinner yearSpinner;

    public PayrollPanel(controller controller) {
        this.controller = controller;

        // Χρήση GridLayout(3,1) για να χωρίσουμε την οθόνη σε 3 ίσα μέρη κάθετα
        setLayout(new GridLayout(3, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Περιθώρια γύρω-γύρω

        // Προσθήκη των υπο-πάνελ
        add(createSalaryPanel());    // A. Μισθοί
        add(createAllowancePanel()); // B. Επιδόματα
        add(createActionPanel());    // Γ. Εκτέλεση

        // Φόρτωση δεδομένων κατά την εκκίνηση
        loadData();
    }

    // --- 1. SALARY PANEL (Διαχείριση Βασικών Μισθών) ---
    private JPanel createSalaryPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("A. Βασικοί Μισθοί (Ανά Ρόλο)"));

        String[] cols = {"Ρόλος", "Βασικός Μισθός (€)"};
        // Override για να μην επιτρέπεται η απευθείας επεξεργασία στο κελί (μόνο μέσω κουμπιού)
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

    // --- 2. ALLOWANCE PANEL (Διαχείριση Επιδομάτων) ---
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

    // --- 3. ACTION PANEL (Εκτέλεση Μισθοδοσίας) ---
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

        // Προεπιλογή τρέχοντος έτους
        int currentYear = LocalDate.now().getYear();
        yearSpinner = new JSpinner(new SpinnerNumberModel(currentYear, 2020, 2050, 1));
        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#")); // Αφαίρεση διαχωριστικών χιλιάδων (π.χ. 2,025)

        JButton runBtn = new JButton("Εκτέλεση Υπολογισμού");
        runBtn.setBackground(new Color(220, 255, 220)); // Ελαφρύ πράσινο
        runBtn.setFont(new Font("Arial", Font.BOLD, 14));
        runBtn.addActionListener(e -> onRunPayroll());

        // Τοποθέτηση στο GridBagLayout
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Μήνας:"), gbc);
        gbc.gridx = 1; panel.add(monthBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Έτος:"), gbc);
        gbc.gridx = 1; panel.add(yearSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(new JSeparator(), gbc); // Διαχωριστική γραμμή

        gbc.gridy = 3;
        panel.add(runBtn, gbc);

        return panel;
    }

    // --- HELPER: Load Data (Φόρτωση Δεδομένων από Controller) ---
    private void loadData() {
        // Καθαρισμός προηγούμενων δεδομένων
        salaryModel.setRowCount(0);
        allowanceModel.setRowCount(0);

        // Ανάκτηση ΟΛΩΝ των τιμών (Μισθοί + Επιδόματα) σε ένα Map
        Map<String, Double> allRates = controller.getAllSalaryRates();

        for (Map.Entry<String, Double> entry : allRates.entrySet()) {
            String key = entry.getKey();
            Double val = entry.getValue();

            // Λογική διαχωρισμού:
            // Αν το κλειδί είναι ρόλος -> Πίνακας Μισθών
            // Αν το κλειδί είναι επίδομα -> Πίνακας Επιδομάτων
            if (key.equalsIgnoreCase("TEACHING") || key.equalsIgnoreCase("ADMINISTRATIVE")) {
                salaryModel.addRow(new Object[]{key, val});
            } else {
                // Εδώ θα μπουν τα RESEARCH_ALLOWANCE και LIBRARY_ALLOWANCE
                allowanceModel.addRow(new Object[]{key, val});
            }
        }
    }

    // --- HELPER: Editing Logic (Κοινή λογική επεξεργασίας) ---
    private void onEditItem(JTable table, DefaultTableModel model) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Παρακαλώ επιλέξτε μια γραμμή προς επεξεργασία.");
            return;
        }

        String keyName = (String) model.getValueAt(row, 0);
        double currentVal = (Double) model.getValueAt(row, 1);

        String input = JOptionPane.showInputDialog(this,
                "Εισάγετε νέα τιμή για '" + keyName + "':", currentVal);

        if (input != null) {
            try {
                double newVal = Double.parseDouble(input);

                // Κανόνας PDF: Απαγορεύεται η μείωση
                if (newVal < currentVal) {
                    JOptionPane.showMessageDialog(this,
                            "ΣΦΑΛΜΑ: Δεν επιτρέπεται η μείωση μισθών ή επιδομάτων!",
                            "Απαγόρευση Μείωσης", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Ενημέρωση βάσει του ονόματος (Key)
                if (keyName.equalsIgnoreCase("LIBRARY_ALLOWANCE")) {
                    controller.updateLibraryAllowance(newVal);

                } else if (keyName.equalsIgnoreCase("RESEARCH_ALLOWANCE")) {
                    controller.updateResearchAllowance(newVal);

                } else {
                    // Περίπτωση Βασικού Μισθού (TEACHING / ADMINISTRATIVE)
                    controller.updateBaseSalary(keyName, newVal);
                }

                // Ανανέωση του πίνακα για να φανεί η αλλαγή
                loadData();
                JOptionPane.showMessageDialog(this, "Η τιμή ενημερώθηκε επιτυχώς!");

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Παρακαλώ εισάγετε έγκυρο αριθμό (π.χ. 1200.50).");
            }
        }
    }

    // --- SUCCESS PANEL (Μήνυμα Επιτυχίας) ---
    private JPanel createCompletedPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel label = new JLabel("Η Μισθοδοσία Ολοκληρώθηκε!");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(new Color(0, 100, 0));
        panel.add(label);
        return panel;
    }

    // --- RUN PAYROLL (Κλήση στον Controller) ---
    private void onRunPayroll() {
        int m = monthBox.getSelectedIndex() + 1; // Οι μήνες ξεκινάνε από 0 στο ComboBox
        int y = (Integer) yearSpinner.getValue();

        // Εκτέλεση της λογικής μισθοδοσίας
        controller.runPayroll(m, y);

        // Εμφάνιση μηνύματος επιτυχίας
        JOptionPane.showMessageDialog(this, createCompletedPanel(), "Κατάσταση", JOptionPane.PLAIN_MESSAGE);
    }
}