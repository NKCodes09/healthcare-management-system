package view;

import model.Patient;
import repository.PatientRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PatientPanel extends JPanel {

    private PatientRepository repository;
    private final JTable table;
    private final DefaultTableModel model;
    private JPanel buttons;

    public PatientPanel() {

        setLayout(new BorderLayout());

        // Safe repository init
        try {
            repository = new PatientRepository();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load patients.csv\n" + e.getMessage(),
                    "Startup Error",
                    JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }

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

        buttons = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");

        addBtn.addActionListener(e -> addPatient());
        editBtn.addActionListener(e -> editPatient());
        deleteBtn.addActionListener(e -> deletePatient());

        buttons.add(addBtn);
        buttons.add(editBtn);
        buttons.add(deleteBtn);

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

        JTextField patientId = new JTextField();
        JTextField firstName = new JTextField();
        JTextField lastName = new JTextField();
        JTextField dob = new JTextField(); // YYYY-MM-DD
        JTextField nhs = new JTextField();
        JTextField gender = new JTextField();
        JTextField phone = new JTextField(); // digits only
        JTextField gpSurgery = new JTextField();

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Patient ID *"));
        form.add(patientId);
        form.add(new JLabel("First Name *"));
        form.add(firstName);
        form.add(new JLabel("Last Name *"));
        form.add(lastName);
        form.add(new JLabel("Date of Birth (YYYY-MM-DD) *"));
        form.add(dob);
        form.add(new JLabel("NHS Number *"));
        form.add(nhs);
        form.add(new JLabel("Gender (M/F) *"));
        form.add(gender);
        form.add(new JLabel("Phone Number *"));
        form.add(phone);
        form.add(new JLabel("GP Surgery ID *"));
        form.add(gpSurgery);

        while (true) {

            int option = JOptionPane.showConfirmDialog(
                    this,
                    form,
                    "Register New Patient",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (option != JOptionPane.OK_OPTION) {
                return; // Cancel pressed
            }

            // 🔒 REQUIRED FIELD CHECK
            if (patientId.getText().trim().isEmpty()
                    || firstName.getText().trim().isEmpty()
                    || lastName.getText().trim().isEmpty()
                    || dob.getText().trim().isEmpty()
                    || nhs.getText().trim().isEmpty()
                    || gender.getText().trim().isEmpty()
                    || phone.getText().trim().isEmpty()
                    || gpSurgery.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "All fields are required.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                continue;
            }

            // 📅 DATE FORMAT VALIDATION
            if (!dob.getText().trim().matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(
                        this,
                        "Date of Birth must be in YYYY-MM-DD format.",
                        "Invalid Date",
                        JOptionPane.WARNING_MESSAGE);
                continue;
            }

            // 📞 PHONE NUMBER VALIDATION (digits only)
            if (!phone.getText().trim().matches("\\d{10,15}")) {
                JOptionPane.showMessageDialog(
                        this,
                        "Phone number must contain digits only (10–15 digits).",
                        "Invalid Phone Number",
                        JOptionPane.WARNING_MESSAGE);
                continue;
            }

            try {
                Patient p = new Patient(
                        patientId.getText().trim(),
                        firstName.getText().trim(),
                        lastName.getText().trim(),
                        dob.getText().trim(),
                        nhs.getText().trim(),
                        gender.getText().trim(),
                        phone.getText().trim(),
                        gpSurgery.getText().trim());

                repository.addPatient(p);
                loadPatients();

                JOptionPane.showMessageDialog(this, "Patient added successfully.");
                break;

            } catch (Exception e) {
                showError(e);
                break;
            }
        }
    }

    private void editPatient() {

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a patient first.");
            return;
        }

        JTextField firstName = new JTextField(model.getValueAt(row, 1).toString());
        JTextField lastName = new JTextField(model.getValueAt(row, 2).toString());
        JTextField dob = new JTextField(model.getValueAt(row, 3).toString());
        JTextField nhs = new JTextField(model.getValueAt(row, 4).toString());
        JTextField gender = new JTextField(model.getValueAt(row, 5).toString());
        JTextField phone = new JTextField(model.getValueAt(row, 6).toString());
        JTextField gpSurgery = new JTextField(model.getValueAt(row, 7).toString());

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("First Name *"));
        form.add(firstName);
        form.add(new JLabel("Last Name *"));
        form.add(lastName);
        form.add(new JLabel("Date of Birth (YYYY-MM-DD) *"));
        form.add(dob);
        form.add(new JLabel("NHS Number *"));
        form.add(nhs);
        form.add(new JLabel("Gender (M/F) *"));
        form.add(gender);
        form.add(new JLabel("Phone Number *"));
        form.add(phone);
        form.add(new JLabel("GP Surgery ID *"));
        form.add(gpSurgery);

        while (true) {

            int option = JOptionPane.showConfirmDialog(
                    this,
                    form,
                    "Edit Patient",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (option != JOptionPane.OK_OPTION) {
                return; // Cancel pressed
            }

            // 🔒 REQUIRED FIELD CHECK
            if (firstName.getText().trim().isEmpty()
                    || lastName.getText().trim().isEmpty()
                    || dob.getText().trim().isEmpty()
                    || nhs.getText().trim().isEmpty()
                    || gender.getText().trim().isEmpty()
                    || phone.getText().trim().isEmpty()
                    || gpSurgery.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "All fields are required.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                continue;
            }

            // 📅 DATE VALIDATION
            if (!dob.getText().trim().matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(
                        this,
                        "Date of Birth must be in YYYY-MM-DD format.",
                        "Invalid Date",
                        JOptionPane.WARNING_MESSAGE);
                continue;
            }

            // 📞 PHONE VALIDATION
            if (!phone.getText().trim().matches("\\d{10,15}")) {
                JOptionPane.showMessageDialog(
                        this,
                        "Phone number must contain digits only (10–15 digits).",
                        "Invalid Phone Number",
                        JOptionPane.WARNING_MESSAGE);
                continue;
            }

            try {
                String patientId = model.getValueAt(row, 0).toString();

                Patient updated = new Patient(
                        patientId,
                        firstName.getText().trim(),
                        lastName.getText().trim(),
                        dob.getText().trim(),
                        nhs.getText().trim(),
                        gender.getText().trim(),
                        phone.getText().trim(),
                        gpSurgery.getText().trim());

                repository.updatePatient(updated);
                loadPatients();

                JOptionPane.showMessageDialog(this, "Patient updated successfully.");
                break;

            } catch (Exception e) {
                showError(e);
                break;
            }
        }
    }

    private void deletePatient() {

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient first.");
            return;
        }

        try {
            String patientId = model.getValueAt(row, 0).toString();

            if (JOptionPane.showConfirmDialog(
                    this,
                    "Delete patient with ID: " + patientId + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
                return;

            repository.deletePatient(patientId);
            loadPatients();

        } catch (Exception e) {
            showError(e);
        }
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
