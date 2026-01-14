package view;

import model.Patient;
import repository.PatientRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;

public class PatientPanel extends JPanel {

    private final PatientRepository repository;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public PatientPanel() {
        repository = new PatientRepository();
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[] {
                "Patient ID",
                "First Name",
                "Last Name",
                "DOB",
                "NHS Number",
                "Gender",
                "Phone",
                "Email",
                "Address",
                "Postcode",
                "Emergency Contact",
                "Emergency Phone",
                "Registered On",
                "GP Surgery"
        }, 0);

        table = new JTable(tableModel);
        loadPatients();

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);
    }

    private JPanel createButtons() {
        JPanel panel = new JPanel();

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");

        add.addActionListener(e -> addPatient());
        edit.addActionListener(e -> editPatient());
        delete.addActionListener(e -> deletePatient());

        panel.add(add);
        panel.add(edit);
        panel.add(delete);

        return panel;
    }

    private void loadPatients() {
        tableModel.setRowCount(0);
        for (Patient p : repository.getAll()) {
            tableModel.addRow(new Object[] {
                    p.getPatientId(),
                    p.getFirstName(),
                    p.getLastName(),
                    p.getDateOfBirth(),
                    p.getNhsNumber(),
                    p.getGender(),
                    p.getPhoneNumber(),
                    p.getEmail(),
                    p.getAddress(),
                    p.getPostcode(),
                    p.getEmergencyContactName(),
                    p.getEmergencyContactPhone(),
                    p.getRegistrationDate(),
                    p.getGpSurgeryId()
            });
        }
    }

    private void addPatient() {
        PatientForm form = new PatientForm(null);
        if (form.showDialog()) {
            try {
                repository.addPatient(form.getPatient());
                loadPatients();
            } catch (IOException ex) {
                showError(ex.getMessage());
            }
        }
    }

    private void editPatient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a patient first.");
            return;
        }

        Patient existing = getPatientFromRow(row);
        PatientForm form = new PatientForm(existing);

        if (form.showDialog()) {
            repository.getAll().set(row, form.getPatient());
            try {
                repository.addPatient(null); // triggers save
                loadPatients();
            } catch (IOException ex) {
                showError(ex.getMessage());
            }
        }
    }

    private void deletePatient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a patient first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete selected patient?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                repository.deletePatient(row);
                loadPatients();
            } catch (IOException ex) {
                showError(ex.getMessage());
            }
        }
    }

    private Patient getPatientFromRow(int row) {
        return new Patient(
                tableModel.getValueAt(row, 0).toString(),
                tableModel.getValueAt(row, 1).toString(),
                tableModel.getValueAt(row, 2).toString(),
                tableModel.getValueAt(row, 3).toString(),
                tableModel.getValueAt(row, 4).toString(),
                tableModel.getValueAt(row, 5).toString(),
                tableModel.getValueAt(row, 6).toString(),
                tableModel.getValueAt(row, 7).toString(),
                tableModel.getValueAt(row, 8).toString(),
                tableModel.getValueAt(row, 9).toString(),
                tableModel.getValueAt(row, 10).toString(),
                tableModel.getValueAt(row, 11).toString(),
                tableModel.getValueAt(row, 12).toString(),
                tableModel.getValueAt(row, 13).toString());
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ===================== Inner Form =====================
    private static class PatientForm {

        private final JTextField[] fields = new JTextField[13];
        private boolean confirmed = false;
        private final Patient original;

        PatientForm(Patient p) {
            original = p;
        }

        boolean showDialog() {
            JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));

            String[] labels = {
                    "Patient ID",
                    "First Name",
                    "Last Name",
                    "DOB (YYYY-MM-DD)",
                    "NHS Number (10 digits)",
                    "Gender",
                    "Phone Number",
                    "Email",
                    "Address",
                    "Postcode",
                    "Emergency Contact Name",
                    "Emergency Contact Phone",
                    "GP Surgery ID"
            };

            for (int i = 0; i < fields.length; i++) {
                fields[i] = new JTextField();
                panel.add(new JLabel(labels[i]));
                panel.add(fields[i]);
            }

            if (original != null) {
                fields[0].setText(original.getPatientId());
                fields[1].setText(original.getFirstName());
                fields[2].setText(original.getLastName());
                fields[3].setText(original.getDateOfBirth());
                fields[4].setText(original.getNhsNumber());
                fields[5].setText(original.getGender());
                fields[6].setText(original.getPhoneNumber());
                fields[7].setText(original.getEmail());
                fields[8].setText(original.getAddress());
                fields[9].setText(original.getPostcode());
                fields[10].setText(original.getEmergencyContactName());
                fields[11].setText(original.getEmergencyContactPhone());
                fields[12].setText(original.getGpSurgeryId());
            }

            while (true) {
                int result = JOptionPane.showConfirmDialog(
                        null,
                        panel,
                        "Patient Details",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);

                if (result != JOptionPane.OK_OPTION) {
                    return false;
                }

                if (validateInputs()) {
                    confirmed = true;
                    return true;
                }
            }
        }

        private boolean validateInputs() {

            // DOB validation
            if (!isValidDate(fields[3].getText())) {
                showError("Date of birth must be valid and not in the future (YYYY-MM-DD).");
                return false;
            }

            // NHS number validation
            if (!fields[4].getText().matches("\\d{10}")) {
                showError("NHS Number must be exactly 10 digits.");
                return false;
            }

            // Phone validation
            if (!fields[6].getText().matches("\\d{10,11}")) {
                showError("Phone number must contain 10 or 11 digits.");
                return false;
            }

            // Emergency phone validation
            if (!fields[11].getText().matches("\\d{10,11}")) {
                showError("Emergency contact phone must contain 10 or 11 digits.");
                return false;
            }

            // GP Surgery ID validation
            if (fields[12].getText().trim().isEmpty()) {
                showError("GP Surgery ID is required (e.g. GP001).");
                return false;
            }

            return true;
        }

        private boolean isValidDate(String value) {
            try {
                LocalDate dob = LocalDate.parse(value);
                return !dob.isAfter(LocalDate.now());
            } catch (Exception e) {
                return false;
            }
        }

        private void showError(String message) {
            JOptionPane.showMessageDialog(
                    null,
                    message,
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        Patient getPatient() {
            return new Patient(
                    fields[0].getText().trim(),
                    fields[1].getText().trim(),
                    fields[2].getText().trim(),
                    fields[3].getText().trim(),
                    fields[4].getText().trim(),
                    fields[5].getText().trim(),
                    fields[6].getText().trim(),
                    fields[7].getText().trim(),
                    fields[8].getText().trim(),
                    fields[9].getText().trim(),
                    fields[10].getText().trim(),
                    fields[11].getText().trim(),
                    LocalDate.now().toString(), // registration_date AUTO
                    fields[12].getText().trim());
        }
    }

}
