package view;

import model.User;
import repository.UserRepository;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final UserRepository repo = new UserRepository();

    public LoginFrame() {

        setTitle("Healthcare System - Login");
        setSize(350, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();

        JButton login = new JButton("Login");
        JButton signup = new JButton("Sign Up (Patient)");

        JPanel p = new JPanel(new GridLayout(0, 2, 6, 6));
        p.add(new JLabel("Username"));
        p.add(user);
        p.add(new JLabel("Password"));
        p.add(pass);
        p.add(login);
        p.add(signup);

        add(p);

        login.addActionListener(e -> {

            User u = repo.authenticate(
                    user.getText(),
                    new String(pass.getPassword()));

            if (u == null) {
                JOptionPane.showMessageDialog(this,
                        "Invalid credentials",
                        "Login failed",
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
            p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
          

        p.add(new JLabel("Username"));
        p.add(user);
        p.add(new JLabel("Password"));
        p.add(pass);
        p.add(new JLabel("Patient ID (P001)"));
        p.add(patientId);

        int ok = JOptionPane.showConfirmDialog(
                this, p, "Patient Signup",
                JOptionPane.OK_CANCEL_OPTION);

        if (ok != JOptionPane.OK_OPTION)
            return;

        if (!patientId.getText().matches("P\\d{3}")) {
            JOptionPane.showMessageDialog(this,
                    "Invalid patient ID",
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
            JOptionPane.showMessageDialog(this, "Signup successful");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
