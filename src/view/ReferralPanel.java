package view;

import model.Referral;
import repository.ReferralRepository;
import repository.ReferralManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReferralPanel extends JPanel {

    private final ReferralRepository repository;
    private final JTable table;
    private final DefaultTableModel model;

    public ReferralPanel() {

        setLayout(new BorderLayout());
        repository = new ReferralRepository();

        model = new DefaultTableModel(
                new String[] {
                        "Referral ID",
                        "Patient ID",
                        "Clinician ID",
                        "Date",
                        "Urgency",
                        "Status"
                }, 0);

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton create = new JButton("Create Referral (Singleton)");
        JButton edit = new JButton("Update Status");
        JButton delete = new JButton("Delete");

        create.addActionListener(e -> createReferral());
        edit.addActionListener(e -> editReferral());
        delete.addActionListener(e -> deleteReferral());

        JPanel buttons = new JPanel();
        buttons.add(create);
        buttons.add(edit);
        buttons.add(delete);

        add(buttons, BorderLayout.SOUTH);

        loadReferrals();
    }

    private void loadReferrals() {
        model.setRowCount(0);

        for (Referral r : repository.getAll()) {
            model.addRow(new Object[] {
                    r.getReferralId(),
                    r.getPatientId(),
                    r.getReferringClinicianId(),
                    r.getReferralDate(),
                    r.getUrgencyLevel(),
                    r.getStatus()
            });
        }
    }

    private void createReferral() {
        try {
            Referral r = new Referral(
                    JOptionPane.showInputDialog(this, "Referral ID"),
                    JOptionPane.showInputDialog(this, "Patient ID"),
                    JOptionPane.showInputDialog(this, "Referring Clinician ID"),
                    JOptionPane.showInputDialog(this, "Referral Date (YYYY-MM-DD)"),
                    JOptionPane.showInputDialog(this, "Urgency Level"),
                    JOptionPane.showInputDialog(this, "Referral Reason"),
                    JOptionPane.showInputDialog(this, "Clinical Summary"),
                    "New");

            ReferralManager.getInstance().processReferral(r, repository);
            loadReferrals();

            JOptionPane.showMessageDialog(this, "Referral created successfully.");

        } catch (Exception e) {
            showError(e);
        }
    }

    private void editReferral() {

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a referral first.");
            return;
        }

        try {
            String id = model.getValueAt(row, 0).toString();
            String newStatus = JOptionPane.showInputDialog(
                    this,
                    "Update Status",
                    model.getValueAt(row, 5));

            if (newStatus == null || newStatus.trim().isEmpty())
                return;

            repository.updateStatus(id, newStatus);
            loadReferrals();

        } catch (Exception e) {
            showError(e);
        }
    }

    private void deleteReferral() {

        int row = table.getSelectedRow();
        if (row == -1)
            return;

        try {
            String id = model.getValueAt(row, 0).toString();
            repository.deleteReferral(id);
            loadReferrals();

        } catch (Exception e) {
            showError(e);
        }
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
