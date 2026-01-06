package view;

import controller.ReferralController;
import model.*;

import javax.swing.*;
import java.awt.*;

public class ReferralPanel extends JPanel {

    public ReferralPanel() {
        setLayout(new GridLayout(5, 2));

        JTextField patientName = new JTextField();
        JTextField clinicianName = new JTextField();
        JTextField urgency = new JTextField();
        JTextArea summary = new JTextArea();

        JButton submit = new JButton("Submit Referral");

        add(new JLabel("Patient Name"));
        add(patientName);
        add(new JLabel("Clinician Name"));
        add(clinicianName);
        add(new JLabel("Urgency"));
        add(urgency);
        add(new JLabel("Clinical Summary"));
        add(summary);
        add(submit);

        ReferralController controller = new ReferralController();

        submit.addActionListener(e -> {
            Patient p = new Patient("P1", patientName.getText(),
                    "01/01/1990", "NHS001", "Contact", "GP");

            Clinician c = new Clinician("C1", clinicianName.getText(),
                    "Doctor", "General", "Hospital");

            controller.createReferral(p, c, urgency.getText(), summary.getText());
            JOptionPane.showMessageDialog(this, "Referral created");
        });
    }
}
