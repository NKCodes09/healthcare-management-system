import javax.swing.*;
import java.awt.*;
import view.LoginFrame;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

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

            new LoginFrame().setVisible(true);
        });
    }
}
