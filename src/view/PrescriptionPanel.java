package view;

import model.Prescription;
import repository.PrescriptionRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PrescriptionPanel extends JPanel {

    private final PrescriptionRepository repository;
    private final DefaultTableModel model;
    private final JTable table;

    public PrescriptionPanel() {

        repository = new PrescriptionRepository();
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[] {
                "Prescription ID", "Patient ID", "Clinician ID", "Appointment ID",
                "Prescription Date", "Medication", "Dosage", "Frequency",
                "Duration (Days)", "Quantity", "Instructions",
                "Pharmacy", "Status", "Issue Date", "Collection Date"
        }, 0);

        table = new JTable(model);
        loadPrescriptions();

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);
    }

    /* ================= BUTTONS ================= */

    private JPanel createButtons() {
        JPanel p = new JPanel();

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");

        add.addActionListener(e -> addPrescription());
        edit.addActionListener(e -> editPrescription());
        delete.addActionListener(e -> deletePrescription());

        p.add(add);
        p.add(edit);
        p.add(delete);

        return p;
    }

    /* ================= LOAD ================= */

    private void loadPrescriptions() {
        model.setRowCount(0);
        for (Prescription p : repository.getAll()) {
            model.addRow(new Object[] {
                    p.getPrescriptionId(),
                    p.getPatientId(),
                    p.getClinicianId(),
                    p.getAppointmentId(),
                    p.getPrescriptionDate(),
                    p.getMedicationName(),
                    p.getDosage(),
                    p.getFrequency(),
                    p.getDurationDays(),
                    p.getQuantity(),
                    p.getInstructions(),
                    p.getPharmacyName(),
                    p.getStatus(),
                    p.getIssueDate(),
                    p.getCollectionDate()
            });
        }
    }

    /* ================= CRUD ================= */

    private void addPrescription() {
        PrescriptionForm form = new PrescriptionForm(null);
        if (!form.showDialog())
            return;

        try {
            repository.addPrescription(form.getPrescription());
            loadPrescriptions();
        } catch (IOException ex) {
            showError(ex.getMessage());
        }
    }

    private void editPrescription() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a prescription first.");
            return;
        }

        Prescription existing = repository.getAll().get(row);
        PrescriptionForm form = new PrescriptionForm(existing);

        if (!form.showDialog())
            return;

        Prescription updated = form.getPrescription();

        Prescription fixed = new Prescription(
                existing.getPrescriptionId(), // 🔒 ID locked
                updated.getPatientId(),
                updated.getClinicianId(),
                updated.getAppointmentId(),
                updated.getPrescriptionDate(),
                updated.getMedicationName(),
                updated.getDosage(),
                updated.getFrequency(),
                updated.getDurationDays(),
                updated.getQuantity(),
                updated.getInstructions(),
                updated.getPharmacyName(),
                updated.getStatus(),
                updated.getIssueDate(),
                updated.getCollectionDate());

        repository.getAll().set(row, fixed);

        try {
            repository.updateAll();
            loadPrescriptions();
        } catch (IOException ex) {
            showError(ex.getMessage());
        }
    }

    private void deletePrescription() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a prescription first.");
            return;
        }

        if (JOptionPane.showConfirmDialog(
                this,
                "Delete selected prescription?",
                "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                repository.deletePrescription(row);
                loadPrescriptions();
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
     * PRESCRIPTION FORM
     * =====================================================
     */

    private static class PrescriptionForm {

        private final JTextField[] f = new JTextField[15];
        private final Prescription original;

        PrescriptionForm(Prescription p) {
            original = p;
        }

        boolean showDialog() {

            JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));

            String[] labels = {
                    "Prescription ID", "Patient ID", "Clinician ID", "Appointment ID",
                    "Prescription Date (YYYY-M-D)",
                    "Medication Name", "Dosage", "Frequency",
                    "Duration Days", "Quantity",
                    "Instructions", "Pharmacy Name",
                    "Status (Issued / Collected)",
                    "Issue Date (YYYY-M-D)",
                    "Collection Date (YYYY-M-D)"
            };

            for (int i = 0; i < f.length; i++) {
                f[i] = new JTextField();
                panel.add(new JLabel(labels[i]));
                panel.add(f[i]);
            }

            if (original != null) {
                f[0].setText(original.getPrescriptionId());
                f[1].setText(original.getPatientId());
                f[2].setText(original.getClinicianId());
                f[3].setText(original.getAppointmentId());
                f[4].setText(original.getPrescriptionDate());
                f[5].setText(original.getMedicationName());
                f[6].setText(original.getDosage());
                f[7].setText(original.getFrequency());
                f[8].setText(original.getDurationDays());
                f[9].setText(original.getQuantity());
                f[10].setText(original.getInstructions());
                f[11].setText(original.getPharmacyName());
                f[12].setText(original.getStatus());
                f[13].setText(original.getIssueDate());
                f[14].setText(original.getCollectionDate());

                f[0].setEditable(false); // 🔒 ID lock
            }

            while (true) {
                int result = JOptionPane.showConfirmDialog(
                        null, panel,
                        original == null ? "Add Prescription" : "Edit Prescription",
                        JOptionPane.OK_CANCEL_OPTION);

                if (result != JOptionPane.OK_OPTION)
                    return false;
                if (validate())
                    return true;
            }
        }

        /* ================= VALIDATION ================= */

        private boolean validate() {

            if (!f[0].getText().matches("RX\\d{3}")) {
                error("Prescription ID must be RX001 format.");
                return false;
            }

            if (!f[1].getText().matches("P\\d{3}")) {
                error("Patient ID must be like P001.");
                return false;
            }

            if (!f[2].getText().matches("C\\d{3}")) {
                error("Clinician ID must be like C001.");
                return false;
            }

            if (!f[3].getText().trim().isEmpty()
                    && !f[3].getText().matches("A\\d{3}")) {
                error("Appointment ID must be empty or A001 format.");
                return false;
            }

            LocalDate prescriptionDate = parseDate(f[4].getText());
            if (prescriptionDate == null) {
                error("Invalid prescription date.");
                return false;
            }

            if (f[5].getText().trim().length() < 2) {
                error("Medication name required.");
                return false;
            }

            if (f[6].getText().trim().isEmpty()) {
                error("Dosage is required.");
                return false;
            }

            if (f[7].getText().trim().isEmpty()) {
                error("Frequency is required.");
                return false;
            }

            if (!f[8].getText().matches("\\d+")) {
                error("Duration must be numeric.");
                return false;
            }

            if (!f[9].getText().matches("\\d+")) {
                error("Quantity must be numeric.");
                return false;
            }

            if (f[10].getText().trim().length() < 3) {
                error("Instructions required.");
                return false;
            }

            if (f[11].getText().trim().length() < 3) {
                error("Pharmacy name required.");
                return false;
            }

            if (!f[12].getText().matches("Issued|Collected")) {
                error("Status must be Issued or Collected.");
                return false;
            }

            LocalDate issueDate = parseDate(f[13].getText());
            if (issueDate == null) {
                error("Invalid issue date.");
                return false;
            }

            LocalDate collectionDate = null;
            if (!f[14].getText().trim().isEmpty()) {
                collectionDate = parseDate(f[14].getText());
                if (collectionDate == null || collectionDate.isBefore(issueDate)) {
                    error("Collection date must be after issue date.");
                    return false;
                }
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

        Prescription getPrescription() {

            LocalDate prescriptionDate = parseDate(f[4].getText());
            LocalDate issueDate = parseDate(f[13].getText());
            LocalDate collectionDate = parseDate(f[14].getText());

            return new Prescription(
                    f[0].getText().trim(),
                    f[1].getText().trim(),
                    f[2].getText().trim(),
                    f[3].getText().trim(),
                    prescriptionDate.toString(),
                    f[5].getText().trim(),
                    f[6].getText().trim(),
                    f[7].getText().trim(),
                    f[8].getText().trim(),
                    f[9].getText().trim(),
                    f[10].getText().trim(),
                    f[11].getText().trim(),
                    f[12].getText().trim(),
                    issueDate.toString(),
                    collectionDate == null ? "" : collectionDate.toString());
        }
    }
}
