package view;

import model.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(User user) {

        setTitle("Healthcare Referral System | Student ID: 25001300");
        setSize(1600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        /* ================= ROLE BASED ACCESS ================= */

        // ===== PATIENT =====
        if (user.getRole().equals("PATIENT")) {

            tabs.addTab("Facilities", new FacilityPanel());
            tabs.addTab("Appointments", new AppointmentPanel());
        }

        // ===== STAFF =====
        if (user.getRole().equals("STAFF")) {

            tabs.addTab("Patients", new PatientPanel());
            tabs.addTab("Facilities", new FacilityPanel());
            tabs.addTab("Appointments", new AppointmentPanel());
        }

        // ===== CLINICIAN =====
        if (user.getRole().equals("CLINICIAN")) {

            tabs.addTab("Patients", new PatientPanel());
            tabs.addTab("Appointments", new AppointmentPanel());
            tabs.addTab("Referrals", new ReferralPanel());
        }

        // ===== ADMIN =====
        if (user.getRole().equals("ADMIN")) {

            tabs.addTab("Patients", new PatientPanel());
            tabs.addTab("Clinicians", new ClinicianPanel());
            tabs.addTab("Staff", new StaffPanel());
            tabs.addTab("Facilities", new FacilityPanel());
            tabs.addTab("Appointments", new AppointmentPanel());
            tabs.addTab("Prescriptions", new PrescriptionPanel());
            tabs.addTab("Referrals", new ReferralPanel());
        }

        add(tabs, BorderLayout.CENTER);

        /* ================= TOP BAR ================= */

        JPanel top = new JPanel(new BorderLayout());
        JLabel welcome = new JLabel(
                "Logged in as: " + user.getUsername() + " (" + user.getRole() + ")");

        welcome.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        top.add(welcome, BorderLayout.WEST);

        JButton logout = new JButton("Logout");
        top.add(logout, BorderLayout.EAST);

        logout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        add(top, BorderLayout.NORTH);
    }
}
