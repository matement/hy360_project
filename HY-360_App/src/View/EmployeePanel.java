package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmployeePanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public EmployeePanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(createTitle(), BorderLayout.NORTH);
        add(createTable(), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);
    }

    private JLabel createTitle() {
        JLabel label = new JLabel("Διαχείριση Υπαλλήλων", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 22f));
        return label;
    }

    private JScrollPane createTable() {

        String[] columns = {
            "ID",                   // 0
            "Όνομα",                // 1
            "Οικ. Κατάσταση",       // 2
            "Παιδιά",               // 3
            "Ονόματα Παιδιών",      // 4 
            "Ηλικίες Παιδιών",      // 5
            "Κατηγορία",            // 6
            "Τμήμα",                // 7
            "Ημ. Έναρξης",          // 8
            "Διεύθυνση",            // 9
            "Τηλέφωνο",             // 10
            "Τράπεζα",              // 11
            "Αρ. Λογαριασμού",      // 12
            "Κατάσταση"             // 13
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        // ΕΝΔΕΙΚΤΙΚΑ ΔΕΔΟΜΕΝΑ
        model.addRow(new Object[]{
            1,
            "Γιώργος Παπαδόπουλος",
            "Έγγαμος",
            2,
            "Μαρία, Γιάννης",
            "5, 8",
            "Διοικητικός",
            "Πληροφορική",
            "2026-09-01",
            "Ηρ. Πολυτεχνείου 10",
            "6971234567",
            "Alpha Bank",
            "GR16011012500000000123",
            "Ενεργός"
        });

        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        return new JScrollPane(table);
    }

    private JPanel createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,15,10));

        JButton addBtn = new JButton("Πρόσληψη");
        JButton editBtn = new JButton("Επεξεργασία");
        JButton fireBtn = new JButton("Απόλυση/Συνταξιοδότηση");
        JButton viewBtn = new JButton("Προβολή");

        addBtn.addActionListener(e -> onAdd());
        editBtn.addActionListener(e -> onEdit());
        fireBtn.addActionListener(e -> onFire());
        viewBtn.addActionListener(e -> onView());

        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(viewBtn);
        panel.add(fireBtn);

        return panel;
    }

    private void onAdd() {
        EmployeeDialog dialog = new EmployeeDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                "Πρόσληψη Υπαλλήλου",false);

        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            model.addRow(new Object[] {
                    model.getRowCount() + 1,
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
                    dialog.getIban(),
                    "Ενεργός"
            });
        }
    }

    private void onEdit() {
        if (!isRowSelected()) return;

        int row = table.getSelectedRow();

        EmployeeDialog dialog = new EmployeeDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Επεξεργασία",false);
        
        dialog.setFields(
                (String) model.getValueAt(row, 1),  // Name
                (String) model.getValueAt(row, 2),  // Marital
                (int) model.getValueAt(row, 3),     // Count
                (String) model.getValueAt(row, 4),  // Names 
                (String) model.getValueAt(row, 5),  // Ages
                (String) model.getValueAt(row, 6),  // Category
                (String) model.getValueAt(row, 7),  // Dept
                (String) model.getValueAt(row, 8),  // Date
                (String) model.getValueAt(row, 9),  // Address
                (String) model.getValueAt(row, 10), // Phone
                (String) model.getValueAt(row, 11), // Bank
                (String) model.getValueAt(row, 12)  // IBAN
        );

        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            model.setValueAt(dialog.getNameValue(), row, 1);
            model.setValueAt(dialog.getMaritalStatus(), row, 2);
            model.setValueAt(dialog.getNumChildren(), row, 3);
            model.setValueAt(dialog.getChildrenNames(), row, 4);
            model.setValueAt(dialog.getChildrenAges(), row, 5);
            model.setValueAt(dialog.getCategory(), row, 6);
            model.setValueAt(dialog.getDepartment(), row, 7);
            model.setValueAt(dialog.getStartDate(), row, 8);
            model.setValueAt(dialog.getAddress(), row, 9);
            model.setValueAt(dialog.getPhone(), row, 10);
            model.setValueAt(dialog.getBankName(), row, 11);
            model.setValueAt(dialog.getIban(), row, 12);
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
                (String) model.getValueAt(row, 8),
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

        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        
        // Uncomment this check for final:
        /*
        if (!today.equals(lastDayOfMonth)) {
            JOptionPane.showMessageDialog(this,
                    "Η απόλυση μπορεί να γίνει μόνο την τελευταία μέρα του μήνα",
                    "Προειδοποίηση",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        */

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Είσαι σίγουρος για την απόλυση του υπαλλήλου;",
                "Επιβεβαίωση Απόλυσης",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {

            model.setValueAt("Απολυμένος", row, 13); 

            JOptionPane.showMessageDialog(this,
                    "Ο υπάλληλος απολύθηκε. Η μισθοδοσία για το μήνα καταβλήθηκε.");
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