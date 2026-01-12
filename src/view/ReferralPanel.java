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
                new String[] { "Referral ID", "Patient NHS", "Clinician ID", "Urgency", "Status" }, 0);

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton create = new JButton("Create Referral (Singleton)");
        JButton delete = new JButton("Delete");

        create.addActionListener(e -> createReferral());
        delete.addActionListener(e -> deleteReferral());

        JPanel buttons = new JPanel();
        buttons.add(create);
        buttons.add(delete);
        add(buttons, BorderLayout.SOUTH);

        loadReferrals();
    }

    private void loadReferrals() {
        model.setRowCount(0);

        for (Referral r : repository.getAll()) {
            model.addRow(new Object[] {
                    r.getReferralId(),
                    r.getPatientNhsNumber(),
                    r.getReferringClinicianId(),
                    r.getUrgencyLevel(),
                    r.getStatus()
            });
        }
    }

    private void createReferral() {
        try {
            Referral r = new Referral(
                    JOptionPane.showInputDialog(this, "Referral ID"),
                    JOptionPane.showInputDialog(this, "Patient NHS"),
                    JOptionPane.showInputDialog(this, "Clinician ID"),
                    JOptionPane.showInputDialog(this, "From Facility"),
                    JOptionPane.showInputDialog(this, "To Facility"),
                    JOptionPane.showInputDialog(this, "Clinical Summary"),
                    JOptionPane.showInputDialog(this, "Urgency"),
                    JOptionPane.showInputDialog(this, "Referral Date"),
                    JOptionPane.showInputDialog(this, "Reason"),
                    "", "New", "");

            ReferralManager
                    .getInstance()
                    .processReferral(r, repository);

            loadReferrals();

            JOptionPane.showMessageDialog(this, "Referral created successfully.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    private void editReferral() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        try {
            String id = model.getValueAt(row, 0).toString();

            String newStatus = JOptionPane.showInputDialog(
                    this,
                    "Update Status",
                    model.getValueAt(row, 3) // column index of status
            );

            if (newStatus == null || newStatus.trim().isEmpty())
                return;

            Referral updated = new Referral(
                    id,
                    "", "", "", "",
                    model.getValueAt(row, 1).toString(), // summary
                    model.getValueAt(row, 2).toString(), // urgency
                    "", "", "",
                    newStatus,
                    "");

            repository.updateReferral(updated);
            loadReferrals(); // ✅ reload table

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void deleteReferral() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        try {
            String id = model.getValueAt(row, 0).toString();
            repository.deleteReferral(id); // same repo
            loadReferrals();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
