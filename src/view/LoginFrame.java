package view;

import model.User;
import repository.UserRepository;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final UserRepository repo = new UserRepository();

    public LoginFrame() {

        setTitle("Healthcare Management System – Login");
        setSize(380, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();

        JButton login = new JButton("Login");
        JButton signup = new JButton("Sign Up (Patient)");

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("Username"), c);

        c.gridx = 1;
        panel.add(username, c);

        c.gridx = 0;
        c.gridy = 1;
        panel.add(new JLabel("Password"), c);

        c.gridx = 1;
        panel.add(password, c);

        c.gridx = 0;
        c.gridy = 2;
        panel.add(login, c);

        c.gridx = 1;
        panel.add(signup, c);

        add(panel);

        login.addActionListener(e -> {

            User u = repo.authenticate(
                    username.getText(),
                    new String(password.getPassword()));

            if (u == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            dispose();
            new MainFrame(u).setVisible(true);
        });

        signup.addActionListener(e -> signup());
    }

    private void signup() {

        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();
        JTextField patientId = new JTextField();

        JPanel p = new JPanel(new GridLayout(0, 2, 6, 6));
        p.add(new JLabel("Username"));
        p.add(user);
        p.add(new JLabel("Password"));
        p.add(pass);
        p.add(new JLabel("Patient ID (P001)"));
        p.add(patientId);

        if (JOptionPane.showConfirmDialog(
                this, p, "Patient Signup",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
            return;

        if (!patientId.getText().matches("P\\d{3}")) {
            JOptionPane.showMessageDialog(this,
                    "Invalid patient ID format",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            repo.add(new User(
                    user.getText(),
                    new String(pass.getPassword()),
                    "PATIENT",
                    patientId.getText()));
            JOptionPane.showMessageDialog(this,
                    "Signup successful");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage());
        }
    }
}
