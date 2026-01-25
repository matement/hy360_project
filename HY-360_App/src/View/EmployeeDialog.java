package View;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class EmployeeDialog extends JDialog {

    private boolean confirmed = false;

    private JTextField nameField;
    private JComboBox<String> maritalBox;
    private JSpinner childrenSpinner;
    private JTextField childrenAgesField;
    private JTextField childrenNamesField; 

    private JComboBox<String> categoryBox;

    private JTextField departmentField;

    // --- ΑΛΛΑΓΗ: UI ΓΙΑ ΜΗΝΑ ΚΑΙ ΕΤΟΣ ---
    private JComboBox<String> startMonthBox;
    private JSpinner startYearSpinner;
    // ------------------------------------

    private JTextField addressField;
    private JTextField phoneField;

    private JTextField bankField;
    private JTextField ibanField;
        
    private final String[] months = {
        "Ιανουάριος", "Φεβρουάριος", "Μάρτιος", "Απρίλιος", "Μάιος", "Ιούνιος",
        "Ιούλιος", "Αύγουστος", "Σεπτέμβριος", "Οκτώβριος", "Νοέμβριος", "Δεκέμβριος"
    };

    public EmployeeDialog(JFrame parent, String title, boolean readFlag) {
        super(parent, title, true);
        setSize(500, 700); 
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        add(createForm(), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);

        if(readFlag){
            makeFieldsRead();
        }
    }

    private JPanel createForm() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nameField = new JTextField();
        maritalBox = new JComboBox<>(new String[] { "Άγαμος", "Έγγαμος" });
        childrenSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        
        childrenNamesField = new JTextField();
        childrenAgesField = new JTextField();

        categoryBox = new JComboBox<>(new String[] { "ADMINISTRATIVE", "TEACHING" });

        departmentField = new JTextField();

        // --- SETUP DATE PICKER ---
        int currentYear = LocalDate.now().getYear();
        
        startMonthBox = new JComboBox<>(months);
        startYearSpinner = new JSpinner(new SpinnerNumberModel(currentYear, 2020, 2050, 1));
        
        // Panel για να μπουν δίπλα δίπλα
        JPanel startDatePanel = new JPanel(new GridLayout(1, 2, 5, 0));
        startDatePanel.add(startMonthBox);
        startDatePanel.add(startYearSpinner);
        // -------------------------

        addressField = new JTextField();
        phoneField = new JTextField();

        bankField = new JTextField();
        ibanField = new JTextField();

        panel.add(new JLabel("Όνομα:"));
        panel.add(nameField);
        
        panel.add(new JLabel("Οικ. Κατάσταση:"));
        panel.add(maritalBox);
        
        panel.add(new JLabel("Αριθμός Παιδιών:"));
        panel.add(childrenSpinner);
        
        panel.add(new JLabel("Ονόματα Παιδιών:")); 
        panel.add(childrenNamesField);            
        
        panel.add(new JLabel("Ηλικίες Παιδιών:"));
        panel.add(childrenAgesField);
        
        panel.add(new JLabel("Κατηγορία:"));
        panel.add(categoryBox);

        panel.add(new JLabel("Τμήμα:"));
        panel.add(departmentField);
        
        panel.add(new JLabel("Έναρξη (Μήνας/Έτος):")); 
        panel.add(startDatePanel); // Προσθέτουμε το Panel, όχι Textfield

        panel.add(new JLabel("Διεύθυνση:"));
        panel.add(addressField);
        
        panel.add(new JLabel("Τηλέφωνο:"));
        panel.add(phoneField);

        panel.add(new JLabel("Τράπεζα:"));
        panel.add(bankField);
        
        panel.add(new JLabel("IBAN:"));
        panel.add(ibanField);

        return panel;
    }

    private JPanel createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Cancel");

        okBtn.addActionListener(e -> onConfirm());
        cancelBtn.addActionListener(e -> dispose());

        panel.add(okBtn);
        panel.add(cancelBtn);

        return panel;
    }

    private void onConfirm() {
        if (nameField.getText().isEmpty()) {
            error("Το όνομα είναι υποχρεωτικό");
            return;
        }

        // Έλεγχος Αναδρομικότητας
        if (!validateStartDate())
            return;

        confirmed = true;
        dispose();
    }

    private boolean validateStartDate() {
        try {
            // Δημιουργία ημερομηνίας από τα πεδία (πάντα 1η του μήνα)
            int month = startMonthBox.getSelectedIndex() + 1;
            int year = (int) startYearSpinner.getValue();
            LocalDate selectedDate = LocalDate.of(year, month, 1);
            
            // Ημερομηνία ελέγχου: Η 1η μέρα του ΤΡΕΧΟΝΤΟΣ μήνα
            LocalDate validFrom = LocalDate.now().withDayOfMonth(1);

            // Αν η επιλεγμένη είναι πριν την τρέχουσα (αναδρομική)
            if (selectedDate.isBefore(validFrom)) {
                 error("Δεν επιτρέπεται αναδρομική πρόσληψη (πριν τον τρέχοντα μήνα).");
                 return false;
            }

        } catch (Exception e) {
            error("Σφάλμα στην ημερομηνία");
            return false;
        }
        return true;
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Σφάλμα", JOptionPane.ERROR_MESSAGE);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    // Getters UI Values
    public String getNameValue() { return nameField.getText(); }
    public String getMaritalStatus() { return (String) maritalBox.getSelectedItem(); }
    public int getNumChildren() { return (int) childrenSpinner.getValue(); }
    public String getChildrenNames() { return childrenNamesField.getText(); }
    public String getChildrenAges() { return childrenAgesField.getText(); }
    public String getCategory() { return (String) categoryBox.getSelectedItem(); }
    public String getDepartment() { return departmentField.getText(); }
    
    // --- ΝΕΟ GETTER: Επιστρέφει String YYYY-MM-01 ---
    public String getStartDate() {
        int month = startMonthBox.getSelectedIndex() + 1;
        int year = (int) startYearSpinner.getValue();
        return LocalDate.of(year, month, 1).toString();
    }
    // ------------------------------------------------

    public String getAddress() { return addressField.getText(); }
    public String getPhone() { return phoneField.getText(); }
    public String getBankName() { return bankField.getText(); }
    public String getIban() { return ibanField.getText(); }

    // --- SET FIELDS (Για Edit/View) ---
    public void setFields(
            String name,
            String marital,
            int children,
            String childrenNames, 
            String ages,
            String category,
            String department,
            String startDateStr, // Έρχεται ως String "YYYY-MM-DD"
            String address,
            String phone,
            String bank,
            String iban) {
        
        nameField.setText(name);
        maritalBox.setSelectedItem(marital);
        childrenSpinner.setValue(children);
        
        childrenNamesField.setText(childrenNames); 
        childrenAgesField.setText(ages);

        if (category != null) {
            for (int i=0; i<categoryBox.getItemCount(); i++) {
                if (category.contains(categoryBox.getItemAt(i))) {
                    categoryBox.setSelectedIndex(i);
                    break;
                }
            }
        }

        departmentField.setText(department);
        
        // Parse Start Date -> UI
        try {
            LocalDate date = LocalDate.parse(startDateStr);
            startMonthBox.setSelectedIndex(date.getMonthValue() - 1);
            startYearSpinner.setValue(date.getYear());
        } catch (Exception e) {
            // Αν είναι null ή λάθος format
        }

        addressField.setText(address);
        phoneField.setText(phone);
        bankField.setText(bank);
        ibanField.setText(iban);
    }

    // Πρόσθεσε αυτή τη μέθοδο στο View/EmployeeDialog.java
    public void disableFixedFields() {
        // 1. Κλείδωμα Ημερομηνίας Έναρξης
        startMonthBox.setEnabled(false);
        startYearSpinner.setEnabled(false);

        // 2. Κλείδωμα Τμήματος
        departmentField.setEditable(false);

        // 3. Κλείδωμα Κατηγορίας
        categoryBox.setEnabled(false);
    }

    private void makeFieldsRead() {
        nameField.setEditable(false);
        maritalBox.setEnabled(false);
        childrenSpinner.setEnabled(false);
        childrenNamesField.setEditable(false);
        childrenAgesField.setEditable(false);
        categoryBox.setEnabled(false);
        departmentField.setEditable(false);
        
        // Disable date selection
        startMonthBox.setEnabled(false);
        startYearSpinner.setEnabled(false);

        addressField.setEditable(false);
        phoneField.setEditable(false);
        bankField.setEditable(false);
        ibanField.setEditable(false);
    }
}