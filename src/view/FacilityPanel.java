package view;

import controller.FacilityController;
import model.Facility;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

public class FacilityPanel extends JPanel {

    private final FacilityController controller = new FacilityController();
    private final DefaultTableModel model;
    private final JTable table;

    public FacilityPanel() {

        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[] {
                "Facility ID", "Name", "Type", "Address", "Postcode",
                "Phone", "Email", "Opening Hours",
                "Manager", "Capacity", "Specialities"
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

        for (Facility f : controller.getAll()) {
            model.addRow(new Object[] {
                    f.getFacilityId(),
                    f.getFacilityName(),
                    f.getFacilityType(),
                    f.getAddress(),
                    f.getPostcode(),
                    f.getPhoneNumber(),
                    f.getEmail(),
                    f.getOpeningHours(),
                    f.getManagerName(),
                    f.getCapacity(),
                    f.getSpecialitiesOffered()
            });
        }
    }

    private void add() {

        FacilityForm form = new FacilityForm(null);
        if (!form.show())
            return;

        try {
            controller.add(form.get());
            load();
        } catch (IOException e) {
            error(e.getMessage());
        }
    }

    private void edit() {

        int r = table.getSelectedRow();
        if (r == -1)
            return;

        FacilityForm form = new FacilityForm(controller.getAll().get(r));
        if (!form.show())
            return;

        try {
            controller.update(r, form.get());
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
                this, "Delete facility?",
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

    private static class FacilityForm {

        private final JTextField[] f = new JTextField[11];
        private final Facility original;

        FacilityForm(Facility o) {
            original = o;
        }

        boolean show() {

            JPanel p = new JPanel(new GridLayout(0, 2, 6, 6));

            String[] labels = {
                    "Facility ID (S001/H001)", "Name", "Type",
                    "Address", "Postcode", "Phone",
                    "Email", "Opening Hours",
                    "Manager", "Capacity",
                    "Specialities (| separated)"
            };

            for (int i = 0; i < f.length; i++) {
                f[i] = new JTextField();
                p.add(new JLabel(labels[i]));
                p.add(f[i]);
            }

            if (original != null) {
                f[0].setText(original.getFacilityId());
                f[0].setEditable(false);
                f[1].setText(original.getFacilityName());
                f[2].setText(original.getFacilityType());
                f[3].setText(original.getAddress());
                f[4].setText(original.getPostcode());
                f[5].setText(original.getPhoneNumber());
                f[6].setText(original.getEmail());
                f[7].setText(original.getOpeningHours());
                f[8].setText(original.getManagerName());
                f[9].setText(original.getCapacity());
                f[10].setText(original.getSpecialitiesOffered());
            }

            JScrollPane scroll = new JScrollPane(p);
            scroll.setPreferredSize(new Dimension(500, 420));

            while (true) {
                int ok = JOptionPane.showConfirmDialog(
                        null, scroll,
                        original == null ? "Add Facility" : "Edit Facility",
                        JOptionPane.OK_CANCEL_OPTION);

                if (ok != JOptionPane.OK_OPTION)
                    return false;

                if (validate())
                    return true;
            }
        }

        private boolean validate() {

            if (!f[0].getText().matches("(S|H)\\d{3}")) {
                error("Facility ID must be S001 or H001.");
                return false;
            }

            if (f[1].getText().length() < 3) {
                error("Name required.");
                return false;
            }

            if (!f[4].getText().matches("[A-Z]\\d{1,2}\\s?\\d[A-Z]{2}")) {
                error("Invalid postcode.");
                return false;
            }

            if (!f[6].getText().matches(".+@.+\\..+")) {
                error("Invalid email.");
                return false;
            }

            if (!f[9].getText().matches("\\d+")) {
                error("Capacity must be numeric.");
                return false;
            }

            return true;
        }

        Facility get() {
            return new Facility(
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
                    f[10].getText());
        }

        private void error(String m) {
            JOptionPane.showMessageDialog(null, m,
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
