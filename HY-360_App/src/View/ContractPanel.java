package View;

import javax.swing.*;
import java.awt.*;

public class ContractPanel extends JPanel {
    public ContractPanel() {
        setLayout(new BorderLayout());
        add(createTitle("Συμβάσεις"), BorderLayout.NORTH);
        add(new JLabel("Διαχείριση συμβάσεων", SwingConstants.CENTER),
            BorderLayout.CENTER);
    }

    private JLabel createTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 22f));
        label.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return label;
    }
}

