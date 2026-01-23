package App;

import javax.swing.SwingUtilities;
import com.formdev.flatlaf.FlatDarkLaf;
import View.MainFrame;

public class Main {
    public static void main(String[] args) {
        // Setup FlatLaf before creating any GUI components
        FlatDarkLaf.setup();

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
