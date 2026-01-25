package View;

import Controller.controller; 
import JDBC.DBConnection; 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ReportsPanel extends JPanel {

    private controller controller;
    private JTable predefinedTable;
    private DefaultTableModel predefinedModel;
    private JComboBox<String> reportSelector;
    private JTextField paramField;
    private JLabel paramLabel;
    private JTextArea sqlArea;
    private JTable sqlTable;
    private DefaultTableModel sqlModel;

    public ReportsPanel(controller controller) {
        this.controller = controller; 

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Έτοιμες Αναφορές", createPredefinedTab());
        tabbedPane.addTab("SQL Console (Advanced)", createSqlTab());
        
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createPredefinedTab() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Επιλογή Αναφοράς"));

        String[] reports = {
            "1. Ιστορικό Πληρωμών Υπαλλήλου (ID)",
            "2. Στατιστικά Μισθών ανά Κατηγορία (Min/Max/Avg)",
            "3. Συνολικό Κόστος Μισθοδοσίας (Μήνας/Έτος)",
            "4. Ανάλυση Κόστους ανά Ρόλο (Μήνας/Έτος)"
        };

        reportSelector = new JComboBox<>(reports);
        paramLabel = new JLabel("ID Υπαλλήλου:");
        paramField = new JTextField(10);
        JButton runBtn = new JButton("Εκτέλεση");

        reportSelector.addActionListener(e -> {
            int idx = reportSelector.getSelectedIndex();
            paramField.setText("");
            paramField.setEnabled(true);
            
            if (idx == 0) { 
                paramLabel.setText("ID Υπαλλήλου:");
            } else if (idx == 1) { 
                paramLabel.setText("(Δεν απαιτείται)");
                paramField.setEnabled(false);
            } else { 
                paramLabel.setText("Ημ/νία (MM/YYYY):");
                int m = LocalDate.now().getMonthValue();
                int y = LocalDate.now().getYear();
                paramField.setText(m + "/" + y);
            }
        });

        runBtn.addActionListener(e -> executePredefinedQuery());

        selectionPanel.add(new JLabel("Αναφορά:"));
        selectionPanel.add(reportSelector);
        selectionPanel.add(paramLabel);
        selectionPanel.add(paramField);
        selectionPanel.add(runBtn);

        predefinedModel = new DefaultTableModel();
        predefinedTable = new JTable(predefinedModel);
        
        mainPanel.add(selectionPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(predefinedTable), BorderLayout.CENTER);

        return mainPanel;
    }

    private void executePredefinedQuery() {
        int idx = reportSelector.getSelectedIndex();
        String input = paramField.getText().trim();
        
        predefinedModel.setRowCount(0);
        predefinedModel.setColumnCount(0);

        try {
            switch (idx) {
                case 0: 
                    if (input.isEmpty()) { JOptionPane.showMessageDialog(this, "Δώσε ID!"); return; }
                    int empId = Integer.parseInt(input);
                    List<String> history = controller.getPaymentHistory(empId);
                    
                    setPredefinedColumns("Ημερομηνία", "Ποσό");
                    for (String record : history) {
                        try {
                            String[] parts = record.split("\\|");
                            String datePart = parts[0].replace("Date:", "").trim();
                            String amountPart = parts[1].replace("Amount:", "").trim();
                            predefinedModel.addRow(new Object[]{datePart, amountPart});
                        } catch (Exception e) {
                            predefinedModel.addRow(new Object[]{record, ""});
                        }
                    }
                    break;

                case 1: 
                    Map<String, String> stats = controller.getSalaryStatsPerRole();
                    setPredefinedColumns("Ρόλος", "Στατιστικά (Max | Min | Avg)");
                    for (Map.Entry<String, String> entry : stats.entrySet()) {
                        predefinedModel.addRow(new Object[]{ entry.getKey(), entry.getValue() });
                    }
                    break;

                case 2: 
                    int[] date1 = parseDate(input);
                    double total = controller.getTotalPayrollCost(date1[0], date1[1]);
                    setPredefinedColumns("Περίοδος", "Συνολικό Κόστος");
                    predefinedModel.addRow(new Object[]{ input, String.format("%.2f €", total) });
                    break;

                case 3: 
                    int[] date2 = parseDate(input);
                    Map<String, Double> costs = controller.getTotalCostPerRole(date2[0], date2[1]);
                    setPredefinedColumns("Ρόλος", "Κόστος");
                    for (Map.Entry<String, Double> entry : costs.entrySet()) {
                        predefinedModel.addRow(new Object[]{ entry.getKey(), String.format("%.2f €", entry.getValue()) });
                    }
                    break;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Σφάλμα: " + ex.getMessage());
        }
    }

    private JPanel createSqlTab() {
        JPanel panel = new JPanel(new BorderLayout(5,5));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel topPanel = new JPanel(new BorderLayout(5,5));
        topPanel.add(new JLabel("Εισάγετε εντολή SQL (Μόνο SELECT):"), BorderLayout.NORTH);
        
        sqlArea = new JTextArea(4, 40);
        sqlArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        topPanel.add(new JScrollPane(sqlArea), BorderLayout.CENTER);
        
        JButton execBtn = new JButton("Execute SQL");
        execBtn.addActionListener(e -> executeRawSql());
        topPanel.add(execBtn, BorderLayout.SOUTH);

        sqlModel = new DefaultTableModel();
        sqlTable = new JTable(sqlModel);
        sqlTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(sqlTable), BorderLayout.CENTER);

        return panel;
    }

    private void executeRawSql() {
        String query = sqlArea.getText().trim();
        if (query.isEmpty()) return;

        if (!query.toLowerCase().startsWith("select")) {
            JOptionPane.showMessageDialog(this, "Επιτρέπονται μόνο εντολές SELECT!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        sqlModel.setRowCount(0);
        sqlModel.setColumnCount(0);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            Vector<String> columnNames = new Vector<>();
            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnLabel(column));
            }
            sqlModel.setColumnIdentifiers(columnNames);

            while (rs.next()) {
                Vector<Object> rowData = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.add(rs.getObject(i));
                }
                sqlModel.addRow(rowData);
            }

            if (sqlModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Δεν βρέθηκαν αποτελέσματα.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "SQL Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setPredefinedColumns(String... cols) {
        for (String col : cols) {
            predefinedModel.addColumn(col);
        }
    }

    private int[] parseDate(String input) {
        String[] parts = input.split("/");
        if (parts.length != 2) throw new NumberFormatException();
        int m = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        return new int[]{m, y};
    }
}