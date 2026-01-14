package view;

import model.Clinician;
import repository.ClinicianRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;

public class ClinicianPanel extends JPanel {

    private final ClinicianRepository repository;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public ClinicianPanel() {

        repository = new ClinicianRepository();
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[] {
                "Clinician ID",
                "First Name",
                "Last Name",
                "Title",
                "Speciality",
                "GMC Number",
                "Phone Number",
                "Email",
                "Workplace ID",
                "Workplace Type",
                "Employment Status",
                "Start Date"
        }, 0);

        table = new JTable(tableModel);
        loadClinicians();

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);
    }

    /* ---------------- Buttons ---------------- */

    private JPanel createButtons() {
        JPanel panel = new JPanel();

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");

        add.addActionListener(e -> addClinician());
        edit.addActionListener(e -> editClinician());
        delete.addActionListener(e -> deleteClinician());

        panel.add(add);
        panel.add(edit);
        panel.add(delete);

        return panel;
    }

    /* ---------------- Load Table ---------------- */

    private void loadClinicians() {
        tableModel.setRowCount(0);
        for (Clinician c : repository.getAll()) {
            tableModel.addRow(new Object[] {
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

    /* ---------------- CRUD ---------------- */

    private void addClinician() {
        ClinicianForm form = new ClinicianForm(null);
        if (form.showDialog()) {
            try {
                repository.addClinician(form.getClinician());
                loadClinicians();
            } catch (IOException ex) {
                showError(ex.getMessage());
            }
        }
    }

    private void editClinician() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a clinician first.");
            return;
        }

        ClinicianForm form = new ClinicianForm(repository.getAll().get(row));
        if (form.showDialog()) {
            repository.getAll().set(row, form.getClinician());
            try {
                repository.updateAll();
                loadClinicians();
            } catch (IOException ex) {
                showError(ex.getMessage());
            }
        }
    }

    private void deleteClinician() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a clinician first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete selected clinician?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                repository.deleteClinician(row);
                loadClinicians();
            } catch (IOException ex) {
                showError(ex.getMessage());
            }
        }
    }

    /* ---------------- Helpers ---------------- */

    private void showError(String msg) {
        JOptionPane.showMessageDialog(
                this,
                msg,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /*
     * ======================================================
     * CLINICIAN FORM
     * ======================================================
     */

    private static class ClinicianForm {

        private final JTextField[] fields = new JTextField[11];
        private final Clinician original;

        ClinicianForm(Clinician c) {
            original = c;
        }

        boolean showDialog() {

            JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));

            String[] labels = {
                    "Clinician ID",
                    "First Name",
                    "Last Name",
                    "Title",
                    "Speciality",
                    "GMC / NMC Number",
                    "Phone Number",
                    "Email",
                    "Workplace ID",
                    "Workplace Type",
                    "Employment Status"
            };

            for (int i = 0; i < fields.length; i++) {
                fields[i] = new JTextField();
                panel.add(new JLabel(labels[i]));
                panel.add(fields[i]);
            }

            if (original != null) {
                fields[0].setText(original.getClinicianId());
                fields[1].setText(original.getFirstName());
                fields[2].setText(original.getLastName());
                fields[3].setText(original.getTitle());
                fields[4].setText(original.getSpeciality());
                fields[5].setText(original.getGmcNumber());
                fields[6].setText(original.getPhoneNumber());
                fields[7].setText(original.getEmail());
                fields[8].setText(original.getWorkplaceId());
                fields[9].setText(original.getWorkplaceType());
                fields[10].setText(original.getEmploymentStatus());
            }

            while (true) {
                int result = JOptionPane.showConfirmDialog(
                        null,
                        panel,
                        "Clinician Details",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);

                if (result != JOptionPane.OK_OPTION) {
                    return false;
                }

                if (validateInputs()) {
                    return true;
                }
            }
        }

        /* ================= VALIDATION ================= */

        private boolean validateInputs() {

            // Required fields
            if (fields[0].getText().trim().isEmpty()) {
                error("Clinician ID is required.");
                return false;
            }

            if (fields[1].getText().trim().isEmpty()
                    || fields[2].getText().trim().isEmpty()) {
                error("First name and last name are required.");
                return false;
            }

            // GMC / NMC number
            String gmc = fields[5].getText().trim();
            if (!(gmc.matches("\\d{7}") || gmc.matches("N\\d{6}"))) {
                error("GMC number must be 7 digits (doctor) or N###### (nurse).");
                return false;
            }

            // Phone number
            if (!fields[6].getText().trim().matches("\\d{10,11}")) {
                error("Phone number must be 10 or 11 digits.");
                return false;
            }

            // Email
            String email = fields[7].getText().trim();
            if (!email.contains("@") || !email.contains(".")) {
                error("Enter a valid email address.");
                return false;
            }

            // Workplace ID
            if (fields[8].getText().trim().isEmpty()) {
                error("Workplace ID is required (e.g. S001 or H001).");
                return false;
            }

            // Employment status
            if (fields[10].getText().trim().isEmpty()) {
                error("Employment status is required.");
                return false;
            }

            return true;
        }

        private void error(String msg) {
            JOptionPane.showMessageDialog(
                    null,
                    msg,
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        /* ================= CREATE OBJECT ================= */

        Clinician getClinician() {
            return new Clinician(
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
                    LocalDate.now().toString() // start_date auto
            );
        }
    }

}
