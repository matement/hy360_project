package View;

import javax.swing.*;
import java.awt.*;

public class ContractPanel extends JPanel {

    public ContractPanel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Διαχείριση Συμβάσεων", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));

        add(title, BorderLayout.NORTH);

        add(new JLabel("Σύναψη & Ανανέωση Συμβάσεων", SwingConstants.CENTER),
            BorderLayout.CENTER);
    }
}
