package view;

import controller.PatientController;
import model.Patient;
import repository.PatientRepository;

import javax.swing.*;
import java.awt.*;

public class PatientPanel extends JPanel {

    private DefaultListModel<Patient> listModel = new DefaultListModel<>();
    private JList<Patient> patientList = new JList<>(listModel);

    public PatientPanel() {
        setLayout(new BorderLayout());

        PatientRepository repo = new PatientRepository();
        PatientController controller = new PatientController(repo);
        controller.loadPatients("patients.csv");

        for (Patient p : controller.getPatients()) {
            listModel.addElement(p);
        }

        add(new JScrollPane(patientList), BorderLayout.CENTER);
    }
}
