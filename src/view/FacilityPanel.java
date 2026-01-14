package view;

import model.Facility;
import repository.FacilityRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

public class FacilityPanel extends JPanel {

    private FacilityRepository repository;
    private DefaultTableModel model;
    private JTable table;

    public FacilityPanel() {

        repository = new FacilityRepository();
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[] {
                "Facility ID", "Name", "Type", "Address", "Postcode",
                "Phone", "Email", "Opening Hours", "Manager",
                "Capacity", "Specialities"
        }, 0);

        table = new JTable(model); // ✅ CREATE FIRST

        // ✅ STYLE AFTER CREATION
        table.setRowHeight(24);
        table.setSelectionBackground(new Color(220, 235, 250));
        table.setSelectionForeground(Color.BLACK);
        table.getTableHeader().setReorderingAllowed(false);

        loadFacilities();

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);
    }

    /* ================= BUTTONS ================= */

    private JPanel createButtons() {

        JPanel p = new JPanel();

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");

        add.setFocusPainted(false);
        edit.setFocusPainted(false);
        delete.setFocusPainted(false);

        add.addActionListener(e -> addFacility());
        edit.addActionListener(e -> editFacility());
        delete.addActionListener(e -> deleteFacility());

        p.add(add);
        p.add(edit);
        p.add(delete);

        return p;
    }

    /* ================= LOAD ================= */

    private void loadFacilities() {

        model.setRowCount(0);

        for (Facility f : repository.getAll()) {
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

    /* ================= CRUD ================= */

    private void addFacility() {

        Facility f = facilityForm(null);
        if (f == null)
            return;

        try {
            repository.add(f);
            loadFacilities();
        } catch (IOException ex) {
            showError(ex.getMessage());
        }
    }

    private void editFacility() {

        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Select a facility first.");
            return;
        }

        Facility old = repository.getAll().get(row);
        Facility updated = facilityForm(old);
        if (updated == null)
            return;

        repository.getAll().set(row, updated);

        try {
            repository.updateAll();
            loadFacilities();
        } catch (IOException ex) {
            showError(ex.getMessage());
        }
    }

    private void deleteFacility() {

        int row = table.getSelectedRow();
        if (row == -1)
            return;

        if (JOptionPane.showConfirmDialog(
                this,
                "Delete selected facility?",
                "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            try {
                repository.delete(row);
                loadFacilities();
            } catch (IOException ex) {
                showError(ex.getMessage());
            }
        }
    }

    /* ================= FORM ================= */

    private Facility facilityForm(Facility f) {

        JTextField id = new JTextField();
        JTextField name = new JTextField();
        JTextField type = new JTextField();
        JTextField address = new JTextField();
        JTextField postcode = new JTextField();
        JTextField phone = new JTextField();
        JTextField email = new JTextField();
        JTextField opening = new JTextField();
        JTextField manager = new JTextField();
        JTextField capacity = new JTextField();
        JTextField specialities = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));

        panel.add(new JLabel("Facility ID"));
        panel.add(id);
        panel.add(new JLabel("Name"));
        panel.add(name);
        panel.add(new JLabel("Type"));
        panel.add(type);
        panel.add(new JLabel("Address"));
        panel.add(address);
        panel.add(new JLabel("Postcode"));
        panel.add(postcode);
        panel.add(new JLabel("Phone"));
        panel.add(phone);
        panel.add(new JLabel("Email"));
        panel.add(email);
        panel.add(new JLabel("Opening Hours"));
        panel.add(opening);
        panel.add(new JLabel("Manager"));
        panel.add(manager);
        panel.add(new JLabel("Capacity"));
        panel.add(capacity);
        panel.add(new JLabel("Specialities (| separated)"));
        panel.add(specialities);

        if (f != null) {
            id.setText(f.getFacilityId());
            id.setEditable(false);
            name.setText(f.getFacilityName());
            type.setText(f.getFacilityType());
            address.setText(f.getAddress());
            postcode.setText(f.getPostcode());
            phone.setText(f.getPhoneNumber());
            email.setText(f.getEmail());
            opening.setText(f.getOpeningHours());
            manager.setText(f.getManagerName());
            capacity.setText(f.getCapacity());
            specialities.setText(f.getSpecialitiesOffered());
        }

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setPreferredSize(new Dimension(520, 420));

        while (true) {

            int ok = JOptionPane.showConfirmDialog(
                    this,
                    scroll,
                    f == null ? "Add Facility" : "Edit Facility",
                    JOptionPane.OK_CANCEL_OPTION);

            if (ok != JOptionPane.OK_OPTION)
                return null;

            if (!id.getText().matches("[SH]\\d{3}")) {
                showError("Facility ID must be S001 or H001");
                continue;
            }

            if (name.getText().trim().length() < 3) {
                showError("Facility name required");
                continue;
            }

            if (!capacity.getText().matches("\\d+")) {
                showError("Capacity must be numeric");
                continue;
            }

            if (!email.getText().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                showError("Invalid email format");
                continue;
            }

            return new Facility(
                    id.getText().trim(),
                    name.getText().trim(),
                    type.getText().trim(),
                    address.getText().trim(),
                    postcode.getText().trim(),
                    phone.getText().trim(),
                    email.getText().trim(),
                    opening.getText().trim(),
                    manager.getText().trim(),
                    capacity.getText().trim(),
                    specialities.getText().trim());
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg,
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
