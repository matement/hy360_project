package View;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;

public class RenewContractDialog extends JDialog {

    private boolean confirmed = false;
    private LocalDate oldEndDate;

    private JComboBox<String> startMonthBox;
    private JSpinner startYearSpinner;
    
    private JComboBox<String> endMonthBox;
    private JSpinner endYearSpinner;
    
    private JTextField salaryField;

    private final String[] months = {
        "Ιανουάριος", "Φεβρουάριος", "Μάρτιος", "Απρίλιος", "Μάιος", "Ιούνιος",
        "Ιούλιος", "Αύγουστος", "Σεπτέμβριος", "Οκτώβριος", "Νοέμβριος", "Δεκέμβριος"
    };

    public RenewContractDialog(JFrame parent, String oldEndStr) {
        super(parent, "Ανανέωση Σύμβασης", true);
        setSize(450, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10,10));

        // Parse παλιά ημερομηνία (αν είναι "---", παίρνουμε τη σημερινή)
        try {
            if (oldEndStr == null || oldEndStr.equals("---")) {
                this.oldEndDate = LocalDate.now();
            } else {
                this.oldEndDate = LocalDate.parse(oldEndStr);
            }
        } catch (Exception e) {
            this.oldEndDate = LocalDate.now();
        }

        add(new JLabel(" Ημερομηνία Λήξης Τρέχουσας Σύμβασης: " + this.oldEndDate, SwingConstants.CENTER), BorderLayout.NORTH);
        add(createForm(), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);
    }

    private JPanel createForm() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // Προεπιλογή: Η νέα σύμβαση ξεκινάει τον επόμενο μήνα
        LocalDate defaultStart = oldEndDate.plusDays(1);
        int currentYear = LocalDate.now().getYear();

        startMonthBox = new JComboBox<>(months);
        startYearSpinner = new JSpinner(new SpinnerNumberModel(currentYear, 2020, 2050, 1));
        
        // Σετάρουμε τις τιμές βάσει της επόμενης μέρας
        startMonthBox.setSelectedIndex(defaultStart.getMonthValue() - 1);
        startYearSpinner.setValue(defaultStart.getYear());

        endMonthBox = new JComboBox<>(months);
        endYearSpinner = new JSpinner(new SpinnerNumberModel(currentYear, 2020, 2050, 1));
        
        // Προεπιλογή λήξης: 6 μήνες μετά
        LocalDate defaultEnd = defaultStart.plusMonths(6);
        endMonthBox.setSelectedIndex(defaultEnd.getMonthValue() - 1);
        endYearSpinner.setValue(defaultEnd.getYear());

        salaryField = new JTextField("1000.0");

        // Panels
        JPanel startP = new JPanel(new GridLayout(1,2,5,0));
        startP.add(startMonthBox);
        startP.add(startYearSpinner);

        JPanel endP = new JPanel(new GridLayout(1,2,5,0));
        endP.add(endMonthBox);
        endP.add(endYearSpinner);

        panel.add(new JLabel("Έναρξη Νέας (Μήνας/Έτος):"));
        panel.add(startP);
        
        panel.add(new JLabel("Λήξη Νέας (Μήνας/Έτος):"));
        panel.add(endP);
        
        panel.add(new JLabel("Ποσό Σύμβασης (€):"));
        panel.add(salaryField);

        return panel;
    }

    private JPanel createButtons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        ok.addActionListener(e -> onConfirm());
        cancel.addActionListener(e -> dispose());

        p.add(ok);
        p.add(cancel);
        return p;
    }

    private void onConfirm() {
        LocalDate newStart = getSelectedStartDate();
        LocalDate newEnd = getSelectedEndDate();

        // 1. Έλεγχος: Η νέα έναρξη πρέπει να είναι ΜΕΤΑ την παλιά λήξη
        if (!newStart.isAfter(oldEndDate)) {
            JOptionPane.showMessageDialog(this, 
                "Η νέα σύμβαση πρέπει να ξεκινάει μετά τη λήξη της παλιάς (" + oldEndDate + ")",
                "Σφάλμα Ημερομηνίας", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Έλεγχος: Η λήξη πρέπει να είναι μετά την έναρξη
        if (newEnd.isBefore(newStart)) {
            JOptionPane.showMessageDialog(this, "Η ημερομηνία λήξης είναι πριν την έναρξη!", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Double.parseDouble(salaryField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Λάθος ποσό.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            return;
        }

        confirmed = true;
        dispose();
    }

    private LocalDate getSelectedStartDate() {
        int m = startMonthBox.getSelectedIndex() + 1;
        int y = (int) startYearSpinner.getValue();
        return LocalDate.of(y, m, 1);
    }

    private LocalDate getSelectedEndDate() {
        int m = endMonthBox.getSelectedIndex() + 1;
        int y = (int) endYearSpinner.getValue();
        return YearMonth.of(y, m).atEndOfMonth();
    }

    public boolean isConfirmed() { return confirmed; }
    public java.sql.Date getStartDate() { return java.sql.Date.valueOf(getSelectedStartDate()); }
    public java.sql.Date getEndDate() { return java.sql.Date.valueOf(getSelectedEndDate()); }
    public double getSalary() { return Double.parseDouble(salaryField.getText()); }
}