package View;

import Controller.controller; 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ContractPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private controller controller; 

    public ContractPanel(controller controller) {
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
            
            
            String category = (String) row[6]; 
            
            if (category == null || 
               (!category.contains("CONTRACTOR") && !category.contains("Συμβασιούχος"))) {
                continue; 
            }
            

            Object[] guiRow = new Object[14];
            guiRow[0] = row[0]; // ID
            guiRow[1] = row[1]; // Name
            guiRow[2] = row[2]; // Marital
            guiRow[3] = row[3]; // Kids
            guiRow[4] = row[4]; // Kids Names
            guiRow[5] = row[5]; // Kids Ages
            guiRow[6] = row[6]; // Category
            guiRow[7] = row[7]; // Dept
            guiRow[8] = row[8]; // Start Date
            
            String endDate = controller.getContractEndDate((int) row[0]);
            guiRow[9] = (endDate != null) ? endDate : "---";  
            
            guiRow[10] = row[9]; // Address
            guiRow[11] = row[10]; // Phone
            guiRow[12] = row[11]; // Bank
            guiRow[13] = row[12]; // IBAN

            model.addRow(guiRow);
        }
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
            
            String rawCat = d.getCategory(); 
            String fullCat = "CONTRACTOR " + rawCat; 
            
            boolean success = controller.addEmployee(
                d.getName(), d.getMarital(), d.getChildren(),
                d.getChildrenNames(), d.getAges(), 
                fullCat, 
                d.getDepartment(), d.getStart(), d.getAddress(),
                d.getPhone(), d.getBank(), d.getIban(),
                d.getEnd()
            );

            if (success) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Η σύμβαση αποθηκεύτηκε στη βάση!");
            } else {
                JOptionPane.showMessageDialog(this, "Σφάλμα αποθήκευσης.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onEdit() {
        if (!isRowSelected()) return;

        int row = table.getSelectedRow();
        int id = (int) model.getValueAt(row, 0); 

        ContractDialog d = new ContractDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                "Επεξεργασία",
                false
        );

        d.setFields(
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
                (String) model.getValueAt(row, 12),
                (String) model.getValueAt(row, 13)
        );

       
        d.disableFixedFields(); 
        d.setVisible(true);

        if (d.isConfirmed()) {
            boolean success = controller.updateEmployee(
                id, d.getName(), d.getMarital(), d.getChildren(),
                d.getChildrenNames(), d.getAges(), 
                d.getCategory(),    
                d.getDepartment(),  
                d.getStart(), d.getAddress(),
                d.getPhone(), d.getBank(), d.getIban()
            );
            
            if (success) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Τα στοιχεία ενημερώθηκαν!");
            }
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
                model.getValueAt(row, 8).toString(),
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
        int id = (int) model.getValueAt(row, 0); 

      
        String oldEndStr = (String) model.getValueAt(row, 9);

      
        RenewContractDialog dialog = new RenewContractDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this), 
                oldEndStr
        );
        
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            try {
                
                controller.renewContract(
                    id, 
                    dialog.getStartDate(), 
                    dialog.getEndDate(), 
                    dialog.getSalary()
                );
                
                JOptionPane.showMessageDialog(this, "Η σύμβαση ανανεώθηκε επιτυχώς!");
                refreshTable();
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Σφάλμα κατά την ανανέωση.", "Error", JOptionPane.ERROR_MESSAGE);
            }
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