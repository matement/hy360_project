package View;

import Controller.controller; 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.Map;

public class PayrollPanel extends JPanel {

    private controller controller; 
    
    private DefaultTableModel settingsModel;
    private DefaultTableModel historyModel;
    private JComboBox<String> monthBox;
    private JSpinner yearSpinner;

    public PayrollPanel(controller controller) {
        this.controller = controller;

        setLayout(new GridLayout(2, 1, 10, 10)); 
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createSettingsPanel());
        add(createExecutionPanel());

        loadSettingsFromDB();
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Α. Ρύθμιση Βασικών Μισθών (Από Βάση Δεδομένων)"));

        String[] columns = {"Ρόλος / Κατηγορία", "Βασικός Μισθός (€)"};
        settingsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        JTable settingsTable = new JTable(settingsModel);
        settingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JButton updateBtn = new JButton("Ενημέρωση Επιλεγμένου");
        updateBtn.addActionListener(e -> updateSelectedSetting(settingsTable));
        JButton refreshBtn = new JButton("Ανανέωση Λίστας");
        refreshBtn.addActionListener(e -> loadSettingsFromDB());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(updateBtn);
        btnPanel.add(refreshBtn);

        panel.add(new JScrollPane(settingsTable), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadSettingsFromDB() {
        settingsModel.setRowCount(0);
        Map<String, Double> rates = controller.getAllSalaryRates();
        
        for (Map.Entry<String, Double> entry : rates.entrySet()) {
            settingsModel.addRow(new Object[]{
                entry.getKey(),
                entry.getValue()
            });
        }
    }

    private void updateSelectedSetting(JTable table) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Παρακαλώ επιλέξτε μια κατηγορία προς αλλαγή.");
            return;
        }

        String roleName = (String) settingsModel.getValueAt(row, 0);
        double currentValue = (Double) settingsModel.getValueAt(row, 1);

        String input = JOptionPane.showInputDialog(this, 
                "Εισάγετε νέο Βασικό Μισθό για: " + roleName + "\n(Τρέχουσα: " + currentValue + "€)", 
                currentValue);

        if (input != null && !input.isEmpty()) {
            try {
                double newValue = Double.parseDouble(input);

                if (newValue < currentValue) {
                    JOptionPane.showMessageDialog(this, 
                        "Προσοχή: Επιχειρείτε μείωση μισθού.\nΗ αλλαγή θα προχωρήσει, αλλά βεβαιωθείτε ότι είναι νόμιμη.",
                        "Προειδοποίηση", JOptionPane.WARNING_MESSAGE);
                }

                controller.updateBaseSalary(roleName, newValue);
                
                loadSettingsFromDB();
                JOptionPane.showMessageDialog(this, "Η τιμή ενημερώθηκε στη βάση δεδομένων.");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Παρακαλώ εισάγετε έγκυρο αριθμό.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createExecutionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Β. Εκτέλεση Μισθοδοσίας"));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        
        monthBox = new JComboBox<>(new String[]{
            "1 - Ιανουάριος", "2 - Φεβρουάριος", "3 - Μάρτιος", "4 - Απρίλιος", 
            "5 - Μάιος", "6 - Ιούνιος", "7 - Ιούλιος", "8 - Αύγουστος", 
            "9 - Σεπτέμβριος", "10 - Οκτώβριος", "11 - Νοέμβριος", "12 - Δεκέμβριος"
        });
        
        int currentYear = LocalDate.now().getYear();
        yearSpinner = new JSpinner(new SpinnerNumberModel(currentYear, 2020, 2050, 1));
        
        JButton runBtn = new JButton("Εκτέλεση Υπολογισμού");
        runBtn.setFont(runBtn.getFont().deriveFont(Font.BOLD));
        runBtn.setForeground(new Color(0, 100, 0));
        runBtn.addActionListener(e -> runPayrollLogic());

        controls.add(new JLabel("Μήνας:"));
        controls.add(monthBox);
        controls.add(new JLabel("Έτος:"));
        controls.add(yearSpinner);
        controls.add(runBtn);

        panel.add(controls, BorderLayout.NORTH);

        String[] cols = {"Μήνυμα Συστήματος"};
        historyModel = new DefaultTableModel(cols, 0);
        JTable historyTable = new JTable(historyModel);
        panel.add(new JScrollPane(historyTable), BorderLayout.CENTER);

        return panel;
    }

    private void runPayrollLogic() {
        int monthIndex = monthBox.getSelectedIndex() + 1;
        int year = (Integer) yearSpinner.getValue();

        historyModel.setRowCount(0);
        historyModel.addRow(new Object[]{"Εκκίνηση διαδικασίας..."});

        new Thread(() -> {
            try {
                
                controller.runPayroll(monthIndex, year);

                SwingUtilities.invokeLater(() -> {
                    historyModel.addRow(new Object[]{"Η μισθοδοσία ολοκληρώθηκε επιτυχώς!"});
                    historyModel.addRow(new Object[]{"Οι πληρωμές καταχωρήθηκαν στον πίνακα Payment."});
                    JOptionPane.showMessageDialog(this, "Η μισθοδοσία για " + monthIndex + "/" + year + " ολοκληρώθηκε.");
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    historyModel.addRow(new Object[]{"Σφάλμα: " + ex.getMessage()});
                    JOptionPane.showMessageDialog(this, "Σφάλμα κατά την εκτέλεση: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }
}