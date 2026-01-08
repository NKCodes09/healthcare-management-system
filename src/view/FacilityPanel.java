package view;

import model.Facility;
import repository.FacilityRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FacilityPanel extends JPanel {

    private final FacilityRepository repository;
    private final JTable table;
    private final DefaultTableModel model;

    public FacilityPanel() {
        setLayout(new BorderLayout());
        repository = new FacilityRepository();

        model = new DefaultTableModel(
                new String[] { "Facility ID", "Name", "Type", "Location" }, 0);

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton add = new JButton("Add");
        JButton delete = new JButton("Delete");

        add.addActionListener(e -> addFacility());
        delete.addActionListener(e -> deleteFacility());

        JPanel buttons = new JPanel();
        buttons.add(add);
        buttons.add(delete);
        add(buttons, BorderLayout.SOUTH);

        loadFacilities();
    }

    private void loadFacilities() {
        try {
            repository.load("data/facilities.csv");
            model.setRowCount(0);

            for (Facility f : repository.getAll()) {
                model.addRow(new Object[] {
                        f.getFacilityId(),
                        f.getFacilityName(),
                        f.getFacilityType(),
                        f.getLocation()
                });
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    private void addFacility() {
        try {
            Facility f = new Facility(
                    JOptionPane.showInputDialog(this, "Facility ID"),
                    JOptionPane.showInputDialog(this, "Name"),
                    JOptionPane.showInputDialog(this, "Type"),
                    JOptionPane.showInputDialog(this, "Location"));

            repository.addFacility(f);
            loadFacilities();
        } catch (Exception e) {
            showError(e);
        }
    }

    private void deleteFacility() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        try {
            repository.deleteFacility(model.getValueAt(row, 0).toString());
            loadFacilities();
        } catch (Exception e) {
            showError(e);
        }
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
