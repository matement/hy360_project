package View;

import JDBC.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class SearchPanel extends JPanel {

    private JTextField nameField;
    private JComboBox<String> roleBox;
    private JComboBox<String> deptBox;
    private JCheckBox marriedCheck;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public SearchPanel() {
        setLayout(new BorderLayout(10, 10));

        // --- 1. Φίλτρα Αναζήτησης ---
        JPanel filtersPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        filtersPanel.setBorder(BorderFactory.createTitledBorder("Κριτήρια Αναζήτησης (Αυθαίρετες Ερωτήσεις)"));

        nameField = new JTextField();
        roleBox = new JComboBox<>(new String[]{"- Όλα -", "TEACHING", "ADMINISTRATIVE"});
        deptBox = new JComboBox<>(new String[]{
                "- Όλα -",
                "Mathematics and Applied Mathematics",
                "Physics",
                "Computer Science",
                "Biology",
                "Chemistry",
                "Materials Science and Technology",
                "Medicine",
                "Philology",
                "History and Archaeology",
                "Philosophy",
                "Primary Education",
                "Preschool Education",
                "Sociology",
                "Economics",
                "Political Science",
                "Psychology"
        });        marriedCheck = new JCheckBox("Μόνο Έγγαμοι");

        filtersPanel.add(new JLabel("Όνομα (περιέχει):"));
        filtersPanel.add(nameField);
        filtersPanel.add(new JLabel("Ρόλος:"));
        filtersPanel.add(roleBox);
        filtersPanel.add(new JLabel("Τμήμα:"));
        filtersPanel.add(deptBox);
        filtersPanel.add(new JLabel("Οικ. Κατάσταση:"));
        filtersPanel.add(marriedCheck);

        JButton searchBtn = new JButton("Αναζήτηση");
        searchBtn.addActionListener(e -> performSearch());

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(filtersPanel, BorderLayout.CENTER);
        topContainer.add(searchBtn, BorderLayout.SOUTH);

        add(topContainer, BorderLayout.NORTH);

        // --- 2. Πίνακας Αποτελεσμάτων ---
        tableModel = new DefaultTableModel(new String[]{"ID", "Όνομα", "Τμήμα", "Ρόλος", "Τύπος"}, 0);
        resultTable = new JTable(tableModel);
        add(new JScrollPane(resultTable), BorderLayout.CENTER);
    }

    private void performSearch() {
        tableModel.setRowCount(0); // Καθαρισμός προηγούμενων

        // Δυναμικό χτίσιμο του SQL Query
        StringBuilder sql = new StringBuilder(
                "SELECT e.idEmployee, e.name, d.department_name, r.role_name, et.type_name " +
                        "FROM Employee e " +
                        "JOIN Department d ON e.Department_departmentID = d.departmentID " +
                        "JOIN Role r ON e.Role_roleID = r.roleID " +
                        "JOIN Employment_type et ON e.Employment_type_typeID = et.typeID " +
                        "WHERE 1=1 "); // Το 1=1 βοηθάει να κολλάμε τα AND από κάτω

        // Λίστα για να κρατάμε τις τιμές των παραμέτρων
        java.util.List<Object> params = new java.util.ArrayList<>();

        // Φίλτρο Ονόματος
        if (!nameField.getText().trim().isEmpty()) {
            sql.append("AND e.name LIKE ? ");
            params.add("%" + nameField.getText().trim() + "%");
        }

        // Φίλτρο Ρόλου
        if (roleBox.getSelectedIndex() > 0) {
            sql.append("AND r.role_name = ? ");
            params.add(roleBox.getSelectedItem());
        }

        // Φίλτρο Τμήματος
        if (deptBox.getSelectedIndex() > 0) {
            sql.append("AND d.department_name = ? ");
            params.add(deptBox.getSelectedItem());
        }

        // Φίλτρο Έγγαμου
        if (marriedCheck.isSelected()) {
            sql.append("AND e.is_Married = 1 ");
        }

        // Εκτέλεση
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // Γέμισμα των ? με τις τιμές
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("idEmployee"));
                row.add(rs.getString("name"));
                row.add(rs.getString("department_name"));
                row.add(rs.getString("role_name"));
                row.add(rs.getString("type_name"));
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Σφάλμα αναζήτησης: " + e.getMessage());
        }
    }
}