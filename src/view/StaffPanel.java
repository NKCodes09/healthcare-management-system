package view;

import controller.StaffController;
import model.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

public class StaffPanel extends JPanel {

    private final StaffController controller = new StaffController();
    private final DefaultTableModel model;
    private final JTable table;

    public StaffPanel() {

        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[] {
                "ID", "First", "Last", "Role", "Dept",
                "Facility", "Phone", "Email",
                "Status", "Start Date", "Manager", "Access"
        }, 0);

        table = new JTable(model);
        table.setRowHeight(24);

        load();

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons(), BorderLayout.SOUTH);
    }

    private JPanel buttons() {

        JPanel p = new JPanel();

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton del = new JButton("Delete");

        add.addActionListener(e -> add());
        edit.addActionListener(e -> edit());
        del.addActionListener(e -> delete());

        p.add(add);
        p.add(edit);
        p.add(del);

        return p;
    }

    private void load() {

        model.setRowCount(0);

        for (Staff s : controller.getAll()) {
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

    private void add() {

        StaffForm f = new StaffForm(null);
        if (!f.show())
            return;

        try {
            controller.add(f.get());
            load();
        } catch (IOException e) {
            error(e.getMessage());
        }
    }

    private void edit() {

        int r = table.getSelectedRow();
        if (r == -1)
            return;

        StaffForm f = new StaffForm(controller.getAll().get(r));

        if (!f.show())
            return;

        try {
            controller.update(r, f.get());
            load();
        } catch (IOException e) {
            error(e.getMessage());
        }
    }

    private void delete() {

        int r = table.getSelectedRow();
        if (r == -1)
            return;

        if (JOptionPane.showConfirmDialog(
                this, "Delete staff member?",
                "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            try {
                controller.delete(r);
                load();
            } catch (IOException e) {
                error(e.getMessage());
            }
        }
    }

    private void error(String m) {
        JOptionPane.showMessageDialog(this, m,
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    /* ================= FORM ================= */

    private static class StaffForm {

        private final JTextField[] f = new JTextField[12];
        private final Staff original;

        StaffForm(Staff o) {
            original = o;
        }

        boolean show() {

            JPanel p = new JPanel(new GridLayout(0, 2, 6, 6));

            String[] labels = {
                    "Staff ID (ST001)", "First Name", "Last Name",
                    "Role", "Department", "Facility ID",
                    "Phone", "Email", "Employment Status",
                    "Start Date (YYYY-MM-DD)", "Line Manager", "Access Level"
            };

            for (int i = 0; i < f.length; i++) {
                f[i] = new JTextField();
                p.add(new JLabel(labels[i]));
                p.add(f[i]);
            }

            if (original != null) {
                f[0].setText(original.getStaffId());
                f[0].setEditable(false);
                f[1].setText(original.getFirstName());
                f[2].setText(original.getLastName());
                f[3].setText(original.getRole());
                f[4].setText(original.getDepartment());
                f[5].setText(original.getFacilityId());
                f[6].setText(original.getPhoneNumber());
                f[7].setText(original.getEmail());
                f[8].setText(original.getEmploymentStatus());
                f[9].setText(original.getStartDate());
                f[10].setText(original.getLineManager());
                f[11].setText(original.getAccessLevel());
            }

            JScrollPane scroll = new JScrollPane(p);
            scroll.setPreferredSize(new Dimension(500, 420));

            while (true) {

                int ok = JOptionPane.showConfirmDialog(
                        null, scroll,
                        original == null ? "Add Staff" : "Edit Staff",
                        JOptionPane.OK_CANCEL_OPTION);

                if (ok != JOptionPane.OK_OPTION)
                    return false;

                if (validate())
                    return true;
            }
        }

        private boolean validate() {

            if (!f[0].getText().matches("ST\\d{3}")) {
                error("Staff ID must be ST001 format.");
                return false;
            }

            if (!f[7].getText().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                error("Invalid email.");
                return false;
            }

            return true;
        }

        Staff get() {

            return new Staff(
                    f[0].getText(),
                    f[1].getText(),
                    f[2].getText(),
                    f[3].getText(),
                    f[4].getText(),
                    f[5].getText(),
                    f[6].getText(),
                    f[7].getText(),
                    f[8].getText(),
                    f[9].getText(),
                    f[10].getText(),
                    f[11].getText());
        }

        private void error(String m) {
            JOptionPane.showMessageDialog(null, m,
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
