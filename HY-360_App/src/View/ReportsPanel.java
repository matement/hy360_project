package View;

import javax.swing.*;
import java.awt.*;

public class ReportsPanel extends JPanel {

    public ReportsPanel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Αναφορές & Ερωτήσεις", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));

        add(title, BorderLayout.NORTH);

        add(new JLabel("Στατιστικά & Ερωτήματα Βάσης Δεδομένων",
                SwingConstants.CENTER),
                BorderLayout.CENTER);
    }
}
