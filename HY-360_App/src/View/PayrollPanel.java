package View;

import javax.swing.*;
import java.awt.*;

public class PayrollPanel extends JPanel {
    public PayrollPanel() {
        setLayout(new BorderLayout());
        add(createTitle("Μισθοδοσία"), BorderLayout.NORTH);
        add(new JLabel("Καταβολή μισθοδοσίας", SwingConstants.CENTER),
            BorderLayout.CENTER);
    }

    private JLabel createTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 22f));
        label.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return label;
    }
}

