package View;

import javax.swing.*;
import java.awt.*;

public class ReportsPanel extends JPanel {
    public ReportsPanel() {
        setLayout(new BorderLayout());
        add(createTitle("Αναφορές"), BorderLayout.NORTH);
        add(new JLabel("Στατιστικά & αναφορές", SwingConstants.CENTER),
            BorderLayout.CENTER);
    }

    private JLabel createTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 22f));
        label.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return label;
    }
}
