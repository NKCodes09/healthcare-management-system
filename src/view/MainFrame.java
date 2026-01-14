package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {

        setTitle("Healthcare Referral System | Student ID: 25001300");
        setSize(1500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Patients", new PatientPanel());
        tabs.addTab("Clinicians", new ClinicianPanel());
        tabs.addTab("Prescriptions", new PrescriptionPanel());
        tabs.addTab("Referrals", new ReferralPanel());
        tabs.addTab("Staff", new StaffPanel());
        tabs.addTab("Facilities", new FacilityPanel());
        tabs.addTab("Appointments", new AppointmentPanel());

        add(tabs, BorderLayout.CENTER);
    }
}
