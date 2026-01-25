package View;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;

public class ContractDialog extends JDialog {

    private boolean confirmed = false;
    private JTextField nameField;
    private JComboBox<String> maritalBox;
    private JSpinner childrenSpinner;
    private JTextField childrenNamesField; 
    private JTextField childrenAgesField; 
    private JComboBox<String> categoryBox;
    private JTextField departmentField;
    private JComboBox<String> startMonthBox;
    private JSpinner startYearSpinner;
    private JComboBox<String> endMonthBox;
    private JSpinner endYearSpinner;
    private JTextField addressField;
    private JTextField phoneField;
    private JTextField bankField;
    private JTextField ibanField;
    private final String[] months = {
        "Ιανουάριος", "Φεβρουάριος", "Μάρτιος", "Απρίλιος", "Μάιος", "Ιούνιος",
        "Ιούλιος", "Αύγουστος", "Σεπτέμβριος", "Οκτώβριος", "Νοέμβριος", "Δεκέμβριος"
    };

    public ContractDialog(JFrame parent, String title, boolean readOnly) {
        super(parent, title, true);
        setSize(550, 720); 
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10,10));

        add(createForm(), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);

        if (readOnly) makeFieldsRead();
    }
  
    private JPanel createForm() {
        JPanel panel = new JPanel(new GridLayout(0,2,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        nameField = new JTextField();
        maritalBox = new JComboBox<>(new String[] {"Άγαμος", "Έγγαμος"});
        childrenSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        
        childrenNamesField = new JTextField();
        childrenAgesField = new JTextField(); 

        categoryBox = new JComboBox<>(new String[] {"ADMINISTRATIVE", "TEACHING"});

        departmentField = new JTextField();

        int currentYear = LocalDate.now().getYear();
        
        startMonthBox = new JComboBox<>(months);
        startYearSpinner = new JSpinner(new SpinnerNumberModel(currentYear, 2020, 2050, 1));
        
        endMonthBox = new JComboBox<>(months);
        endYearSpinner = new JSpinner(new SpinnerNumberModel(currentYear, 2020, 2050, 1));
        
    
        JPanel startDatePanel = new JPanel(new GridLayout(1, 2, 5, 0));
        startDatePanel.add(startMonthBox);
        startDatePanel.add(startYearSpinner);

        JPanel endDatePanel = new JPanel(new GridLayout(1, 2, 5, 0));
        endDatePanel.add(endMonthBox);
        endDatePanel.add(endYearSpinner);
       

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
        panel.add(startDatePanel); 
        
        panel.add(new JLabel("Λήξη (Μήνας/Έτος):")); 
        panel.add(endDatePanel);  

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

    public void disableFixedFields() {
        startMonthBox.setEnabled(false);
        startYearSpinner.setEnabled(false);
        endMonthBox.setEnabled(false);
        endYearSpinner.setEnabled(false);

        departmentField.setEditable(false);

        categoryBox.setEnabled(false);
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

        if (!validateDates()) return;

        confirmed = true;
        dispose();
    }

    private boolean validateDates() {
        try {
            LocalDate start = getLocalDateStart();
            LocalDate end = getLocalDateEnd();
            
            if (end.isBefore(start)) {
                error("Η ημερομηνία λήξης δεν μπορεί να είναι πριν την έναρξη");
                return false;
            }
        } catch (Exception e) {
            error("Λάθος στις ημερομηνίες");
            return false;
        }
        return true;
    }

   
    private LocalDate getLocalDateStart() {
        int month = startMonthBox.getSelectedIndex() + 1;
        int year = (int) startYearSpinner.getValue();
        return LocalDate.of(year, month, 1); 
    }

    private LocalDate getLocalDateEnd() {
        int month = endMonthBox.getSelectedIndex() + 1;
        int year = (int) endYearSpinner.getValue();
        
        return YearMonth.of(year, month).atEndOfMonth();
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Σφάλμα", JOptionPane.ERROR_MESSAGE);
    }

    private void makeFieldsRead() {
        nameField.setEditable(false);
        maritalBox.setEnabled(false);
        childrenSpinner.setEnabled(false);
        
        childrenNamesField.setEditable(false);
        childrenAgesField.setEditable(false);
        
        categoryBox.setEnabled(false);
        departmentField.setEditable(false);
        
        startMonthBox.setEnabled(false);
        startYearSpinner.setEnabled(false);
        endMonthBox.setEnabled(false);
        endYearSpinner.setEnabled(false);
        
        addressField.setEditable(false);
        phoneField.setEditable(false);
        bankField.setEditable(false);
        ibanField.setEditable(false);
    }

    public boolean isConfirmed() { return confirmed; }
    
    public String getName() { return nameField.getText(); }
    public String getMarital() { return (String) maritalBox.getSelectedItem(); }
    public int getChildren() { return (int) childrenSpinner.getValue(); }
    
    public String getChildrenNames() { return childrenNamesField.getText(); }
    public String getAges() { return childrenAgesField.getText(); }

    public String getCategory() { return (String) categoryBox.getSelectedItem(); }
    public String getDepartment() { return departmentField.getText(); }
    
    public String getStart() { 
        return getLocalDateStart().toString(); 
    }
    
    public String getEnd() { 
        return getLocalDateEnd().toString(); 
    }


    public String getAddress() { return addressField.getText(); }
    public String getPhone() { return phoneField.getText(); }
    public String getBank() { return bankField.getText(); }
    public String getIban() { return ibanField.getText(); }

    public void setFields(String name, String marital, int children, 
                          String names, String ages, 
                          String category, String department, String startStr, String endStr, 
                          String address, String phone, String bank, String iban) {
        
        nameField.setText(name);
        maritalBox.setSelectedItem(marital);
        childrenSpinner.setValue(children);
        
        childrenNamesField.setText(names);
        childrenAgesField.setText(ages); 
        
        categoryBox.setSelectedItem(category);
        departmentField.setText(department);
        
      
        try {
            LocalDate startDate = LocalDate.parse(startStr);
            startMonthBox.setSelectedIndex(startDate.getMonthValue() - 1);
            startYearSpinner.setValue(startDate.getYear());
        } catch (Exception e) {}

        try {
            if (endStr != null && !endStr.equals("---")) {
                LocalDate endDate = LocalDate.parse(endStr);
                endMonthBox.setSelectedIndex(endDate.getMonthValue() - 1);
                endYearSpinner.setValue(endDate.getYear());
            }
        } catch (Exception e) {}

        addressField.setText(address);
        phoneField.setText(phone);
        bankField.setText(bank);
        ibanField.setText(iban);
    }
}