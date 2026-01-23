package View;

import javax.swing.*;
import java.awt.*;

public class EmployeeDialog extends JDialog {

    private JTextField tfName, tfChildrenAges, tfDepartment, tfStartDate, tfAddress, tfPhone, tfBankName, tfBankAccount;
    private JComboBox<String> cbMaritalStatus, cbCategory;
    private JSpinner spChildren;
    private boolean confirmed = false;

    public EmployeeDialog(JFrame parent, String title) {
        super(parent, title, true);
        setupUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void setupUI() {
        setLayout(new BorderLayout(10,10));

        JPanel form = new JPanel(new GridLayout(12,2,5,5));

        form.add(new JLabel("Όνομα:"));
        tfName = new JTextField();
        form.add(tfName);

        form.add(new JLabel("Οικογενειακή κατάσταση:"));
        cbMaritalStatus = new JComboBox<>(new String[]{"Άγαμος","Έγγαμος"});
        form.add(cbMaritalStatus);

        form.add(new JLabel("Αριθμός παιδιών:"));
        spChildren = new JSpinner(new SpinnerNumberModel(0,0,10,1));
        form.add(spChildren);

        form.add(new JLabel("Ηλικίες παιδιών:"));
        tfChildrenAges = new JTextField();
        form.add(tfChildrenAges);

        form.add(new JLabel("Κατηγορία προσωπικού:"));
        cbCategory = new JComboBox<>(new String[]{
            "Μόνιμος Διοικητικός", "Συμβασιούχος Διοικητικός",
            "Μόνιμος Διδακτικός", "Συμβασιούχος Διδακτικός"
        });
        form.add(cbCategory);

        form.add(new JLabel("Τμήμα:"));
        tfDepartment = new JTextField();
        form.add(tfDepartment);

        form.add(new JLabel("Ημερ. Έναρξης:"));
        tfStartDate = new JTextField();
        form.add(tfStartDate);

        form.add(new JLabel("Διεύθυνση:"));
        tfAddress = new JTextField();
        form.add(tfAddress);

        form.add(new JLabel("Τηλέφωνο:"));
        tfPhone = new JTextField();
        form.add(tfPhone);

        form.add(new JLabel("Τράπεζα:"));
        tfBankName = new JTextField();
        form.add(tfBankName);

        form.add(new JLabel("Αρ. Λογαριασμού:"));
        tfBankAccount = new JTextField();
        form.add(tfBankAccount);

        add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Cancel");

        okBtn.addActionListener(e -> {
            confirmed = true;
            setVisible(false);
        });

        cancelBtn.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
        });

        buttons.add(okBtn);
        buttons.add(cancelBtn);
        add(buttons, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() { return confirmed; }
    public String getNameValue() { return tfName.getText(); }
    public String getMaritalStatus() { return (String)cbMaritalStatus.getSelectedItem(); }
    public int getNumChildren() { return (Integer)spChildren.getValue(); }
    public String getChildrenAges() { return tfChildrenAges.getText(); }
    public String getCategory() { return (String)cbCategory.getSelectedItem(); }
    public String getDepartment() { return tfDepartment.getText(); }
    public String getStartDate() { return tfStartDate.getText(); }
    public String getAddress() { return tfAddress.getText(); }
    public String getPhone() { return tfPhone.getText(); }
    public String getBankName() { return tfBankName.getText(); }
    public String getBankAccount() { return tfBankAccount.getText(); }

    public void setFields(String name, String marital, int children, String ages,
                          String category, String dept, String startDate,
                          String address, String phone, String bank, String account) {
        tfName.setText(name);
        cbMaritalStatus.setSelectedItem(marital);
        spChildren.setValue(children);
        tfChildrenAges.setText(ages);
        cbCategory.setSelectedItem(category);
        tfDepartment.setText(dept);
        tfStartDate.setText(startDate);
        tfAddress.setText(address);
        tfPhone.setText(phone);
        tfBankName.setText(bank);
        tfBankAccount.setText(account);
    }
}
