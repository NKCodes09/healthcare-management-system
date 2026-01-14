package view;

import model.Staff;
import repository.StaffRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;

public class StaffPanel extends JPanel {

    private final StaffRepository repo = new StaffRepository();
    private final DefaultTableModel model;
    private  JTable table;

    public StaffPanel() {
       

        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[] {
                "ID", "First", "Last", "Role", "Dept", "Facility",
                "Phone", "Email", "Status", "Start", "Manager", "Access"
        }, 0);

        table = new JTable(model);
        load();
        table.setRowHeight(24);
        table.setSelectionBackground(new Color(220, 235, 250));
        table.setSelectionForeground(Color.BLACK);
        table.getTableHeader().setReorderingAllowed(false);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons(), BorderLayout.SOUTH);
    }

    private JPanel buttons() {
        JPanel p = new JPanel();
        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton del = new JButton("Delete");

        add.addActionListener(e -> addStaff());
        edit.addActionListener(e -> editStaff());
        del.addActionListener(e -> deleteStaff());

        p.add(add);
        p.add(edit);
        p.add(del);
        return p;
    }

    private void load() {
        model.setRowCount(0);
        for (Staff s : repo.getAll()) {
            model.addRow(new Object[] {
                    s.getStaffId(), s.getFirstName(), s.getLastName(),
                    s.getRole(), s.getDepartment(), s.getFacilityId(),
                    s.getPhoneNumber(), s.getEmail(), s.getEmploymentStatus(),
                    s.getStartDate(), s.getLineManager(), s.getAccessLevel()
            });
        }
    }

    /* ---------- CRUD ---------- */

    private void addStaff() {
        Staff s = staffForm(null);
        if (s == null)
            return;
        try {
            repo.add(s);
            load();
        } catch (IOException ex) {
            error(ex.getMessage());
        }
    }

    private void editStaff() {
        int r = table.getSelectedRow();
        if (r < 0) {
            error("Select staff");
            return;
        }
        Staff updated = staffForm(repo.getAll().get(r));
        if (updated == null)
            return;
        repo.getAll().set(r, updated);
        try {
            repo.updateAll();
            load();
        } catch (IOException ex) {
            error(ex.getMessage());
        }
    }

    private void deleteStaff() {
        int r = table.getSelectedRow();
        if (r < 0)
            return;
        if (JOptionPane.showConfirmDialog(this, "Delete?", "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                repo.delete(r);
                load();
            } catch (IOException ex) {
                error(ex.getMessage());
            }
        }
    }

    /* ---------- FORM ---------- */

    private Staff staffForm(Staff s) {

        JTextField[] f = new JTextField[12];
        JPanel p = new JPanel(new GridLayout(0, 2, 5, 5));
        String[] lbl = {
                "Staff ID", "First", "Last", "Role", "Dept", "Facility ID",
                "Phone", "Email", "Employment Status",
                "Start Date", "Line Manager", "Access Level"
        };

        for (int i = 0; i < f.length; i++) {
            f[i] = new JTextField();
            p.add(new JLabel(lbl[i]));
            p.add(f[i]);
        }

        if (s != null) {
            f[0].setText(s.getStaffId());
            f[0].setEditable(false);
            f[1].setText(s.getFirstName());
            f[2].setText(s.getLastName());
            f[3].setText(s.getRole());
            f[4].setText(s.getDepartment());
            f[5].setText(s.getFacilityId());
            f[6].setText(s.getPhoneNumber());
            f[7].setText(s.getEmail());
            f[8].setText(s.getEmploymentStatus());
            f[9].setText(s.getStartDate());
            f[10].setText(s.getLineManager());
            f[11].setText(s.getAccessLevel());
        }

        while (true) {
            int ok = JOptionPane.showConfirmDialog(this, p,
                    s == null ? "Add Staff" : "Edit Staff",
                    JOptionPane.OK_CANCEL_OPTION);

            if (ok != JOptionPane.OK_OPTION)
                return null;

            if (!f[0].getText().matches("ST\\d{3}")) {
                error("ID ST001");
                continue;
            }
            if (!f[5].getText().matches("[SH]\\d{3}")) {
                error("Facility S001/H001");
                continue;
            }
            if (!f[6].getText().matches("\\d{10,11}")) {
                error("Phone digits");
                continue;
            }
            if (!f[7].getText().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                error("Email");
                continue;
            }
            try {
                LocalDate.parse(f[9].getText());
            } catch (Exception e) {
                error("Invalid date");
                continue;
            }

            return new Staff(
                    f[0].getText(), f[1].getText(), f[2].getText(),
                    f[3].getText(), f[4].getText(), f[5].getText(),
                    f[6].getText(), f[7].getText(), f[8].getText(),
                    f[9].getText(), f[10].getText(), f[11].getText());
        }
    }

    private void error(String m) {
        JOptionPane.showMessageDialog(this, m, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
