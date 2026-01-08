package view;

import model.Patient;
import repository.PatientRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PatientPanel extends JPanel {

    private final PatientRepository repository;
    private final JTable table;
    private final DefaultTableModel model;

    public PatientPanel() {

        setLayout(new BorderLayout());

        repository = new PatientRepository();

        model = new DefaultTableModel(
                new String[] {
                        "NHS Number",
                        "First Name",
                        "Last Name",
                        "DOB",
                        "Phone",
                        "Gender",
                        "GP Surgery"
                }, 0);

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton add = new JButton("Add");
        JButton delete = new JButton("Delete");

        add.addActionListener(e -> addPatient());
        delete.addActionListener(e -> deletePatient());

        JPanel buttons = new JPanel();
        buttons.add(add);
        buttons.add(delete);

        add(buttons, BorderLayout.SOUTH);

        loadPatients();
    }

    private void loadPatients() {
        try {
            repository.load("data/patients.csv");

            model.setRowCount(0);

            for (Patient p : repository.getAll()) {
                model.addRow(new Object[] {
                        p.getNhsNumber(),
                        p.getFirstName(),
                        p.getLastName(),
                        p.getDateOfBirth(),
                        p.getPhoneNumber(),
                        p.getGender(),
                        p.getRegisteredGpSurgery()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load patients file:\n" + e.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addPatient() {
        try {
            String nhs = JOptionPane.showInputDialog(this, "NHS Number");
            if (nhs == null || nhs.isEmpty())
                return;

            Patient p = new Patient(
                    nhs,
                    JOptionPane.showInputDialog(this, "First Name"),
                    JOptionPane.showInputDialog(this, "Last Name"),
                    JOptionPane.showInputDialog(this, "DOB"),
                    JOptionPane.showInputDialog(this, "Phone"),
                    JOptionPane.showInputDialog(this, "Gender"),
                    JOptionPane.showInputDialog(this, "GP Surgery"));

            repository.addPatient(p);
            loadPatients();

            JOptionPane.showMessageDialog(this, "Patient added successfully.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add patient:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePatient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient first.");
            return;
        }

        try {
            String nhs = model.getValueAt(row, 0).toString();

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Delete patient with NHS: " + nhs + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION)
                return;

            repository.deletePatient(nhs);
            loadPatients();

            JOptionPane.showMessageDialog(this, "Patient deleted successfully.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete patient:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
