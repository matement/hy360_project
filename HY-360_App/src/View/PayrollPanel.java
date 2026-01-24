package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.Random; 

public class PayrollPanel extends JPanel {


    private DefaultTableModel settingsModel;
    private DefaultTableModel historyModel;
    private JComboBox<String> monthBox;
    private JSpinner yearSpinner;

    public PayrollPanel() {
        setLayout(new GridLayout(2, 1, 10, 10)); 
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createSettingsPanel());

        add(createExecutionPanel());
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Α. Ρύθμιση Βασικών Μισθών & Επιδομάτων"));

        String[] columns = {"Περιγραφή Κατηγορίας", "Τρέχουσα Τιμή (€)"};
        settingsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        settingsModel.addRow(new Object[]{"Βασικός Μισθός Διοικητικού (Μόνιμος)", 1000.0});
        settingsModel.addRow(new Object[]{"Βασικός Μισθός Διδακτικού (Μόνιμος)", 1300.0});
        settingsModel.addRow(new Object[]{"Επίδομα Έρευνας (Διδακτικοί)", 300.0});
        settingsModel.addRow(new Object[]{"Επίδομα Βιβλιοθήκης (Συμβασιούχοι Διδακτικοί)", 150.0});

        JTable settingsTable = new JTable(settingsModel);
        settingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JButton updateBtn = new JButton("Μεταβολή Τιμής (Αύξηση Μόνο)");
        updateBtn.addActionListener(e -> updateSelectedSetting(settingsTable));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(updateBtn);

        panel.add(new JScrollPane(settingsTable), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updateSelectedSetting(JTable table) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Παρακαλώ επιλέξτε μια κατηγορία προς αλλαγή.");
            return;
        }

        String description = (String) settingsModel.getValueAt(row, 0);
        double currentValue = (Double) settingsModel.getValueAt(row, 1);

        String input = JOptionPane.showInputDialog(this, 
                "Εισάγετε νέα τιμή για:\n" + description + "\n(Τρέχουσα: " + currentValue + "€)", 
                currentValue);

        if (input != null && !input.isEmpty()) {
            try {
                double newValue = Double.parseDouble(input);

                if (newValue < currentValue) {
                    JOptionPane.showMessageDialog(this, 
                        "Σφάλμα: Δεν επιτρέπεται η μείωση μισθών ή επιδομάτων!\n" +
                        "Νέα τιμή: " + newValue + " < Παλιά τιμή: " + currentValue,
                        "Απαγορευμένη Ενέργεια", JOptionPane.ERROR_MESSAGE);
                } else {
                    settingsModel.setValueAt(newValue, row, 1);
                    JOptionPane.showMessageDialog(this, "Η τιμή ενημερώθηκε επιτυχώς.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Παρακαλώ εισάγετε έγκυρο αριθμό.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createExecutionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Β. Εκτέλεση & Ιστορικό Μισθοδοσίας"));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        
        monthBox = new JComboBox<>(new String[]{
            "Ιανουάριος", "Φεβρουάριος", "Μάρτιος", "Απρίλιος", "Μάιος", "Ιούνιος",
            "Ιούλιος", "Αύγουστος", "Σεπτέμβριος", "Οκτώβριος", "Νοέμβριος", "Δεκέμβριος"
        });
        
        int currentYear = LocalDate.now().getYear();
        yearSpinner = new JSpinner(new SpinnerNumberModel(currentYear, 2020, 2050, 1));
        
        JButton runBtn = new JButton("Υπολογισμός & Πληρωμή");
        runBtn.setFont(runBtn.getFont().deriveFont(Font.BOLD));
        runBtn.addActionListener(e -> runPayrollLogic());

        controls.add(new JLabel("Μήνας:"));
        controls.add(monthBox);
        controls.add(new JLabel("Έτος:"));
        controls.add(yearSpinner);
        controls.add(runBtn);

        panel.add(controls, BorderLayout.NORTH);

        String[] cols = {"Ημερομηνία", "Ονοματεπώνυμο", "Κατηγορία", "Βασικός", "Επιδόματα", "Κρατήσεις (0)", "Τελικό Ποσό"};
        historyModel = new DefaultTableModel(cols, 0);
        JTable historyTable = new JTable(historyModel);
        
        panel.add(new JScrollPane(historyTable), BorderLayout.CENTER);

        return panel;
    }

    private void runPayrollLogic() {
        double baseAdmin = (Double) settingsModel.getValueAt(0, 1);
        double baseTeach = (Double) settingsModel.getValueAt(1, 1);
        double researchBonus = (Double) settingsModel.getValueAt(2, 1);
        double libraryBonus = (Double) settingsModel.getValueAt(3, 1);

        // Κανονικά εδώ κάνουμε: SELECT * FROM Employees WHERE Active = 1
        // Για το Demo, θα δημιουργήσουμε εικονικά δεδομένα βάσει των κανόνων της εκφώνησης:
        
        historyModel.setRowCount(0); // Καθαρισμός πίνακα
        String dateStr = LocalDate.now().toString(); // Ημερομηνία πληρωμής

        // --- DEMO 1: Μόνιμος Διοικητικός (10 χρόνια, Έγγαμος, 2 παιδιά) ---
        // Κανόνας: Βασικός + 15% για κάθε χρόνο μετά τον 1ο + Οικ. Επίδομα
        int yearsService = 10;
        double increaseYears = baseAdmin * 0.15 * (yearsService - 1); // 15% για κάθε χρόνο ΜΕΤΑ τον πρώτο
        double familyBonus = baseAdmin * (0.05 + (0.05 * 2)); // 5% σύζυγος + 5% * 2 παιδιά
        
        double totalAdmin = baseAdmin + increaseYears + familyBonus;
        
        historyModel.addRow(new Object[]{
            dateStr, "Γιώργος Παπαδόπουλος", "Μόνιμος Διοικ.", 
            String.format("%.2f€", baseAdmin), 
            String.format("%.2f€", increaseYears + familyBonus), 
            "0€", 
            String.format("%.2f€", totalAdmin)
        });

        // --- DEMO 2: Μόνιμος Διδακτικός (5 χρόνια, Άγαμος) ---
        // Κανόνας: Βασικός + 15% έτη + Επίδομα Έρευνας
        yearsService = 5;
        increaseYears = baseTeach * 0.15 * (yearsService - 1);
        double totalTeach = baseTeach + increaseYears + researchBonus; // + Research (σταθερό)

        historyModel.addRow(new Object[]{
            dateStr, "Μαρία Κωνσταντίνου", "Μόνιμος Διδακ.", 
            String.format("%.2f€", baseTeach), 
            String.format("%.2f€", increaseYears + researchBonus), 
            "0€", 
            String.format("%.2f€", totalTeach)
        });

        // --- DEMO 3: Συμβασιούχος Διδακτικός ---
        // Κανόνας: Μισθός Σύμβασης (πχ 800) + Επίδομα Βιβλιοθήκης
        double contractWage = 800.0;
        double totalContract = contractWage + libraryBonus;

        historyModel.addRow(new Object[]{
            dateStr, "Νίκος Αλεξίου", "Συμβ. Διδακ.", 
            String.format("%.2f€", contractWage), 
            String.format("%.2f€", libraryBonus), 
            "0€", 
            String.format("%.2f€", totalContract)
        });
        
        JOptionPane.showMessageDialog(this, 
            "Η μισθοδοσία για " + monthBox.getSelectedItem() + " " + yearSpinner.getValue() + 
            " ολοκληρώθηκε.\nΠραγματοποιήθηκαν καταθέσεις σε 3 εργαζόμενους.");
    }
}