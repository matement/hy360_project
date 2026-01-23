package View;

import javax.swing.*;

import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    public MainFrame() {
        setTitle("HY-360 – Payroll System");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);
        add(createContentPanel(), BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new GridLayout(4, 1, 0, 10));
        sidebar.setPreferredSize(new Dimension(180, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JButton empBtn = new JButton("Υπάλληλοι");
        JButton conBtn = new JButton("Συμβάσεις");
        JButton payBtn = new JButton("Μισθοδοσία");
        JButton repBtn = new JButton("Αναφορές");

        empBtn.addActionListener(e -> cardLayout.show(contentPanel, "EMP"));
        conBtn.addActionListener(e -> cardLayout.show(contentPanel, "CON"));
        payBtn.addActionListener(e -> cardLayout.show(contentPanel, "PAY"));
        repBtn.addActionListener(e -> cardLayout.show(contentPanel, "REP"));

        sidebar.add(empBtn);
        sidebar.add(conBtn);
        sidebar.add(payBtn);
        sidebar.add(repBtn);

        return sidebar;
    }

    private JPanel createContentPanel() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(new EmployeePanel(), "EMP");
        contentPanel.add(new ContractPanel(), "CON");
        contentPanel.add(new PayrollPanel(), "PAY");
        contentPanel.add(new ReportsPanel(), "REP");

        cardLayout.show(contentPanel, "EMP"); // start with Employees panel
        return contentPanel;
    }
}
