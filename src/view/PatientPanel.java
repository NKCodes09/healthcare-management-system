package view;

import model.Patient;
import repository.PatientRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PatientPanel extends JPanel {

    private final PatientRepository repository;
    private final DefaultTableModel model;
    private final JTable table;

    public PatientPanel() {

        repository = new PatientRepository();
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[] {
                "Patient ID", "First Name", "Last Name", "DOB", "NHS Number",
                "Gender", "Phone", "Email", "Address", "Postcode",
                "Emergency Contact", "Emergency Phone",
                "Registered On", "GP Surgery ID"
        }, 0);

        table = new JTable(model);
        loadPatients();

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);
    }

    /* ================= BUTTONS ================= */

    private JPanel createButtons() {
        JPanel p = new JPanel();

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");

        add.addActionListener(e -> addPatient());
        edit.addActionListener(e -> editPatient());
        delete.addActionListener(e -> deletePatient());

        p.add(add);
        p.add(edit);
        p.add(delete);

        return p;
    }

    /* ================= LOAD ================= */

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

    /* ================= CRUD ================= */

    private void addPatient() {
        PatientForm form = new PatientForm(null);
        if (!form.showDialog())
            return;

        try {
            repository.addPatient(form.getPatient());
            loadPatients();
        } catch (IOException ex) {
            showError(ex.getMessage());
        }
    }

    private void editPatient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a patient first.");
            return;
        }

        Patient existing = repository.getAll().get(row);
        PatientForm form = new PatientForm(existing);

        if (!form.showDialog())
            return;

        Patient updated = form.getPatient();

        Patient fixed = new Patient(
                existing.getPatientId(), // 🔒 ID locked
                updated.getFirstName(),
                updated.getLastName(),
                updated.getDateOfBirth(),
                updated.getNhsNumber(),
                updated.getGender(),
                updated.getPhoneNumber(),
                updated.getEmail(),
                updated.getAddress(),
                updated.getPostcode(),
                updated.getEmergencyContactName(),
                updated.getEmergencyContactPhone(),
                existing.getRegistrationDate(),
                updated.getGpSurgeryId());

        repository.getAll().set(row, fixed);

        try {
            repository.updateAll();
            loadPatients();
        } catch (IOException ex) {
            showError(ex.getMessage());
        }
    }

    private void deletePatient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a patient first.");
            return;
        }

        if (JOptionPane.showConfirmDialog(
                this,
                "Delete selected patient?",
                "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                repository.deletePatient(row);
                loadPatients();
            } catch (IOException ex) {
                showError(ex.getMessage());
            }
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /*
     * =====================================================
     * PATIENT FORM
     * =====================================================
     */

    private static class PatientForm {

        private final JTextField[] f = new JTextField[13];
        private final Patient original;

        PatientForm(Patient p) {
            original = p;
        }

        boolean showDialog() {

            JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));

            String[] labels = {
                    "Patient ID", "First Name", "Last Name", "DOB (YYYY-M-D)",
                    "NHS Number", "Gender", "Phone", "Email",
                    "Address", "Postcode",
                    "Emergency Contact", "Emergency Phone",
                    "GP Surgery ID"
            };

            for (int i = 0; i < f.length; i++) {
                f[i] = new JTextField();
                panel.add(new JLabel(labels[i]));
                panel.add(f[i]);
            }

            if (original != null) {
                f[0].setText(original.getPatientId());
                f[1].setText(original.getFirstName());
                f[2].setText(original.getLastName());
                f[3].setText(original.getDateOfBirth());
                f[4].setText(original.getNhsNumber());
                f[5].setText(original.getGender());
                f[6].setText(original.getPhoneNumber());
                f[7].setText(original.getEmail());
                f[8].setText(original.getAddress());
                f[9].setText(original.getPostcode());
                f[10].setText(original.getEmergencyContactName());
                f[11].setText(original.getEmergencyContactPhone());
                f[12].setText(original.getGpSurgeryId());

                f[0].setEditable(false);
            }

            while (true) {
                int result = JOptionPane.showConfirmDialog(
                        null, panel,
                        original == null ? "Add Patient" : "Edit Patient",
                        JOptionPane.OK_CANCEL_OPTION);

                if (result != JOptionPane.OK_OPTION)
                    return false;
                if (validate())
                    return true;
            }
        }

        /* ================= VALIDATION ================= */

        private boolean validate() {

            if (!f[0].getText().matches("P\\d{3}")) {
                error("Patient ID must be in format P001.");
                return false;
            }

            if (!f[1].getText().matches("[A-Za-z\\s]{2,}")) {
                error("First name is invalid.");
                return false;
            }

            if (!f[2].getText().matches("[A-Za-z\\s]{2,}")) {
                error("Last name is invalid.");
                return false;
            }

            LocalDate dob = parseDate(f[3].getText());
            if (dob == null || dob.isAfter(LocalDate.now())) {
                error("Invalid date of birth.");
                return false;
            }

            if (!f[4].getText().matches("\\d{10}")) {
                error("NHS number must be exactly 10 digits.");
                return false;
            }

            if (!f[5].getText().matches("Male|Female|Other")) {
                error("Gender must be Male, Female, or Other.");
                return false;
            }

            if (!f[6].getText().matches("\\d{10,11}")) {
                error("Phone number must be 10–11 digits.");
                return false;
            }

            if (!f[7].getText().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                error("Invalid email address.");
                return false;
            }

            if (f[8].getText().trim().length() < 5) {
                error("Address is required.");
                return false;
            }

            if (!f[9].getText().matches("[A-Za-z0-9\\s]{5,8}")) {
                error("Invalid postcode.");
                return false;
            }

            if (!f[10].getText().matches("[A-Za-z\\s]{2,}")) {
                error("Emergency contact name invalid.");
                return false;
            }

            if (!f[11].getText().matches("\\d{10,11}")) {
                error("Emergency phone must be 10–11 digits.");
                return false;
            }

            if (!f[12].getText().matches("S\\d{3}")) {
                error("GP Surgery ID must be like S001.");
                return false;
            }

            return true;
        }

        private LocalDate parseDate(String input) {
            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-M-d");
                return LocalDate.parse(input.trim(), fmt);
            } catch (DateTimeParseException e) {
                return null;
            }
        }

        private void error(String msg) {
            JOptionPane.showMessageDialog(null, msg, "Validation Error", JOptionPane.ERROR_MESSAGE);
        }

        /* ================= CREATE OBJECT ================= */

        Patient getPatient() {
            LocalDate dob = parseDate(f[3].getText());

            return new Patient(
                    f[0].getText().trim(),
                    f[1].getText().trim(),
                    f[2].getText().trim(),
                    dob.toString(),
                    f[4].getText().trim(),
                    f[5].getText().trim(),
                    f[6].getText().trim(),
                    f[7].getText().trim(),
                    f[8].getText().trim(),
                    f[9].getText().trim(),
                    f[10].getText().trim(),
                    f[11].getText().trim(),
                    LocalDate.now().toString(),
                    f[12].getText().trim());
        }
    }
}
