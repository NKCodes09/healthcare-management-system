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

    private final DefaultTableModel tableModel;
    private final JTable table;

    public ReferralPanel() {

        setLayout(new BorderLayout());

        String[] columns = {
                "Referral ID", "Patient ID", "Referring Clinician", "Referred Clinician",
                "Referring Facility", "Referred Facility", "Referral Date", "Urgency",
                "Reason", "Clinical Summary", "Investigations", "Status",
                "Appointment ID", "Notes", "Created Date", "Last Updated"
        };

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        loadData();

        JPanel buttons = new JPanel();

        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");

        buttons.add(addBtn);
        buttons.add(editBtn);
        buttons.add(deleteBtn);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addReferral());
        editBtn.addActionListener(e -> editReferral());
        deleteBtn.addActionListener(e -> deleteReferral());
    }

    private void loadData() {

        tableModel.setRowCount(0);

        for (Referral r : repository.getAll()) {
            tableModel.addRow(new Object[] {
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

    /* ================= ADD ================= */

    private void addReferral() {

        ReferralFormDialog form = new ReferralFormDialog(null);
        Referral r = form.showDialog(); // ✅ Patient-style form

        if (r != null) {
            try {
                manager.processReferral(r, repository); // ✅ Singleton
                loadData();
            } catch (IOException ex) {
                showError(ex.getMessage());
            }
        }
    }

    /* ================= EDIT ================= */

    private void editReferral() {

        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a referral to edit.");
            return;
        }

        Referral existing = repository.getAll().get(row);

        ReferralFormDialog form = new ReferralFormDialog(existing);
        Referral updated = form.showDialog(); // ✅ same pattern as Patient

        if (updated != null) {

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
                    updated.getCreatedDate(), // 🔒 keep created
                    LocalDate.now().toString() // ✅ update timestamp
            );

            try {
                repository.deleteReferral(row);
                repository.addReferral(fixed);
                loadData();
            } catch (IOException ex) {
                showError(ex.getMessage());
            }
        }
    }

    /* ================= DELETE ================= */

    private void deleteReferral() {

        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a referral to delete.");
            return;
        }

        if (JOptionPane.showConfirmDialog(
                this,
                "Delete selected referral?",
                "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            try {
                repository.deleteReferral(row);
                loadData();
            } catch (IOException ex) {
                showError(ex.getMessage());
            }
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg,
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
