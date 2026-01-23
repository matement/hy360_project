package View;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ContractDialog extends JDialog {

    private boolean confirmed = false;

    private JTextField nameField;
    private JComboBox<String> maritalBox;
    private JSpinner childrenSpinner;
    private JTextField agesField;
    private JTextField categoryField;
    private JTextField departmentField;
    private JTextField startField;
    private JTextField endField;
    private JTextField addressField;
    private JTextField phoneField;
    private JTextField bankField;
    private JTextField ibanField;

    private boolean readOnly;

    public ContractDialog(JFrame parent, String title, boolean readOnly) {
        super(parent, title, true);
        this.readOnly = readOnly;
        setSize(500, 650);
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
        childrenSpinner = new JSpinner(new SpinnerNumberModel(0,0,10,1));
        agesField = new JTextField();
        categoryField = new JTextField();
        departmentField = new JTextField();
        startField = new JTextField("YYYY-MM-DD");
        endField = new JTextField("YYYY-MM-DD");
        addressField = new JTextField();
        phoneField = new JTextField();
        bankField = new JTextField();
        ibanField = new JTextField();

        panel.add(new JLabel("Όνομα:")); panel.add(nameField);
        panel.add(new JLabel("Οικ. Κατάσταση:")); panel.add(maritalBox);
        panel.add(new JLabel("Αριθμός Παιδιών:")); panel.add(childrenSpinner);
        panel.add(new JLabel("Ηλικίες Παιδιών:")); panel.add(agesField);
        panel.add(new JLabel("Κατηγορία:")); panel.add(categoryField);
        panel.add(new JLabel("Τμήμα:")); panel.add(departmentField);
        panel.add(new JLabel("Ημ. Έναρξης (YYYY-MM-DD):")); panel.add(startField);
        panel.add(new JLabel("Ημ. Λήξης (YYYY-MM-DD):")); panel.add(endField);
        panel.add(new JLabel("Διεύθυνση:")); panel.add(addressField);
        panel.add(new JLabel("Τηλέφωνο:")); panel.add(phoneField);
        panel.add(new JLabel("Τράπεζα:")); panel.add(bankField);
        panel.add(new JLabel("IBAN:")); panel.add(ibanField);

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

        if (!validateDates()) return;

        confirmed = true;
        dispose();
    }

    private boolean validateDates() {
        try {
            LocalDate start = LocalDate.parse(startField.getText());
            LocalDate end = LocalDate.parse(endField.getText());
            if (end.isBefore(start)) {
                error("Η ημερομηνία λήξης πρέπει να είναι μετά την έναρξη");
                return false;
            }
        } catch (DateTimeParseException e) {
            error("Λάθος μορφή ημερομηνίας. Χρησιμοποίησε YYYY-MM-DD");
            return false;
        }
        return true;
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Σφάλμα", JOptionPane.ERROR_MESSAGE);
    }

    private void makeFieldsRead() {
        nameField.setEditable(false);
        maritalBox.setEnabled(false);
        childrenSpinner.setEnabled(false);
        agesField.setEditable(false);
        categoryField.setEditable(false);
        departmentField.setEditable(false);
        startField.setEditable(false);
        endField.setEditable(false);
        addressField.setEditable(false);
        phoneField.setEditable(false);
        bankField.setEditable(false);
        ibanField.setEditable(false);
    }

    // ================= GETTERS =================
    public boolean isConfirmed() { return confirmed; }

    public String getName() { return nameField.getText(); }
    public String getMarital() { return (String) maritalBox.getSelectedItem(); }
    public int getChildren() { return (int) childrenSpinner.getValue(); }
    public String getAges() { return agesField.getText(); }
    public String getCategory() { return categoryField.getText(); }
    public String getDepartment() { return departmentField.getText(); }
    public String getStart() { return startField.getText(); }
    public String getEnd() { return endField.getText(); }
    public String getAddress() { return addressField.getText(); }
    public String getPhone() { return phoneField.getText(); }
    public String getBank() { return bankField.getText(); }
    public String getIban() { return ibanField.getText(); }

    // ================= SETFIELDS =================
    public void setFields(
            String name,
            String marital,
            int children,
            String ages,
            String category,
            String department,
            String start,
            String end,
            String address,
            String phone,
            String bank,
            String iban
    ) {
        nameField.setText(name);
        maritalBox.setSelectedItem(marital);
        childrenSpinner.setValue(children);
        agesField.setText(ages);
        categoryField.setText(category);
        departmentField.setText(department);
        startField.setText(start);
        endField.setText(end);
        addressField.setText(address);
        phoneField.setText(phone);
        bankField.setText(bank);
        ibanField.setText(iban);
    }
}
