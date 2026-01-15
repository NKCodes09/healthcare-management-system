package view;

import model.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(User user) {

        setTitle("Healthcare Referral System | Student ID: 25001300");
        setSize(1700, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        if (user.getRole().equals("PATIENT")) {
            tabs.addTab("Facilities", new FacilityPanel());
            tabs.addTab("Appointments", new AppointmentPanel());
        }

        if (user.getRole().equals("STAFF")) {
            tabs.addTab("Patients", new PatientPanel());
            tabs.addTab("Facilities", new FacilityPanel());
            tabs.addTab("Appointments", new AppointmentPanel());
        }

        if (user.getRole().equals("CLINICIAN")) {
            tabs.addTab("Patients", new PatientPanel());
            tabs.addTab("Appointments", new AppointmentPanel());
            tabs.addTab("Referrals", new ReferralPanel());
        }

        if (user.getRole().equals("ADMIN")) {
            tabs.addTab("Patients", new PatientPanel());
            tabs.addTab("Clinicians", new ClinicianPanel());
            tabs.addTab("Staff", new StaffPanel());
            tabs.addTab("Facilities", new FacilityPanel());
            tabs.addTab("Appointments", new AppointmentPanel());
            tabs.addTab("Prescriptions", new PrescriptionPanel());
            tabs.addTab("Referrals", new ReferralPanel());
        }

        JPanel top = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Healthcare Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JLabel userInfo = new JLabel(
                "Logged in as: " + user.getUsername() +
                        " (" + user.getRole() + ")");
        userInfo.setBorder(
                BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JButton logout = new JButton("Logout");
        logout.setFocusPainted(false);

        logout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        top.add(title, BorderLayout.CENTER);
        top.add(userInfo, BorderLayout.WEST);
        top.add(logout, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }
}
