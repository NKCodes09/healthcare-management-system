package view;

import controller.ReferralController;
import model.Referral;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ReferralPanel extends JPanel {

    private final ReferralController controller = new ReferralController();
    private final DefaultTableModel model;
    private final JTable table;

    public ReferralPanel() {

        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[] {
                "Referral ID", "Patient ID",
                "Referring Clinician", "Referred Clinician",
                "Referring Facility", "Referred Facility",
                "Referral Date", "Urgency",
                "Reason", "Clinical Summary",
                "Investigations", "Status",
                "Appointment ID", "Notes",
                "Created Date", "Last Updated"
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

        for (Referral r : controller.getAll()) {
            model.addRow(new Object[] {
                    r.getReferralId(),
                    r.getPatientId(),
                    r.getReferringClinicianId(),
                    r.getReferredToClinicianId(),
                    r.getReferringFacilityId(),
                    r.getReferredToFacilityId(),
                    r.getReferralDate(),
                    r.getUrgencyLevel(),
                    r.getReferralReason(),
                    r.getClinicalSummary(),
                    r.getRequestedInvestigations(),
                    r.getStatus(),
                    r.getAppointmentId(),
                    r.getNotes(),
                    r.getCreatedDate(),
                    r.getLastUpdated()
            });
        }
    }

    /* ================= CRUD ================= */

    private void add() {

        ReferralForm f = new ReferralForm(null);
        if (!f.showDialog())
            return;

        try {
            controller.add(f.getReferral());
            load();
        } catch (IOException ex) {
            error(ex.getMessage());
        }
    }

    private void edit() {

        int r = table.getSelectedRow();
        if (r == -1)
            return;

        Referral old = controller.getAll().get(r);
        ReferralForm f = new ReferralForm(old);

        if (!f.showDialog())
            return;

        try {
            controller.update(r, f.getReferral());
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
                this, "Delete referral?",
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
        JOptionPane.showMessageDialog(this, m,
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    /*
     * =====================================================
     * INNER REFERRAL FORM (MVC-CORRECT)
     * =====================================================
     */

    private static class ReferralForm {

        private final JTextField[] f = new JTextField[14];
        private final JComboBox<String> urgency = new JComboBox<>(new String[] { "Low", "Medium", "High", "Urgent" });
        private final JComboBox<String> status = new JComboBox<>(
                new String[] { "Pending", "Accepted", "Completed", "Rejected" });

        private final Referral original;

        ReferralForm(Referral r) {
            original = r;
        }

        boolean showDialog() {

            JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));

            String[] labels = {
                    "Referral ID", "Patient ID",
                    "Referring Clinician", "Referred Clinician",
                    "Referring Facility", "Referred Facility",
                    "Referral Date (YYYY-M-D)",
                    "Urgency",
                    "Reason",
                    "Clinical Summary",
                    "Investigations",
                    "Status",
                    "Appointment ID",
                    "Notes"
            };

            for (int i = 0; i < f.length; i++) {
                f[i] = new JTextField();
                panel.add(new JLabel(labels[i]));

                if (i == 7)
                    panel.add(urgency);
                else if (i == 11)
                    panel.add(status);
                else
                    panel.add(f[i]);
            }

            if (original != null) {
                f[0].setText(original.getReferralId());
                f[0].setEditable(false);
                f[1].setText(original.getPatientId());
                f[2].setText(original.getReferringClinicianId());
                f[3].setText(original.getReferredToClinicianId());
                f[4].setText(original.getReferringFacilityId());
                f[5].setText(original.getReferredToFacilityId());
                f[6].setText(original.getReferralDate());
                urgency.setSelectedItem(original.getUrgencyLevel());
                f[8].setText(original.getReferralReason());
                f[9].setText(original.getClinicalSummary());
                f[10].setText(original.getRequestedInvestigations());
                status.setSelectedItem(original.getStatus());
                f[12].setText(original.getAppointmentId());
                f[13].setText(original.getNotes());
            }

            JScrollPane scroll = new JScrollPane(panel);
            scroll.setPreferredSize(new Dimension(520, 480));

            while (true) {

                int ok = JOptionPane.showConfirmDialog(
                        null, scroll,
                        original == null ? "Add Referral" : "Edit Referral",
                        JOptionPane.OK_CANCEL_OPTION);

                if (ok != JOptionPane.OK_OPTION)
                    return false;

                if (validate())
                    return true;
            }
        }

        /* ================= VALIDATION ================= */

        private boolean validate() {

            if (!f[0].getText().matches("R\\d{3}")) {
                error("Referral ID must be R001 format.");
                return false;
            }

            if (!f[1].getText().matches("P\\d{3}")) {
                error("Patient ID must be P001.");
                return false;
            }

            if (parseDate(f[6].getText()) == null) {
                error("Invalid referral date.");
                return false;
            }

            if (f[8].getText().trim().length() < 3) {
                error("Referral reason required.");
                return false;
            }

            return true;
        }

        private LocalDate parseDate(String s) {
            try {
                return LocalDate.parse(s.trim(),
                        DateTimeFormatter.ofPattern("yyyy-M-d"));
            } catch (DateTimeParseException e) {
                return null;
            }
        }

        private void error(String m) {
            JOptionPane.showMessageDialog(null, m,
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        }

        Referral getReferral() {

            String today = LocalDate.now().toString();

            return new Referral(
                    f[0].getText().trim(),
                    f[1].getText().trim(),
                    f[2].getText().trim(),
                    f[3].getText().trim(),
                    f[4].getText().trim(),
                    f[5].getText().trim(),
                    f[6].getText().trim(),
                    urgency.getSelectedItem().toString(),
                    f[8].getText().trim(),
                    f[9].getText().trim(),
                    f[10].getText().trim(),
                    status.getSelectedItem().toString(),
                    f[12].getText().trim(),
                    f[13].getText().trim(),
                    original == null ? today : original.getCreatedDate(),
                    today);
        }
    }
}
