package App;

import com.formdev.flatlaf.FlatDarkLaf; 
import View.MainFrame;

import javax.swing.*;

public class PayrollApp {

    public static void main(String[] args) {
       
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF. Using default.");
        }

      
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}