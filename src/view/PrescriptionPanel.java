package view;

import controller.PrescriptionController;
import model.Prescription;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PrescriptionPanel extends JPanel {

    private final PrescriptionController controller = new PrescriptionController();
    private final DefaultTableModel model;
    private final JTable table;

    public PrescriptionPanel() {

        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[] {
                "Prescription ID", "Patient ID", "Clinician ID", "Appointment ID",
                "Prescription Date", "Medication", "Dosage", "Frequency",
                "Duration", "Quantity", "Instructions",
                "Pharmacy", "Status", "Issue Date", "Collection Date"
        }, 0);

        table = new JTable(model);
        table.setRowHeight(24);
        table.getTableHeader().setReorderingAllowed(false);

        load();

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons(), BorderLayout.SOUTH);
    }

    /* ================= TABLE ================= */

    private JPanel buttons() {

        JPanel p = new JPanel();

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton del = new JButton("Delete");

        add.addActionListener(e -> add());
        edit.addActionListener(e -> edit());
        del.addActionListener(e -> delete());

        p.add(add);
        p.add(edit);
        p.add(del);

        return p;
    }

    private void load() {

        model.setRowCount(0);

        for (Prescription p : controller.getAll()) {
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

    private void add() {

        PrescriptionForm f = new PrescriptionForm(null);
        if (!f.showDialog())
            return;

        try {
            controller.add(f.getPrescription());
            load();
        } catch (IOException ex) {
            error(ex.getMessage());
        }
    }

    private void edit() {

        int r = table.getSelectedRow();
        if (r == -1)
            return;

        Prescription old = controller.getAll().get(r);
        PrescriptionForm f = new PrescriptionForm(old);

        if (!f.showDialog())
            return;

        try {
            controller.update(r, f.getPrescription());
            load();
        } catch (IOException ex) {
            error(ex.getMessage());
        }
    }

    private void delete() {

        int r = table.getSelectedRow();
        if (r == -1)
            return;

        if (JOptionPane.showConfirmDialog(
                this, "Delete prescription?",
                "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            try {
                controller.delete(r);
                load();
            } catch (IOException ex) {
                error(ex.getMessage());
            }
        }
    }

    private void error(String m) {
        JOptionPane.showMessageDialog(this, m, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /*
     * =====================================================
     * INNER FORM CLASS (LIKE PATIENT PANEL)
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
                f[0].setEditable(false);
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
            }

            JScrollPane scroll = new JScrollPane(panel);
            scroll.setPreferredSize(new Dimension(520, 420));

            while (true) {

                int ok = JOptionPane.showConfirmDialog(
                        null, scroll,
                        original == null ? "Add Prescription" : "Edit Prescription",
                        JOptionPane.OK_CANCEL_OPTION);

                if (ok != JOptionPane.OK_OPTION)
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
                error("Patient ID must be P001 format.");
                return false;
            }

            if (!f[2].getText().matches("C\\d{3}")) {
                error("Clinician ID must be C001 format.");
                return false;
            }

            if (!f[3].getText().trim().isEmpty()
                    && !f[3].getText().matches("A\\d{3}")) {
                error("Appointment ID must be empty or A001.");
                return false;
            }

            if (parseDate(f[4].getText()) == null) {
                error("Invalid prescription date.");
                return false;
            }

            if (f[5].getText().trim().length() < 2) {
                error("Medication name required.");
                return false;
            }

            if (!f[8].getText().matches("\\d+")
                    || !f[9].getText().matches("\\d+")) {
                error("Duration and quantity must be numeric.");
                return false;
            }

            if (!f[12].getText().matches("Issued|Collected")) {
                error("Status must be Issued or Collected.");
                return false;
            }

            return true;
        }

        private LocalDate parseDate(String input) {
            try {
                return LocalDate.parse(
                        input.trim(),
                        DateTimeFormatter.ofPattern("yyyy-M-d"));
            } catch (DateTimeParseException e) {
                return null;
            }
        }

        private void error(String msg) {
            JOptionPane.showMessageDialog(null, msg,
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        Prescription getPrescription() {

            return new Prescription(
                    f[0].getText().trim(),
                    f[1].getText().trim(),
                    f[2].getText().trim(),
                    f[3].getText().trim(),
                    f[4].getText().trim(),
                    f[5].getText().trim(),
                    f[6].getText().trim(),
                    f[7].getText().trim(),
                    f[8].getText().trim(),
                    f[9].getText().trim(),
                    f[10].getText().trim(),
                    f[11].getText().trim(),
                    f[12].getText().trim(),
                    f[13].getText().trim(),
                    f[14].getText().trim());
        }
    }
}
