package App;

import com.formdev.flatlaf.FlatDarkLaf; // Βιβλιοθήκη για το Look & Feel
import Controller.controller;
import View.MainFrame;

import javax.swing.*;

public class PayrollApp {

    public static void main(String[] args) {
        // 1. Ρύθμιση Εμφάνισης (Dark Theme)
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF. Using default.");
        }

        // 2. Εκκίνηση της Εφαρμογής στο Event Thread
        SwingUtilities.invokeLater(() -> {
            // Προαιρετικό: Αν θες να καθαρίζεις τη βάση σε κάθε επανεκκίνηση,
            // μπορείς να φτιάξεις έναν controller εδώ και να καλέσεις initializeDatabase().
            /*
            controller ctrl = new controller();
            ctrl.initializeDatabase();
            */
            
            // Άνοιγμα του Παραθύρου
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}