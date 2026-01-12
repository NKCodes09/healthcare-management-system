package view;

import model.Patient;
import repository.PatientRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PatientPanel extends JPanel {

    private final PatientRepository repository;
    private final JTable table;
    private final DefaultTableModel model;
    private JPanel buttons;

    public PatientPanel() {

        setLayout(new BorderLayout());
        repository = new PatientRepository();

        model = new DefaultTableModel(
                new String[] {
                        "Patient ID",
                        "First Name",
                        "Last Name",
                        "DOB",
                        "NHS Number",
                        "Gender",
                        "Phone",
                        "GP Surgery"
                }, 0);

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        buttons = new JPanel(); // ✅ class-level
        JButton add = new JButton("Add");
        JButton delete = new JButton("Delete");
        JButton edit = new JButton("Edit");
    

        add.addActionListener(e -> addPatient());
        edit.addActionListener(e -> editPatient());
        buttons.add(edit);
        delete.addActionListener(e -> deletePatient());

       
        buttons.add(add);
        buttons.add(delete);
        buttons.add(edit);
        

        add(buttons, BorderLayout.SOUTH);

        loadPatients();
    }



private void loadPatients() {
    model.setRowCount(0);

    for (Patient p : repository.getAll()) {
        model.addRow(new Object[] {
                p.getPatientId(),
                p.getFirstName(),
                p.getLastName(),
                p.getDateOfBirth(),
                p.getNhsNumber(),
                p.getGender(),
                p.getPhoneNumber(),
                p.getGpSurgeryId()
        });
    }
}


    private void addPatient() {
    try {
        Patient p = new Patient(
                JOptionPane.showInputDialog(this, "Patient ID"),
                JOptionPane.showInputDialog(this, "First Name"),
                JOptionPane.showInputDialog(this, "Last Name"),
                JOptionPane.showInputDialog(this, "Date of Birth"),
                JOptionPane.showInputDialog(this, "NHS Number"),
                JOptionPane.showInputDialog(this, "Gender"),
                JOptionPane.showInputDialog(this, "Phone Number"),
                JOptionPane.showInputDialog(this, "Email"),
                JOptionPane.showInputDialog(this, "Address"),
                JOptionPane.showInputDialog(this, "Postcode"),
                JOptionPane.showInputDialog(this, "Emergency Contact Name"),
                JOptionPane.showInputDialog(this, "Emergency Contact Phone"),
                JOptionPane.showInputDialog(this, "Registration Date"),
                JOptionPane.showInputDialog(this, "GP Surgery ID"));

        repository.addPatient(p);
        loadPatients();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
}

private void editPatient() {

    int row = table.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Select a patient first.");
        return;
    }

    try {
        // 🔑 PRIMARY KEY — NEVER CHANGE THIS
        String patientId = model.getValueAt(row, 0).toString();

        Patient updated = new Patient(
                patientId,
                JOptionPane.showInputDialog(this, "First Name", model.getValueAt(row, 1)),
                JOptionPane.showInputDialog(this, "Last Name", model.getValueAt(row, 2)),
                JOptionPane.showInputDialog(this, "Date of Birth", model.getValueAt(row, 3)),
                JOptionPane.showInputDialog(this, "NHS Number", model.getValueAt(row, 4)),
                JOptionPane.showInputDialog(this, "Gender", model.getValueAt(row, 5)),
                JOptionPane.showInputDialog(this, "Phone Number", model.getValueAt(row, 6)),
                JOptionPane.showInputDialog(this, "Email", ""), // optional
                JOptionPane.showInputDialog(this, "Address", ""), // optional
                JOptionPane.showInputDialog(this, "Postcode", ""), // optional
                JOptionPane.showInputDialog(this, "Emergency Contact Name", ""),
                JOptionPane.showInputDialog(this, "Emergency Contact Phone", ""),
                JOptionPane.showInputDialog(this, "Registration Date", ""),
                JOptionPane.showInputDialog(this, "GP Surgery ID", model.getValueAt(row, 7)));

        repository.updatePatient(updated); // ✅ THIS WAS MISSING
        loadPatients(); // ✅ reload table

        JOptionPane.showMessageDialog(this, "Patient updated successfully.");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void deletePatient() {
        
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient first.");
            return;
        }

        try {
            String nhs = model.getValueAt(row, 0).toString();

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Delete patient with NHS: " + nhs + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION)
                return;

            repository.deletePatient(nhs);
            loadPatients();

            JOptionPane.showMessageDialog(this, "Patient deleted successfully.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete patient:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
