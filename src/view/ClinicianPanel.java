package view;

import model.Clinician;
import repository.ClinicianRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ClinicianPanel extends JPanel {

    private final ClinicianRepository repository;
    private final DefaultTableModel model;
    private final JTable table;

    public ClinicianPanel() {

        repository = new ClinicianRepository();
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[] {
                "Clinician ID", "First Name", "Last Name", "Title", "Speciality",
                "GMC/NMC Number", "Phone", "Email",
                "Workplace ID", "Workplace Type",
                "Employment Status", "Start Date"
        }, 0);

        table = new JTable(model);
        loadClinicians();

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);
    }

    /* ================= BUTTONS ================= */

    private JPanel createButtons() {
        JPanel p = new JPanel();

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");

        add.addActionListener(e -> addClinician());
        edit.addActionListener(e -> editClinician());
        delete.addActionListener(e -> deleteClinician());

        p.add(add);
        p.add(edit);
        p.add(delete);

        return p;
    }

    /* ================= LOAD ================= */

    private void loadClinicians() {
        model.setRowCount(0);
        for (Clinician c : repository.getAll()) {
            model.addRow(new Object[] {
                    c.getClinicianId(),
                    c.getFirstName(),
                    c.getLastName(),
                    c.getTitle(),
                    c.getSpeciality(),
                    c.getGmcNumber(),
                    c.getPhoneNumber(),
                    c.getEmail(),
                    c.getWorkplaceId(),
                    c.getWorkplaceType(),
                    c.getEmploymentStatus(),
                    c.getStartDate()
            });
        }
    }

    /* ================= CRUD ================= */

    private void addClinician() {
        ClinicianForm form = new ClinicianForm(null);
        if (!form.showDialog())
            return;

        try {
            repository.addClinician(form.getClinician());
            loadClinicians();
        } catch (IOException ex) {
            showError(ex.getMessage());
        }
    }

    private void editClinician() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a clinician first.");
            return;
        }

        Clinician existing = repository.getAll().get(row);
        ClinicianForm form = new ClinicianForm(existing);

        if (!form.showDialog())
            return;

        Clinician updated = form.getClinician();

        Clinician fixed = new Clinician(
                existing.getClinicianId(), // 🔒 ID locked
                updated.getFirstName(),
                updated.getLastName(),
                updated.getTitle(),
                updated.getSpeciality(),
                updated.getGmcNumber(),
                updated.getPhoneNumber(),
                updated.getEmail(),
                updated.getWorkplaceId(),
                updated.getWorkplaceType(),
                updated.getEmploymentStatus(),
                updated.getStartDate());

        repository.getAll().set(row, fixed);

        try {
            repository.updateAll();
            loadClinicians();
        } catch (IOException ex) {
            showError(ex.getMessage());
        }
    }

    private void deleteClinician() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a clinician first.");
            return;
        }

        if (JOptionPane.showConfirmDialog(
                this,
                "Delete selected clinician?",
                "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                repository.deleteClinician(row);
                loadClinicians();
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
     * CLINICIAN FORM
     * =====================================================
     */

    private static class ClinicianForm {

        private final JTextField[] f = new JTextField[12];
        private final Clinician original;

        ClinicianForm(Clinician c) {
            original = c;
        }

        boolean showDialog() {

            JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));

            String[] labels = {
                    "Clinician ID", "First Name", "Last Name", "Title", "Speciality",
                    "GMC/NMC Number", "Phone", "Email",
                    "Workplace ID", "Workplace Type",
                    "Employment Status", "Start Date (YYYY-M-D)"
            };

            for (int i = 0; i < f.length; i++) {
                f[i] = new JTextField();
                panel.add(new JLabel(labels[i]));
                panel.add(f[i]);
            }

            if (original != null) {
                f[0].setText(original.getClinicianId());
                f[1].setText(original.getFirstName());
                f[2].setText(original.getLastName());
                f[3].setText(original.getTitle());
                f[4].setText(original.getSpeciality());
                f[5].setText(original.getGmcNumber());
                f[6].setText(original.getPhoneNumber());
                f[7].setText(original.getEmail());
                f[8].setText(original.getWorkplaceId());
                f[9].setText(original.getWorkplaceType());
                f[10].setText(original.getEmploymentStatus());
                f[11].setText(original.getStartDate());

                f[0].setEditable(false); // 🔒 ID locked
            }

            while (true) {
                int result = JOptionPane.showConfirmDialog(
                        null, panel,
                        original == null ? "Add Clinician" : "Edit Clinician",
                        JOptionPane.OK_CANCEL_OPTION);

                if (result != JOptionPane.OK_OPTION)
                    return false;
                if (validate())
                    return true;
            }
        }

        /* ================= VALIDATION ================= */

        private boolean validate() {

            if (!f[0].getText().matches("C\\d{3}")) {
                error("Clinician ID must be in format C001.");
                return false;
            }

            if (!f[1].getText().matches("[A-Za-z\\s]{2,}")) {
                error("First name invalid.");
                return false;
            }

            if (!f[2].getText().matches("[A-Za-z\\s]{2,}")) {
                error("Last name invalid.");
                return false;
            }

            if (f[3].getText().trim().isEmpty()) {
                error("Title is required.");
                return false;
            }

            if (f[4].getText().trim().isEmpty()) {
                error("Speciality is required.");
                return false;
            }

            if (!(f[5].getText().matches("\\d{7}") || f[5].getText().matches("N\\d{6}"))) {
                error("GMC must be 7 digits or N######.");
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

            if (!f[8].getText().matches("[SH]\\d{3}")) {
                error("Workplace ID must be like S001 or H001.");
                return false;
            }

            if (!f[9].getText().matches("GP Surgery|Hospital")) {
                error("Workplace type must be GP Surgery or Hospital.");
                return false;
            }

            if (!f[10].getText().matches("Full-time|Part-time")) {
                error("Employment status must be Full-time or Part-time.");
                return false;
            }

            LocalDate start = parseDate(f[11].getText());
            if (start == null || start.isAfter(LocalDate.now())) {
                error("Invalid start date.");
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

        Clinician getClinician() {
            LocalDate startDate = parseDate(f[11].getText());

            return new Clinician(
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
                    startDate.toString());
        }
    }
}
