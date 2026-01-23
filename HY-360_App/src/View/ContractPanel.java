package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ContractPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public ContractPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(createTitle(), BorderLayout.NORTH);
        add(createTable(), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);
    }

    private JLabel createTitle() {
        JLabel label = new JLabel("Διαχείριση Συμβάσεων", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 22f));
        return label;
    }

    private JScrollPane createTable() {

        String[] columns = {
            "ID",
            "Όνομα",
            "Οικ. Κατάσταση",
            "Παιδιά",
            "Ηλικίες Παιδιών",
            "Κατηγορία",
            "Τμήμα",
            "Ημ. Έναρξης",
            "Ημ. Λήξης",
            "Διεύθυνση",
            "Τηλέφωνο",
            "Τράπεζα",
            "Αρ. Λογαριασμού",
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        // Ενδεικτικά δεδομένα
        model.addRow(new Object[]{
            1,
            "Αλέξανδρος Βίτσος",
            "Άγγαμος",
            2,
            "5, 8",
            "Διοικητικός",
            "Πληροφορική",
            "2026-09-01",
            "2026-10-31",
            "Ηρ. Πολυτεχνείου 10",
            "6971234567",
            "Alpha Bank",
            "GR16011012500000000123",
        });

        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        return new JScrollPane(table);
    }

    private JPanel createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,15,10));

        JButton addBtn = new JButton("Πρόσληψη");
        JButton editBtn = new JButton("Επεξεργασία");
        JButton refrBtn = new JButton("Ανανέωση");
        JButton viewBtn = new JButton("Προβολή");

        addBtn.addActionListener(e -> onAdd());
        editBtn.addActionListener(e -> onEdit());
        refrBtn.addActionListener(e -> onRefr());
        viewBtn.addActionListener(e -> onView());

        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(viewBtn);
        panel.add(refrBtn);

        return panel;
    }

    private void onAdd() {
        ContractDialog d = new ContractDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                "Νέα Σύμβαση",
                false);

        d.setVisible(true);

        if (d.isConfirmed()) {
            model.addRow(new Object[]{
                    model.getRowCount()+1,
                    d.getName(),
                    d.getMarital(),
                    d.getChildren(),
                    d.getAges(),
                    d.getCategory(),
                    d.getDepartment(),
                    d.getStart(),
                    d.getEnd(),
                    d.getAddress(),
                    d.getPhone(),
                    d.getBank(),
                    d.getIban()
            });
        }
    }

    private void onEdit() {
        if (!isRowSelected()) return;

        int row = table.getSelectedRow();

        ContractDialog d = new ContractDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                "Επεξεργασία",
                false
        );

        // Δίνουμε σωστά και τις 12 παραμέτρους, συμπεριλαμβανομένου του End Date
        d.setFields(
                (String) model.getValueAt(row, 1),  // Name
                (String) model.getValueAt(row, 2),  // Marital
                (int) model.getValueAt(row, 3),     // Children
                (String) model.getValueAt(row, 4),  // Ages
                (String) model.getValueAt(row, 5),  // Category
                (String) model.getValueAt(row, 6),  // Department
                (String) model.getValueAt(row, 7),  // Start
                (String) model.getValueAt(row, 8),  // End
                (String) model.getValueAt(row, 9),  // Address
                (String) model.getValueAt(row, 10), // Phone
                (String) model.getValueAt(row, 11), // Bank
                (String) model.getValueAt(row, 12)  // IBAN
        );

        d.setVisible(true);

        if (d.isConfirmed()) {
            model.setValueAt(d.getName(), row, 1);
            model.setValueAt(d.getMarital(), row, 2);
            model.setValueAt(d.getChildren(), row, 3);
            model.setValueAt(d.getAges(), row, 4);
            model.setValueAt(d.getCategory(), row, 5);
            model.setValueAt(d.getDepartment(), row, 6);
            model.setValueAt(d.getStart(), row, 7);
            model.setValueAt(d.getEnd(), row, 8);
            model.setValueAt(d.getAddress(), row, 9);
            model.setValueAt(d.getPhone(), row, 10);
            model.setValueAt(d.getBank(), row, 11);
            model.setValueAt(d.getIban(), row, 12);
        }
    }

    private void onView() {
        if (!isRowSelected()) return;

        int row = table.getSelectedRow();

        ContractDialog d = new ContractDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                "Προβολή",
                true
        );

        d.setFields(
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

        d.setVisible(true);
    }

    private void onRefr() {
        if (!isRowSelected()) return;

        int row = table.getSelectedRow();
        String newEnd = JOptionPane.showInputDialog(
                this,
                "Νέα ημερομηνία λήξης (YYYY-MM-DD):"
        );

        if (newEnd != null && !newEnd.isEmpty()) {
            model.setValueAt(newEnd, row, 8); // End Date
        }
    }

    private boolean isRowSelected() {
        if (table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Παρακαλώ επίλεξε υπάλληλο",
                    "Σφάλμα",
                    JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
        return true;
    }
}
