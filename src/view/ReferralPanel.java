package view;

import model.Referral;
import repository.ReferralManager;
import repository.ReferralRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;

public class ReferralPanel extends JPanel {

    private final ReferralRepository repository = new ReferralRepository();
    private final ReferralManager manager = ReferralManager.getInstance();

    private final DefaultTableModel model;
    private final JTable table;

    public ReferralPanel() {

        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[] {
                "Referral ID", "Patient ID", "Ref Clinician", "To Clinician",
                "From Facility", "To Facility", "Date", "Urgency",
                "Reason", "Summary", "Investigations", "Status",
                "Appointment", "Notes", "Created", "Updated"
        }, 0);

        table = new JTable(model);
        load();

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);
    }

    /* ================= BUTTONS ================= */

    private JPanel createButtons() {

        JPanel p = new JPanel();

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");

        add.addActionListener(e -> addReferral());
        edit.addActionListener(e -> editReferral());
        delete.addActionListener(e -> deleteReferral());

        p.add(add);
        p.add(edit);
        p.add(delete);

        return p;
    }

    /* ================= LOAD ================= */

    private void load() {

        model.setRowCount(0);

        for (Referral r : repository.getAll()) {
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

    private void addReferral() {

        Referral r = referralForm(null);
        if (r == null)
            return;

        try {
            manager.processReferral(r, repository); // ✅ Singleton used
            load();
        } catch (IOException ex) {
            showError(ex.getMessage());
        }
    }

    private void editReferral() {

        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a referral first.");
            return;
        }

        Referral existing = repository.getAll().get(row);
        Referral updated = referralForm(existing);
        if (updated == null)
            return;

        Referral fixed = new Referral(
                updated.getReferralId(),
                updated.getPatientId(),
                updated.getReferringClinicianId(),
                updated.getReferredToClinicianId(),
                updated.getReferringFacilityId(),
                updated.getReferredToFacilityId(),
                updated.getReferralDate(),
                updated.getUrgencyLevel(),
                updated.getReferralReason(),
                updated.getClinicalSummary(),
                updated.getRequestedInvestigations(),
                updated.getStatus(),
                updated.getAppointmentId(),
                updated.getNotes(),
                existing.getCreatedDate(),
                LocalDate.now().toString());

        try {
            repository.delete(row);
            repository.add(fixed);
            load();
        } catch (IOException ex) {
            showError(ex.getMessage());
        }
    }

    private void deleteReferral() {

        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a referral first.");
            return;
        }

        if (JOptionPane.showConfirmDialog(
                this,
                "Delete selected referral?",
                "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            try {
                repository.delete(row);
                load();
            } catch (IOException ex) {
                showError(ex.getMessage());
            }
        }
    }

    /* ================= FORM ================= */

    private Referral referralForm(Referral r) {

        JTextField[] f = new JTextField[14];
        JTextArea summaryArea = new JTextArea(4, 20);

        JComboBox<String> urgencyBox = new JComboBox<>(
                new String[] { "Routine", "Non-urgent", "Urgent" });

        JComboBox<String> statusBox = new JComboBox<>(
                new String[] { "New", "Pending", "In Progress", "Completed" });

        JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));

        String[] labels = {
                "Referral ID", "Patient ID", "Ref Clinician ID", "To Clinician ID",
                "From Facility", "To Facility", "Referral Date",
                "Urgency", "Reason", "Clinical Summary",
                "Investigations", "Status", "Appointment ID", "Notes"
        };

        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i]));

            if (i == 7)
                panel.add(urgencyBox);
            else if (i == 9)
                panel.add(new JScrollPane(summaryArea));
            else if (i == 11)
                panel.add(statusBox);
            else {
                f[i] = new JTextField();
                panel.add(f[i]);
            }
        }

        if (r != null) {
            f[0].setText(r.getReferralId());
            f[0].setEditable(false);
            f[1].setText(r.getPatientId());
            f[2].setText(r.getReferringClinicianId());
            f[3].setText(r.getReferredToClinicianId());
            f[4].setText(r.getReferringFacilityId());
            f[5].setText(r.getReferredToFacilityId());
            f[6].setText(r.getReferralDate());
            urgencyBox.setSelectedItem(r.getUrgencyLevel());
            f[8].setText(r.getReferralReason());
            summaryArea.setText(r.getClinicalSummary());
            f[10].setText(r.getRequestedInvestigations());
            statusBox.setSelectedItem(r.getStatus());
            f[12].setText(r.getAppointmentId());
            f[13].setText(r.getNotes());
        } else {
            f[6].setText(LocalDate.now().toString());
        }

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setPreferredSize(new Dimension(520, 420));

        while (true) {

            int ok = JOptionPane.showConfirmDialog(
                    this,
                    scroll,
                    r == null ? "Add Referral" : "Edit Referral",
                    JOptionPane.OK_CANCEL_OPTION);

            if (ok != JOptionPane.OK_OPTION)
                return null;

            if (!f[0].getText().matches("R\\d{3}")) {
                showError("Referral ID must be like R001.");
                continue;
            }

            if (!f[1].getText().matches("P\\d{3}")) {
                showError("Patient ID must be like P001.");
                continue;
            }

            if (!f[2].getText().matches("C\\d{3}")) {
                showError("Clinician ID must be like C001.");
                continue;
            }

            if (!f[4].getText().matches("[SH]\\d{3}")) {
                showError("Facility ID must be S001 or H001.");
                continue;
            }

            return new Referral(
                    f[0].getText(),
                    f[1].getText(),
                    f[2].getText(),
                    f[3].getText(),
                    f[4].getText(),
                    f[5].getText(),
                    f[6].getText(),
                    urgencyBox.getSelectedItem().toString(),
                    f[8].getText(),
                    summaryArea.getText(),
                    f[10].getText(),
                    statusBox.getSelectedItem().toString(),
                    f[12].getText(),
                    f[13].getText(),
                    LocalDate.now().toString(),
                    LocalDate.now().toString());
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
