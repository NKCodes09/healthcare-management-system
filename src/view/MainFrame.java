package view;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Healthcare Management System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Patients", new PatientPanel());
        tabs.add("Prescriptions", new PrescriptionPanel());
        tabs.add("Referrals", new ReferralPanel());

        add(tabs);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
