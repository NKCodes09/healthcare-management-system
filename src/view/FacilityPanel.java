package view;

import model.Facility;
import repository.FacilityRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

public class FacilityPanel extends JPanel {

    private final FacilityRepository repo = new FacilityRepository();
    private final DefaultTableModel model;
    private final JTable table;

    public FacilityPanel() {

        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[] {
                "ID", "Name", "Type", "Address", "Postcode", "Phone",
                "Email", "Opening Hours", "Manager", "Capacity", "Specialities"
        }, 0);

        table = new JTable(model);
        load();

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons(), BorderLayout.SOUTH);
    }

    private JPanel buttons() {

        JPanel p = new JPanel();
        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton del = new JButton("Delete");

        add.addActionListener(e -> addFacility());
        edit.addActionListener(e -> editFacility());
        del.addActionListener(e -> deleteFacility());

        p.add(add);
        p.add(edit);
        p.add(del);
        return p;
    }

    private void load() {

        model.setRowCount(0);
        for (Facility f : repo.getAll()) {
            model.addRow(new Object[] {
                    f.getFacilityId(), f.getFacilityName(), f.getFacilityType(),
                    f.getAddress(), f.getPostcode(), f.getPhoneNumber(),
                    f.getEmail(), f.getOpeningHours(), f.getManagerName(),
                    f.getCapacity(), f.getSpecialitiesOffered()
            });
        }
    }

    private void addFacility() {

        Facility f = facilityForm(null);
        if (f == null)
            return;

        try {
            repo.add(f);
            load();
        } catch (IOException e) {
            error(e.getMessage());
        }
    }

    private void editFacility() {

        int r = table.getSelectedRow();
        if (r < 0) {
            error("Select facility");
            return;
        }

        Facility f = facilityForm(repo.getAll().get(r));
        if (f == null)
            return;

        repo.getAll().set(r, f);

        try {
            repo.updateAll();
            load();
        } catch (IOException e) {
            error(e.getMessage());
        }
    }

    private void deleteFacility() {

        int r = table.getSelectedRow();
        if (r < 0)
            return;

        if (JOptionPane.showConfirmDialog(
                this, "Delete facility?", "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            try {
                repo.delete(r);
                load();
            } catch (IOException e) {
                error(e.getMessage());
            }
        }
    }

    /* ---------- FORM ---------- */

    private Facility facilityForm(Facility f) {

        JTextField[] t = new JTextField[11];
        JPanel p = new JPanel(new GridLayout(0, 2, 5, 5));

        String[] lbl = {
                "Facility ID", "Name", "Type", "Address", "Postcode", "Phone",
                "Email", "Opening Hours", "Manager", "Capacity", "Specialities"
        };

        for (int i = 0; i < t.length; i++) {
            t[i] = new JTextField();
            p.add(new JLabel(lbl[i]));
            p.add(t[i]);
        }

        if (f != null) {
            t[0].setText(f.getFacilityId());
            t[0].setEditable(false);
            t[1].setText(f.getFacilityName());
            t[2].setText(f.getFacilityType());
            t[3].setText(f.getAddress());
            t[4].setText(f.getPostcode());
            t[5].setText(f.getPhoneNumber());
            t[6].setText(f.getEmail());
            t[7].setText(f.getOpeningHours());
            t[8].setText(f.getManagerName());
            t[9].setText(f.getCapacity());
            t[10].setText(f.getSpecialitiesOffered());
        }

        while (true) {

            int ok = JOptionPane.showConfirmDialog(
                    this, p, f == null ? "Add Facility" : "Edit Facility",
                    JOptionPane.OK_CANCEL_OPTION);

            if (ok != JOptionPane.OK_OPTION)
                return null;

            if (!t[0].getText().matches("[SH]\\d{3}")) {
                error("ID S001 or H001");
                continue;
            }
            if (!t[6].getText().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                error("Email");
                continue;
            }
            if (!t[9].getText().matches("\\d+")) {
                error("Capacity numeric");
                continue;
            }

            return new Facility(
                    t[0].getText(), t[1].getText(), t[2].getText(),
                    t[3].getText(), t[4].getText(), t[5].getText(),
                    t[6].getText(), t[7].getText(), t[8].getText(),
                    t[9].getText(), t[10].getText());
        }
    }

    private void error(String m) {
        JOptionPane.showMessageDialog(this, m, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
