package view;

import controller.PrescriptionController;
import model.Prescription;
import repository.PrescriptionRepository;

import javax.swing.*;
import java.awt.*;

public class PrescriptionPanel extends JPanel {

    public PrescriptionPanel() {
        setLayout(new GridLayout(6, 2));

        JTextField id = new JTextField();
        JTextField patientId = new JTextField();
        JTextField medication = new JTextField();
        JTextField dosage = new JTextField();
        JTextField pharmacy = new JTextField();

        JButton save = new JButton("Create Prescription");

        add(new JLabel("Prescription ID"));
        add(id);
        add(new JLabel("Patient ID"));
        add(patientId);
        add(new JLabel("Medication"));
        add(medication);
        add(new JLabel("Dosage"));
        add(dosage);
        add(new JLabel("Pharmacy"));
        add(pharmacy);
        add(save);

       

        PrescriptionRepository repo = new PrescriptionRepository();
        repo.loadPrescriptions("prescriptions.csv");
        PrescriptionController controller = new PrescriptionController(repo);

        save.addActionListener(e -> {
            Prescription p = new Prescription(
                    id.getText(),
                    patientId.getText(),
                    medication.getText(),
                    dosage.getText(),
                    pharmacy.getText(),
                    "Created");
            controller.createPrescription(p);
            JOptionPane.showMessageDialog(this, "Prescription saved");
        });
    }
}
