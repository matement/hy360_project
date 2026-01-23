package View;

import javax.swing.*;
import java.awt.*;

public class EmployeePanel extends JPanel {
    public EmployeePanel() {
        setLayout(new BorderLayout());
        add(createTitle("Διαχείριση Υπαλλήλων"), BorderLayout.NORTH);
        add(new JLabel("Εδώ θα μπουν φόρμες υπαλλήλων", SwingConstants.CENTER),
            BorderLayout.CENTER);
    }

    private JLabel createTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 22f));
        label.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return label;
    }
}
