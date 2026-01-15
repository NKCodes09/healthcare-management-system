package view;

import controller.ClinicianController;
import model.Clinician;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

public class ClinicianPanel extends JPanel {

    private final ClinicianController controller = new ClinicianController();
    private final DefaultTableModel model;
    private final JTable table;

    public ClinicianPanel() {

        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[] {
                "ID", "First", "Last", "Title", "Speciality",
                "GMC", "Phone", "Email",
                "Workplace ID", "Workplace Type",
                "Status", "Start Date"
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

        for (Clinician c : controller.getAll()) {
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

    private void add() {
        ClinicianForm f = new ClinicianForm(null);
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

        ClinicianForm f = new ClinicianForm(controller.getAll().get(r));

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
                this, "Delete clinician?",
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

    private static class ClinicianForm {

        private final JTextField[] f = new JTextField[11];
        private final Clinician original;

        ClinicianForm(Clinician o) {
            original = o;
        }

        boolean show() {

            JPanel p = new JPanel(new GridLayout(0, 2, 6, 6));

            String[] labels = {
                    "Clinician ID (C001)",
                    "First Name",
                    "Last Name",
                    "Title",
                    "Speciality",
                    "GMC / NMC Number",
                    "Phone",
                    "Email",
                    "Workplace ID",
                    "Workplace Type",
                    "Employment Status"
            };

            for (int i = 0; i < f.length; i++) {
                f[i] = new JTextField();
                p.add(new JLabel(labels[i]));
                p.add(f[i]);
            }

            if (original != null) {
                f[0].setText(original.getClinicianId());
                f[0].setEditable(false);
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
            }

            JScrollPane scroll = new JScrollPane(p);
            scroll.setPreferredSize(new Dimension(520, 420));

            while (true) {

                int ok = JOptionPane.showConfirmDialog(
                        null, scroll,
                        original == null ? "Add Clinician" : "Edit Clinician",
                        JOptionPane.OK_CANCEL_OPTION);

                if (ok != JOptionPane.OK_OPTION)
                    return false;

                if (validate())
                    return true;
            }
        }

        private boolean validate() {

            if (!f[0].getText().matches("C\\d{3}")) {
                error("Clinician ID must be C001 format.");
                return false;
            }

            if (!f[7].getText().contains("@")) {
                error("Invalid email.");
                return false;
            }

            return true;
        }

        Clinician get() {

            String start = original == null ? java.time.LocalDate.now().toString()
                    : original.getStartDate();

            return new Clinician(
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
                    start);
        }

        private void error(String m) {
            JOptionPane.showMessageDialog(null, m,
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
