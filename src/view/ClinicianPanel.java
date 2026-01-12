package view;

import model.Clinician;
import repository.ClinicianRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClinicianPanel extends JPanel {

    private final ClinicianRepository repository;
    private final JTable table;
    private final DefaultTableModel model;

    public ClinicianPanel() {
        setLayout(new BorderLayout());
        repository = new ClinicianRepository();

        model = new DefaultTableModel(
                new String[] { "Clinician ID", "Name", "Role", "Specialty", "Workplace" }, 0);

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton add = new JButton("Add");
        JButton delete = new JButton("Delete");

        add.addActionListener(e -> addClinician());
        delete.addActionListener(e -> deleteClinician());

        JPanel buttons = new JPanel();
        buttons.add(add);
        buttons.add(delete);
        add(buttons, BorderLayout.SOUTH);

        loadClinicians();
    }

    private void loadClinicians() {
        model.setRowCount(0);

        for (Clinician c : repository.getAll()) {
            model.addRow(new Object[] {
                    c.getClinicianId(),
                    c.getName(),
                    c.getRole(),
                    c.getSpecialty(),
                    c.getWorkplace()
            });
        }
    }

    private void addClinician() {
        try {
            Clinician c = new Clinician(
                    JOptionPane.showInputDialog(this, "Clinician ID"),
                    JOptionPane.showInputDialog(this, "Name"),
                    JOptionPane.showInputDialog(this, "Role"),
                    JOptionPane.showInputDialog(this, "Specialty"),
                    JOptionPane.showInputDialog(this, "Workplace"));

            repository.add(c);
            loadClinicians();
        } catch (Exception e) {
            showError(e);
        }
    }

    private void deleteClinician() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        try {
            repository.delete(model.getValueAt(row, 0).toString());
            loadClinicians();
        } catch (Exception e) {
            showError(e);
        }
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
