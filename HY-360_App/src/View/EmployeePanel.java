package View;

import Controller.controller; 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeePanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private controller controller;

    public EmployeePanel(controller controller) {
        this.controller = controller;

        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(createTitle(), BorderLayout.NORTH);
        add(createTable(), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);

        refreshTable(); 
    }

    private void refreshTable() {
        model.setRowCount(0); 
        List<Object[]> rows = controller.getAllEmployees(); 
        for (Object[] row : rows) {
            model.addRow(row);
        }
    }

    private JLabel createTitle() {
        JLabel label = new JLabel("Διαχείριση Υπαλλήλων", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 22f));
        return label;
    }

    private JScrollPane createTable() {
        String[] columns = {
            "ID", "Όνομα", "Οικ. Κατάσταση", "Παιδιά", "Ονόματα Παιδιών", 
            "Ηλικίες Παιδιών", "Κατηγορία", "Τμήμα", "Ημ. Έναρξης", 
            "Διεύθυνση", "Τηλέφωνο", "Τράπεζα", "Αρ. Λογαριασμού", "Κατάσταση"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        

        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Όνομα
        table.getColumnModel().getColumn(4).setPreferredWidth(150); // Ονόματα παιδιών
        table.getColumnModel().getColumn(12).setPreferredWidth(150); // IBAN
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        return new JScrollPane(table);
    }

    private JPanel createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,15,10));

        JButton addBtn = new JButton("Πρόσληψη");
        JButton editBtn = new JButton("Επεξεργασία");
        JButton fireBtn = new JButton("Απόλυση/Συνταξιοδότηση");
        JButton viewBtn = new JButton("Προβολή");
        JButton refreshBtn = new JButton("Ανανέωση");

        addBtn.addActionListener(e -> onAdd());
        editBtn.addActionListener(e -> onEdit());
        fireBtn.addActionListener(e -> onFire());
        viewBtn.addActionListener(e -> onView());
        refreshBtn.addActionListener(e -> refreshTable());

        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(viewBtn);
        panel.add(fireBtn);
        panel.add(refreshBtn);

        return panel;
    }

    private void onAdd() {
        EmployeeDialog dialog = new EmployeeDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                "Πρόσληψη Υπαλλήλου",false);

        dialog.setVisible(true);

        if (dialog.isConfirmed()) {

            String selectedRole = dialog.getCategory(); 
            String fullCategory = "PERMANENT " + selectedRole;

            boolean success = controller.addEmployee(
                    dialog.getNameValue(),
                    dialog.getMaritalStatus(),
                    dialog.getNumChildren(),
                    dialog.getChildrenNames(),
                    dialog.getChildrenAges(),
                    fullCategory,
                    dialog.getDepartment(),
                    dialog.getStartDate(),
                    dialog.getAddress(),
                    dialog.getPhone(),
                    dialog.getBankName(),
                    dialog.getIban()
            );

            if (success) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Επιτυχής Πρόσληψη!");
            } else {
                JOptionPane.showMessageDialog(this, "Σφάλμα κατά την πρόσληψη.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onEdit() {
        if (!isRowSelected()) return;

        int row = table.getSelectedRow();
        int id = (int) model.getValueAt(row, 0); 
        EmployeeDialog dialog = new EmployeeDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Επεξεργασία",false);
        
        dialog.setFields(
                (String) model.getValueAt(row, 1),
                (String) model.getValueAt(row, 2),
                (int) model.getValueAt(row, 3),
                (String) model.getValueAt(row, 4),
                (String) model.getValueAt(row, 5),
                (String) model.getValueAt(row, 6),
                (String) model.getValueAt(row, 7),
                model.getValueAt(row, 8).toString(), 
                (String) model.getValueAt(row, 9),
                (String) model.getValueAt(row, 10),
                (String) model.getValueAt(row, 11),
                (String) model.getValueAt(row, 12)
        );

        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            boolean success = controller.updateEmployee(
                id,
                dialog.getNameValue(),
                dialog.getMaritalStatus(),
                dialog.getNumChildren(),
                dialog.getChildrenNames(),
                dialog.getChildrenAges(),
                dialog.getCategory(),
                dialog.getDepartment(),
                dialog.getStartDate(),
                dialog.getAddress(),
                dialog.getPhone(),
                dialog.getBankName(),
                dialog.getIban()
            );

            if (success) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Επιτυχής Ενημέρωση!");
            }
        }
    }

    private void onView() {
        if (!isRowSelected()) return;
        int row = table.getSelectedRow();

        EmployeeDialog dialog = new EmployeeDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                "Προβολή",
                true 
        );

        dialog.setFields(
                (String) model.getValueAt(row, 1),
                (String) model.getValueAt(row, 2),
                (int) model.getValueAt(row, 3),
                (String) model.getValueAt(row, 4),
                (String) model.getValueAt(row, 5),
                (String) model.getValueAt(row, 6),
                (String) model.getValueAt(row, 7),
                model.getValueAt(row, 8).toString(),
                (String) model.getValueAt(row, 9),
                (String) model.getValueAt(row, 10),
                (String) model.getValueAt(row, 11),
                (String) model.getValueAt(row, 12)
        );

        dialog.setVisible(true);
    }

    private void onFire() {
        if (!isRowSelected()) return;
        int row = table.getSelectedRow();
        int id = (int) model.getValueAt(row, 0);

        /*
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        if (!today.equals(lastDayOfMonth)) { ... }
        */

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Είσαι σίγουρος για την απόλυση του υπαλλήλου;",
                "Επιβεβαίωση Απόλυσης",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = controller.fireEmployee(id);
            
            if (success) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Ο υπάλληλος απολύθηκε. Η μισθοδοσία για το μήνα καταβλήθηκε.");
            }
        }
    }

    private boolean isRowSelected() {
        if (table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this,
                "Παρακαλώ επίλεξε υπάλληλο",
                "Σφάλμα",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}