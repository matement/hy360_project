package View;

import javax.swing.*;
import java.awt.*;

public class PayrollPanel extends JPanel {

    public PayrollPanel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Μισθοδοσία", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));

        add(title, BorderLayout.NORTH);

        add(new JLabel("Καταβολή μισθοδοσίας & μεταβολές μισθών",
                SwingConstants.CENTER),
                BorderLayout.CENTER);
    }
}
