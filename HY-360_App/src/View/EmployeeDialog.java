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

    private JComboBox<String> categoryBox;

    private JTextField departmentField;
    private JTextField startDateField;

    private JTextField addressField;
    private JTextField phoneField;

    private JTextField bankField;
    private JTextField ibanField;

    private boolean readFlag;

    public EmployeeDialog(JFrame parent, String title, boolean readFlag) {
        super(parent, title, true);
        this.readFlag=readFlag;
        setSize(500, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        add(createForm(), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);

        if(readFlag){
            makeFieldsRead();
        }
    }

    // ================= FORM =================
    private JPanel createForm() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nameField = new JTextField();
        maritalBox = new JComboBox<>(new String[] { "Άγαμος", "Έγγαμος" });
        childrenSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        childrenAgesField = new JTextField();

        categoryBox = new JComboBox<>(new String[] { "Διοικητικός", "Διδακτικός" });

        departmentField = new JTextField();
        startDateField = new JTextField("YYYY-MM-DD");

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
        panel.add(new JLabel("Ηλικίες Παιδιών:"));
        panel.add(childrenAgesField);
        panel.add(new JLabel("Κατηγορία:"));
        panel.add(categoryBox);

        panel.add(new JLabel("Τμήμα:"));
        panel.add(departmentField);
        panel.add(new JLabel("Ημ. Έναρξης (YYYY-MM-DD):"));
        panel.add(startDateField);

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

    // ================= BUTTONS =================
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

    // ================= VALIDATION =================
    private void onConfirm() {
        if (nameField.getText().isEmpty()) {
            error("Το όνομα είναι υποχρεωτικό");
            return;
        }

        if (!validateStartDate())
            return;

        confirmed = true;
        dispose();
    }

    private boolean validateStartDate() {
        try {
            LocalDate date = LocalDate.parse(startDateField.getText());
            if (date.getDayOfMonth() != 1) {
                error("Η πρόσληψη πρέπει να γίνεται 1η μέρα του μήνα");
                return false;
            }
            if (date.isBefore(LocalDate.now().withDayOfMonth(1))) {
                error("Δεν επιτρέπεται αναδρομική πρόσληψη");
                return false;
            }
        } catch (Exception e) {
            error("Λάθος μορφή ημερομηνίας");
            return false;
        }
        return true;
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Σφάλμα", JOptionPane.ERROR_MESSAGE);
    }

    // ================= GETTERS =================
    public boolean isConfirmed() {
        return confirmed;
    }

    public String getNameValue() {
        return nameField.getText();
    }

    public String getMaritalStatus() {
        return (String) maritalBox.getSelectedItem();
    }

    public int getNumChildren() {
        return (int) childrenSpinner.getValue();
    }

    public String getChildrenAges() {
        return childrenAgesField.getText();
    }

    public String getCategory() {
        return (String) categoryBox.getSelectedItem();
    }

    public String getDepartment() {
        return departmentField.getText();
    }

    public String getStartDate() {
        return startDateField.getText();
    }

    public String getAddress() {
        return addressField.getText();
    }

    public String getPhone() {
        return phoneField.getText();
    }

    public String getBankName() {
        return bankField.getText();
    }

    public String getIban() {
        return ibanField.getText();
    }

    public void setFields(
            String name,
            String marital,
            int children,
            String ages,
            String category,
            String department,
            String startDate,
            String address,
            String phone,
            String bank,
            String iban) {
        nameField.setText(name);
        maritalBox.setSelectedItem(marital);
        childrenSpinner.setValue(children);
        childrenAgesField.setText(ages);

        categoryBox.setSelectedItem(category);

        departmentField.setText(department);
        startDateField.setText(startDate);
        addressField.setText(address);
        phoneField.setText(phone);
        bankField.setText(bank);
        ibanField.setText(iban);
    }

    private void makeFieldsRead() {
        nameField.setEditable(false);
        maritalBox.setEnabled(false);
        childrenSpinner.setEnabled(false);
        childrenAgesField.setEditable(false);
        categoryBox.setEnabled(false);
        departmentField.setEditable(false);
        startDateField.setEditable(false);
        addressField.setEditable(false);
        phoneField.setEditable(false);
        bankField.setEditable(false);
        ibanField.setEditable(false);
    }

}
