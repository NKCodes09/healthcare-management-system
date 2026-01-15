
// Import Swing core components (JFrame, UIManager, SwingUtilities, etc.)
import javax.swing.*;

// Import AWT classes for font styling
import java.awt.*;

// Import the LoginFrame (first screen of the application)
import view.LoginFrame;

/**
 * Main class
 * ----------
 * Entry point of the Healthcare Management System application.
 * 
 * Responsibilities:
 * - Set global UI styling (fonts, look & feel)
 * - Launch the application on the Event Dispatch Thread (EDT)
 * - Display the Login screen before accessing the main system
 */
public class Main {

        /**
         * main method
         * -----------
         * Java program entry point
         */
        public static void main(String[] args) {

                // Ensure all Swing UI code runs on the Event Dispatch Thread (EDT)
                // This prevents UI freezes and threading issues
                SwingUtilities.invokeLater(() -> {

                        try {
                                // Set system-native look and feel (Windows / macOS / Linux)
                                UIManager.setLookAndFeel(
                                                UIManager.getSystemLookAndFeelClassName());
                        } catch (Exception ignored) {
                                // If look and feel fails, Swing will use default
                        }

                        /*
                         * Global UI styling
                         * -----------------
                         * These settings apply consistent fonts across the entire application
                         * without styling each component individually.
                         */

                        UIManager.put("Button.font",
                                        new Font("Segoe UI", Font.PLAIN, 14));

                        UIManager.put("Label.font",
                                        new Font("Segoe UI", Font.PLAIN, 14));

                        UIManager.put("TextField.font",
                                        new Font("Segoe UI", Font.PLAIN, 14));

                        UIManager.put("ComboBox.font",
                                        new Font("Segoe UI", Font.PLAIN, 14));

                        UIManager.put("Table.font",
                                        new Font("Segoe UI", Font.PLAIN, 13));

                        UIManager.put("TableHeader.font",
                                        new Font("Segoe UI", Font.BOLD, 14));

                        /*
                         * Launch the Login screen
                         * -----------------------
                         * The LoginFrame controls access to the system based on user role:
                         * - Patient
                         * - Clinician
                         * - Staff
                         * - Admin
                         *
                         * After successful login, the MainFrame is opened.
                         */
                        new LoginFrame().setVisible(true);
                });
        }
}
