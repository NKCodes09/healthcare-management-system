package view;

import controller.PatientController;
import model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;

public class PatientPanel extends JPanel {

    private final PatientController controller = new PatientController();
    private final DefaultTableModel model;
    private final JTable table;

    public PatientPanel() {

        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[] {
                "ID", "First", "Last", "DOB", "NHS",
                "Gender", "Phone", "Email", "Address",
                "Postcode", "Emergency", "Emergency Phone",
                "Registered", "GP Surgery"
        }, 0);

        table = new JTable(model);
        table.setRowHeight(24);

        load();

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons(), BorderLayout.SOUTH);
    }

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

        for (Patient p : controller.getAll()) {
            model.addRow(new Object[] {
                    p.getPatientId(),
                    p.getFirstName(),
                    p.getLastName(),
                    p.getDateOfBirth(),
                    p.getNhsNumber(),
                    p.getGender(),
                    p.getPhone(),
                    p.getEmail(),
                    p.getAddress(),
                    p.getPostcode(),
                    p.getEmergencyContact(),
                    p.getEmergencyPhone(),
                    p.getRegistrationDate(),
                    p.getGpSurgeryId()
            });
        }
    }

    private void add() {

        PatientForm f = new PatientForm(null);
        if (!f.show())
            return;

        try {
            controller.add(f.get());
            load();
        } catch (IOException e) {
            error(e.getMessage());
        }
    }

    private void edit() {

        int r = table.getSelectedRow();
        if (r == -1)
            return;

        PatientForm f = new PatientForm(controller.getAll().get(r));

        if (!f.show())
            return;

        try {
            controller.update(r, f.get());
            load();
        } catch (IOException e) {
            error(e.getMessage());
        }
    }

    private void delete() {

        int r = table.getSelectedRow();
        if (r == -1)
            return;

        if (JOptionPane.showConfirmDialog(
                this, "Delete patient?",
                "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            try {
                controller.delete(r);
                load();
            } catch (IOException e) {
                error(e.getMessage());
            }
        }
    }

    private void error(String m) {
        JOptionPane.showMessageDialog(this, m,
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    /* ================= FORM ================= */

    private static class PatientForm {

        private final JTextField[] f = new JTextField[13];
        private final Patient original;

        PatientForm(Patient o) {
            original = o;
        }

        boolean show() {

            JPanel p = new JPanel(new GridLayout(0, 2, 6, 6));

            String[] labels = {
                    "Patient ID (P001)", "First Name", "Last Name",
                    "DOB (YYYY-MM-DD)", "NHS Number",
                    "Gender", "Phone", "Email",
                    "Address", "Postcode",
                    "Emergency Contact", "Emergency Phone",
                    "GP Surgery ID"
            };

            for (int i = 0; i < f.length; i++) {
                f[i] = new JTextField();
                p.add(new JLabel(labels[i]));
                p.add(f[i]);
            }

            if (original != null) {
                f[0].setText(original.getPatientId());
                f[0].setEditable(false);
                f[1].setText(original.getFirstName());
                f[2].setText(original.getLastName());
                f[3].setText(original.getDateOfBirth());
                f[4].setText(original.getNhsNumber());
                f[5].setText(original.getGender());
                f[6].setText(original.getPhone());
                f[7].setText(original.getEmail());
                f[8].setText(original.getAddress());
                f[9].setText(original.getPostcode());
                f[10].setText(original.getEmergencyContact());
                f[11].setText(original.getEmergencyPhone());
                f[12].setText(original.getGpSurgeryId());
            }

            JScrollPane scroll = new JScrollPane(p);
            scroll.setPreferredSize(new Dimension(520, 420));

            while (true) {

                int ok = JOptionPane.showConfirmDialog(
                        null, scroll,
                        original == null ? "Add Patient" : "Edit Patient",
                        JOptionPane.OK_CANCEL_OPTION);

                if (ok != JOptionPane.OK_OPTION)
                    return false;

                if (validate())
                    return true;
            }
        }

        private boolean validate() {

            if (!f[0].getText().matches("P\\d{3}")) {
                error("Patient ID must be P001 format.");
                return false;
            }

            if (!f[4].getText().matches("\\d{10}")) {
                error("NHS number must be 10 digits.");
                return false;
            }

            return true;
        }

        Patient get() {

            String now = LocalDate.now().toString();

            return new Patient(
                    f[0].getText(),
                    f[1].getText(),
                    f[2].getText(),
                    f[3].getText(),
                    f[4].getText(),
                    f[5].getText(),
                    f[6].getText(),
                    f[7].getText(),
                    f[8].getText(),
                    f[9].getText(),
                    f[10].getText(),
                    f[11].getText(),
                    original == null ? now : original.getRegistrationDate(),
                    f[12].getText());
        }

        private void error(String m) {
            JOptionPane.showMessageDialog(null, m,
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
