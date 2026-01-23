package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmployeePanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    public EmployeePanel() {
        setLayout(new BorderLayout());
        add(createTitle(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JLabel createTitle() {
        JLabel label = new JLabel("Διαχείριση Υπαλλήλων", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 22f));
        label.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        return label;
    }

    private JScrollPane createTablePanel() {
        String[] columns = {
            "ID","Όνομα","Οικ. Κατ.","Παιδιά","Ηλικίες Παιδιών","Κατηγορία","Τμήμα",
            "Ημερ. Έναρξης","Διεύθυνση","Τηλέφωνο","Τράπεζα","Αρ. Λογαριασμού"
        };

        Object[][] data = {
            {1,"Γιώργος Παπαδόπουλος","Έγγαμος",2,"5,8","Μόνιμος Διοικητικός","Πληροφορική",
             "01/09/2018","Ηρ. Πολυτεχνείου 10","6971234567","Alpha Bank","1234567890"}
        };

        tableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        return new JScrollPane(table);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,15,10));

        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");

        addBtn.addActionListener(e -> {
            EmployeeDialog dialog = new EmployeeDialog((JFrame) SwingUtilities.getWindowAncestor(this),"Add Employee");
            dialog.setVisible(true);
            if(dialog.isConfirmed()) {
                Object[] row = {
                    tableModel.getRowCount()+1,
                    dialog.getNameValue(),
                    dialog.getMaritalStatus(),
                    dialog.getNumChildren(),
                    dialog.getChildrenAges(),
                    dialog.getCategory(),
                    dialog.getDepartment(),
                    dialog.getStartDate(),
                    dialog.getAddress(),
                    dialog.getPhone(),
                    dialog.getBankName(),
                    dialog.getBankAccount()
                };
                tableModel.addRow(row);
            }
        });

        editBtn.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if(selected==-1) { JOptionPane.showMessageDialog(this,"Επίλεξε υπάλληλο για επεξεργασία!"); return; }

            EmployeeDialog dialog = new EmployeeDialog((JFrame) SwingUtilities.getWindowAncestor(this),"Edit Employee");
            dialog.setFields(
                (String)tableModel.getValueAt(selected,1),
                (String)tableModel.getValueAt(selected,2),
                (Integer)tableModel.getValueAt(selected,3),
                (String)tableModel.getValueAt(selected,4),
                (String)tableModel.getValueAt(selected,5),
                (String)tableModel.getValueAt(selected,6),
                (String)tableModel.getValueAt(selected,7),
                (String)tableModel.getValueAt(selected,8),
                (String)tableModel.getValueAt(selected,9),
                (String)tableModel.getValueAt(selected,10),
                (String)tableModel.getValueAt(selected,11)
            );
            dialog.setVisible(true);
            if(dialog.isConfirmed()) {
                tableModel.setValueAt(dialog.getNameValue(),selected,1);
                tableModel.setValueAt(dialog.getMaritalStatus(),selected,2);
                tableModel.setValueAt(dialog.getNumChildren(),selected,3);
                tableModel.setValueAt(dialog.getChildrenAges(),selected,4);
                tableModel.setValueAt(dialog.getCategory(),selected,5);
                tableModel.setValueAt(dialog.getDepartment(),selected,6);
                tableModel.setValueAt(dialog.getStartDate(),selected,7);
                tableModel.setValueAt(dialog.getAddress(),selected,8);
                tableModel.setValueAt(dialog.getPhone(),selected,9);
                tableModel.setValueAt(dialog.getBankName(),selected,10);
                tableModel.setValueAt(dialog.getBankAccount(),selected,11);
            }
        });

        deleteBtn.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if(selected==-1) { JOptionPane.showMessageDialog(this,"Επίλεξε υπάλληλο για διαγραφή!"); return; }
            tableModel.removeRow(selected);
        });

        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);

        return panel;
    }
}
