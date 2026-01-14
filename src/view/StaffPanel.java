package view;

import model.Staff;
import repository.StaffRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

public class StaffPanel extends JPanel {

    private final StaffRepository repository = new StaffRepository();
    private final DefaultTableModel model;
    private final JTable table;

    public StaffPanel() {

        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[] {
                "Staff ID", "First Name", "Last Name", "Role",
                "Department", "Facility ID", "Phone",
                "Email", "Employment Status",
                "Start Date", "Line Manager", "Access Level"
        }, 0);

        table = new JTable(model);
        loadStaff();

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);
    }

    private JPanel createButtons() {

        JPanel p = new JPanel();

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");

        add.addActionListener(e -> addStaff());
        edit.addActionListener(e -> editStaff());
        delete.addActionListener(e -> deleteStaff());

        p.add(add);
        p.add(edit);
        p.add(delete);

        return p;
    }

    private void loadStaff() {

        model.setRowCount(0);

        for (Staff s : repository.getAll()) {
            model.addRow(new Object[] {
                    s.getStaffId(),
                    s.getFirstName(),
                    s.getLastName(),
                    s.getRole(),
                    s.getDepartment(),
                    s.getFacilityId(),
                    s.getPhoneNumber(),
                    s.getEmail(),
                    s.getEmploymentStatus(),
                    s.getStartDate(),
                    s.getLineManager(),
                    s.getAccessLevel()
            });
        }
    }

    private void addStaff() {

        StaffForm form = new StaffForm(null);
        if (!form.showDialog())
            return;

        try {
            repository.addStaff(form.getStaff());
            loadStaff();
        } catch (IOException ex) {
            showError(ex.getMessage());
        }
    }

    private void editStaff() {

        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a staff member first.");
            return;
        }

        Staff existing = repository.getAll().get(row);
        StaffForm form = new StaffForm(existing);

        if (!form.showDialog())
            return;

        repository.getAll().set(row, form.getStaff());

        try {
            repository.updateAll();
            loadStaff();
        } catch (IOException ex) {
            showError(ex.getMessage());
        }
    }

    private void deleteStaff() {

        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a staff member first.");
            return;
        }

        if (JOptionPane.showConfirmDialog(
                this,
                "Delete selected staff member?",
                "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            try {
                repository.deleteStaff(row);
                loadStaff();
            } catch (IOException ex) {
                showError(ex.getMessage());
            }
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this,
                msg, "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
