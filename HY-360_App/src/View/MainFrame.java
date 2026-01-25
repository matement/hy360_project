package View;

import Controller.controller; 
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private controller controller; 

    public MainFrame() {
       
        controller = new controller(); 

        setTitle("HY-360 Μισθοδοσία Πανεπιστημίου Κρήτης");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
       
        contentPanel.add(new EmployeePanel(controller), "EMPLOYEES");
        contentPanel.add(new ContractPanel(controller), "CONTRACTS");
        contentPanel.add(new PayrollPanel(controller), "PAYROLL");
        contentPanel.add(new ReportsPanel(controller), "REPORTS"); 

        add(createMenu(), BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JMenuBar createMenu() {
        JMenuBar bar = new JMenuBar();

        JButton empBtn = new JButton("Υπάλληλοι");
        empBtn.addActionListener(e -> cardLayout.show(contentPanel, "EMPLOYEES"));

        JButton conBtn = new JButton("Συμβάσεις");
        conBtn.addActionListener(e -> cardLayout.show(contentPanel, "CONTRACTS"));

        JButton payBtn = new JButton("Μισθοδοσία");
        payBtn.addActionListener(e -> cardLayout.show(contentPanel, "PAYROLL"));

        JButton repBtn = new JButton("Αναφορές");
        repBtn.addActionListener(e -> cardLayout.show(contentPanel, "REPORTS"));

        bar.add(empBtn);
        bar.add(conBtn);
        bar.add(payBtn);
        bar.add(repBtn);

        return bar;
    }
}