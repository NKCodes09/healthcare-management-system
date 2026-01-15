package view;

import controller.AppointmentController;
import model.Appointment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;

public class AppointmentPanel extends JPanel {

    private final AppointmentController controller = new AppointmentController();
    private final DefaultTableModel model;
    private final JTable table;

    public AppointmentPanel() {

        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[] {
                "ID", "Patient", "Clinician", "Facility",
                "Date", "Time", "Duration",
                "Type", "Status", "Reason", "Notes",
                "Created", "Updated"
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

        for (Appointment a : controller.getAll()) {
            model.addRow(new Object[] {
                    a.getAppointmentId(),
                    a.getPatientId(),
                    a.getClinicianId(),
                    a.getFacilityId(),
                    a.getAppointmentDate(),
                    a.getAppointmentTime(),
                    a.getDurationMinutes(),
                    a.getAppointmentType(),
                    a.getStatus(),
                    a.getReasonForVisit(),
                    a.getNotes(),
                    a.getCreatedDate(),
                    a.getLastModified()
            });
        }
    }

    private void add() {

        AppointmentForm f = new AppointmentForm(null);
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

        AppointmentForm f = new AppointmentForm(controller.getAll().get(r));

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
                this, "Delete appointment?",
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

    private static class AppointmentForm {

        private final JTextField[] f = new JTextField[10];
        private final Appointment original;

        AppointmentForm(Appointment o) {
            original = o;
        }

        boolean show() {

            JPanel p = new JPanel(new GridLayout(0, 2, 6, 6));

            String[] labels = {
                    "Appointment ID (A001)", "Patient ID",
                    "Clinician ID", "Facility ID",
                    "Date (YYYY-MM-DD)", "Time (HH:MM)",
                    "Duration (minutes)", "Type",
                    "Status", "Reason"
            };

            for (int i = 0; i < f.length; i++) {
                f[i] = new JTextField();
                p.add(new JLabel(labels[i]));
                p.add(f[i]);
            }

            if (original != null) {
                f[0].setText(original.getAppointmentId());
                f[0].setEditable(false);
                f[1].setText(original.getPatientId());
                f[2].setText(original.getClinicianId());
                f[3].setText(original.getFacilityId());
                f[4].setText(original.getAppointmentDate());
                f[5].setText(original.getAppointmentTime());
                f[6].setText(original.getDurationMinutes());
                f[7].setText(original.getAppointmentType());
                f[8].setText(original.getStatus());
                f[9].setText(original.getReasonForVisit());
            }

            JScrollPane scroll = new JScrollPane(p);
            scroll.setPreferredSize(new Dimension(450, 360));

            while (true) {

                int ok = JOptionPane.showConfirmDialog(
                        null, scroll,
                        original == null ? "Add Appointment" : "Edit Appointment",
                        JOptionPane.OK_CANCEL_OPTION);

                if (ok != JOptionPane.OK_OPTION)
                    return false;

                if (validate())
                    return true;
            }
        }

        private boolean validate() {

            if (!f[0].getText().matches("A\\d{3}")) {
                error("Appointment ID must be A001 format.");
                return false;
            }

            if (!f[4].getText().matches("\\d{4}-\\d{2}-\\d{2}")
                    || LocalDate.parse(f[4].getText()).isBefore(LocalDate.now().minusDays(1))) {
                error("Invalid appointment date.");
                return false;
            }

            if (!f[6].getText().matches("\\d+")) {
                error("Duration must be numeric.");
                return false;
            }

            return true;
        }

        Appointment get() {

            String now = LocalDate.now().toString();

            return new Appointment(
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
                    "",
                    original == null ? now : original.getCreatedDate(),
                    now);
        }

        private void error(String m) {
            JOptionPane.showMessageDialog(null, m,
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
