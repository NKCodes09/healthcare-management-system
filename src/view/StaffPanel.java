package view;

import model.Staff;
import repository.StaffRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StaffPanel extends JPanel {

    private final StaffRepository repository;
    private final JTable table;
    private final DefaultTableModel model;

    public StaffPanel() {
        setLayout(new BorderLayout());
        repository = new StaffRepository();

        model = new DefaultTableModel(
                new String[] { "Staff ID", "Name", "Role", "Department" }, 0);

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton add = new JButton("Add");
        JButton delete = new JButton("Delete");

        add.addActionListener(e -> addStaff());
        delete.addActionListener(e -> deleteStaff());

        JPanel buttons = new JPanel();
        buttons.add(add);
        buttons.add(delete);
        add(buttons, BorderLayout.SOUTH);

        loadStaff();
    }

    private void loadStaff() {
        try {
            repository.load("data/staff.csv");
            model.setRowCount(0);

            for (Staff s : repository.getAll()) {
                model.addRow(new Object[] {
                        s.getStaffId(),
                        s.getName(),
                        s.getRole(),
                        s.getDepartment()
                });
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    private void addStaff() {
        try {
            Staff s = new Staff(
                    JOptionPane.showInputDialog(this, "Staff ID"),
                    JOptionPane.showInputDialog(this, "Name"),
                    JOptionPane.showInputDialog(this, "Role"),
                    JOptionPane.showInputDialog(this, "Department"));

            repository.addStaff(s);
            loadStaff();
        } catch (Exception e) {
            showError(e);
        }
    }

    private void deleteStaff() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;

        try {
            repository.deleteStaff(model.getValueAt(row, 0).toString());
            loadStaff();
        } catch (Exception e) {
            showError(e);
        }
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
