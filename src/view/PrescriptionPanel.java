package view;

import model.Prescription;
import repository.PrescriptionRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PrescriptionPanel extends JPanel {

    private final PrescriptionRepository repository;
    private final JTable table;
    private final DefaultTableModel model;

    public PrescriptionPanel() {
        setLayout(new BorderLayout());
        repository = new PrescriptionRepository();

        model = new DefaultTableModel(
                new String[] { "ID", "Patient NHS", "Clinician ID", "Medication", "Dosage", "Pharmacy", "Status" }, 0);

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton add = new JButton("Add");
        JButton delete = new JButton("Delete");

        add.addActionListener(e -> addPrescription());
        delete.addActionListener(e -> deletePrescription());

        JPanel buttons = new JPanel();
        buttons.add(add);
        buttons.add(delete);
        add(buttons, BorderLayout.SOUTH);

        loadPrescriptions();
    }

    private void loadPrescriptions() {
        try {
            repository.load("data/prescriptions.csv");
            model.setRowCount(0);

            for (Prescription p : repository.getAll()) {
                model.addRow(new Object[] {
                        p.getPrescriptionId(),
                        p.getPatientNhsNumber(),
                        p.getClinicianId(),
                        p.getMedication(),
                        p.getDosage(),
                        p.getPharmacy(),
                        p.getCollectionStatus()
                });
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    private void addPrescription() {
        try {
            Prescription p = new Prescription(
                    JOptionPane.showInputDialog(this, "Prescription ID"),
                    JOptionPane.showInputDialog(this, "Patient NHS"),
                    JOptionPane.showInputDialog(this, "Clinician ID"),
                    JOptionPane.showInputDialog(this, "Medication"),
                    JOptionPane.showInputDialog(this, "Dosage"),
                    JOptionPane.showInputDialog(this, "Pharmacy"),
                    JOptionPane.showInputDialog(this, "Status"));

            repository.addPrescription(p);
            loadPrescriptions();
        } catch (Exception e) {
            showError(e);
        }
    }

    private void deletePrescription() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        try {
            repository.deletePrescription(model.getValueAt(row, 0).toString());
            loadPrescriptions();
        } catch (Exception e) {
            showError(e);
        }
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
