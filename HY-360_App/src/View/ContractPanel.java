package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
            "ID",                   // 0
            "Όνομα",                // 1
            "Οικ. Κατάσταση",       // 2
            "Παιδιά",               // 3
            "Ονόματα Παιδιών",      // 4 
            "Ηλικίες Παιδιών",      // 5
            "Κατηγορία",            // 6
            "Τμήμα",                // 7
            "Ημ. Έναρξης",          // 8
            "Ημ. Λήξης",            // 9
            "Διεύθυνση",            // 10
            "Τηλέφωνο",             // 11
            "Τράπεζα",              // 12
            "Αρ. Λογαριασμού",      // 13
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        model.addRow(new Object[]{
            1,
            "Αλέξανδρος Βίτσος",
            "Άγαμος",
            2,
            "Νίκος, Ελένη", 
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
        table.getColumnModel().getColumn(4).setPreferredWidth(120); 
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
                    d.getChildrenNames(),
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

        d.setFields(
                (String) model.getValueAt(row, 1),  // Name
                (String) model.getValueAt(row, 2),  // Marital
                (int) model.getValueAt(row, 3),     // Children
                (String) model.getValueAt(row, 4),  // NEW: Names
                (String) model.getValueAt(row, 5),  // Ages
                (String) model.getValueAt(row, 6),  // Category
                (String) model.getValueAt(row, 7),  // Department
                (String) model.getValueAt(row, 8),  // Start
                (String) model.getValueAt(row, 9),  // End 
                (String) model.getValueAt(row, 10), // Address
                (String) model.getValueAt(row, 11), // Phone
                (String) model.getValueAt(row, 12), // Bank
                (String) model.getValueAt(row, 13)  // IBAN
        );

        d.setVisible(true);

        if (d.isConfirmed()) {
            model.setValueAt(d.getName(), row, 1);
            model.setValueAt(d.getMarital(), row, 2);
            model.setValueAt(d.getChildren(), row, 3);
            model.setValueAt(d.getChildrenNames(), row, 4);
            model.setValueAt(d.getAges(), row, 5);
            model.setValueAt(d.getCategory(), row, 6);
            model.setValueAt(d.getDepartment(), row, 7);
            model.setValueAt(d.getStart(), row, 8);
            model.setValueAt(d.getEnd(), row, 9);
            model.setValueAt(d.getAddress(), row, 10);
            model.setValueAt(d.getPhone(), row, 11);
            model.setValueAt(d.getBank(), row, 12);
            model.setValueAt(d.getIban(), row, 13);
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
                (String) model.getValueAt(row, 12),
                (String) model.getValueAt(row, 13)
        );

        d.setVisible(true);
    }

    private void onRefr() {
        if (!isRowSelected()) return;
        int row = table.getSelectedRow();

        try {
            String oldEndStr = (String) model.getValueAt(row, 9);
            LocalDate oldEnd = LocalDate.parse(oldEndStr);

            LocalDate newStart = oldEnd.plusDays(1);

            String newEndStr = JOptionPane.showInputDialog(this,
                "Η τρέχουσα σύμβαση λήγει στις: " + oldEndStr + "\n" +
                "Η νέα σύμβαση θα ξεκινήσει στις: " + newStart + "\n\n" +
                "Παρακαλώ εισάγετε τη ΝΕΑ ημερομηνία λήξης (YYYY-MM-DD):");

            if (newEndStr == null || newEndStr.trim().isEmpty()) return;

            LocalDate newEnd = LocalDate.parse(newEndStr);
            if (newEnd.isBefore(newStart)) {
                JOptionPane.showMessageDialog(this, "Η λήξη δεν μπορεί να είναι πριν την έναρξη!", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                return;
            }

            model.addRow(new Object[]{
                model.getRowCount() + 1,        
                model.getValueAt(row, 1),       
                model.getValueAt(row, 2),       
                model.getValueAt(row, 3),       
                model.getValueAt(row, 4),      
                model.getValueAt(row, 5),       
                model.getValueAt(row, 6),       
                model.getValueAt(row, 7),       
                newStart.toString(),            
                newEnd.toString(),              
                model.getValueAt(row, 10),      
                model.getValueAt(row, 11),      
                model.getValueAt(row, 12),      
                model.getValueAt(row, 13)       
            });

            JOptionPane.showMessageDialog(this, "Η σύμβαση ανανεώθηκε (δημιουργήθηκε νέα εγγραφή).");

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Λάθος μορφή ημερομηνίας (YYYY-MM-DD)", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
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