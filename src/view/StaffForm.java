package view;

import model.Staff;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class StaffForm {

    private final JTextField[] f = new JTextField[12];
    private final Staff original;

    public StaffForm(Staff s) {
        this.original = s;
    }

    public boolean showDialog() {

        JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));

        String[] labels = {
                "Staff ID",
                "First Name",
                "Last Name",
                "Role",
                "Department",
                "Facility ID",
                "Phone Number",
                "Email",
                "Employment Status",
                "Start Date (YYYY-MM-DD)",
                "Line Manager",
                "Access Level"
        };

        for (int i = 0; i < f.length; i++) {
            f[i] = new JTextField();
            panel.add(new JLabel(labels[i]));
            panel.add(f[i]);
        }

        if (original != null)
            populate();

        while (true) {

            int result = JOptionPane.showConfirmDialog(
                    null,
                    panel,
                    original == null ? "Add Staff" : "Edit Staff",
                    JOptionPane.OK_CANCEL_OPTION);

            if (result != JOptionPane.OK_OPTION)
                return false;

            if (validateInput())
                return true;
        }
    }

    private void populate() {

        f[0].setText(original.getStaffId());
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

        f[0].setEditable(false);
    }

    private boolean validateInput() {

        if (!f[0].getText().matches("ST\\d{3}")) {
            error("Staff ID must be like ST001.");
            return false;
        }

        if (!f[1].getText().matches("[A-Za-z\\s]{2,}")) {
            error("Invalid first name.");
            return false;
        }

        if (!f[2].getText().matches("[A-Za-z\\s]{2,}")) {
            error("Invalid last name.");
            return false;
        }

        if (!f[5].getText().matches("[SH]\\d{3}")) {
            error("Facility ID must be S001 or H001.");
            return false;
        }

        if (!f[6].getText().matches("\\d{10,11}")) {
            error("Phone must be 10–11 digits.");
            return false;
        }

        if (!f[7].getText().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            error("Invalid email address.");
            return false;
        }

        try {
            LocalDate.parse(f[9].getText());
        } catch (DateTimeParseException e) {
            error("Invalid start date.");
            return false;
        }

        if (!f[11].getText().matches("Basic|Standard|Manager")) {
            error("Access level must be Basic, Standard, or Manager.");
            return false;
        }

        return true;
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(null,
                msg, "Validation Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public Staff getStaff() {

        return new Staff(
                f[0].getText().trim(),
                f[1].getText().trim(),
                f[2].getText().trim(),
                f[3].getText().trim(),
                f[4].getText().trim(),
                f[5].getText().trim(),
                f[6].getText().trim(),
                f[7].getText().trim(),
                f[8].getText().trim(),
                f[9].getText().trim(),
                f[10].getText().trim(),
                f[11].getText().trim());
    }
}
