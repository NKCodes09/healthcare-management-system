package view;

import model.Appointment;
import repository.AppointmentRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AppointmentPanel extends JPanel {

    private final AppointmentRepository repo = new AppointmentRepository();
    private final DefaultTableModel model;
    private  JTable table;

    public AppointmentPanel() {
     

        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[] {
                "ID", "Patient", "Clinician", "Facility",
                "Date", "Time", "Duration", "Type", "Status",
                "Reason", "Notes", "Created", "Updated"
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

    /* ================= BUTTONS ================= */

    private JPanel buttons() {

        JPanel p = new JPanel();

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton del = new JButton("Delete");

        add.addActionListener(e -> addAppointment());
        edit.addActionListener(e -> editAppointment());
        del.addActionListener(e -> deleteAppointment());

        p.add(add);
        p.add(edit);
        p.add(del);
        return p;
    }

    /* ================= LOAD ================= */

    private void load() {

        model.setRowCount(0);

        for (Appointment a : repo.getAll()) {
            model.addRow(new Object[] {
                    a.getAppointmentId(),
                    a.getPatientId(),
                    a.getClinicianId(),
                    a.getFacilityId(),
                    a.getAppointmentDate(),
                    a.getAppointmentTime(),
                    a.getDurationMinutes(),
                    a.getAppointmentType(),
                    a.getStatus(),
                    a.getReasonForVisit(),
                    a.getNotes(),
                    a.getCreatedDate(),
                    a.getLastModified()
            });
        }
    }

    /* ================= CRUD ================= */

    private void addAppointment() {

        Appointment a = appointmentForm(null);
        if (a == null)
            return;

        try {
            repo.add(a);
            load();
        } catch (IOException e) {
            showError(e.getMessage());
        }
    }

    private void editAppointment() {

        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Select appointment first.");
            return;
        }

        Appointment old = repo.getAll().get(row);
        Appointment updated = appointmentForm(old);
        if (updated == null)
            return;

        Appointment fixed = new Appointment(
                updated.getAppointmentId(),
                updated.getPatientId(),
                updated.getClinicianId(),
                updated.getFacilityId(),
                updated.getAppointmentDate(),
                updated.getAppointmentTime(),
                updated.getDurationMinutes(),
                updated.getAppointmentType(),
                updated.getStatus(),
                updated.getReasonForVisit(),
                updated.getNotes(),
                old.getCreatedDate(),
                LocalDate.now().toString());

        repo.getAll().set(row, fixed);

        try {
            repo.updateAll();
            load();
        } catch (IOException e) {
            showError(e.getMessage());
        }
    }

    private void deleteAppointment() {

        int row = table.getSelectedRow();
        if (row < 0)
            return;

        if (JOptionPane.showConfirmDialog(
                this,
                "Delete appointment?",
                "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            try {
                repo.delete(row);
                load();
            } catch (IOException e) {
                showError(e.getMessage());
            }
        }
    }

    /* ================= FORM ================= */

    private Appointment appointmentForm(Appointment a) {

        JTextField idField = new JTextField();
        JTextField patientField = new JTextField();
        JTextField clinicianField = new JTextField();
        JTextField facilityField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField timeField = new JTextField();
        JTextField durationField = new JTextField();
        JTextField reasonField = new JTextField();

        JTextArea notesArea = new JTextArea(3, 20);

        JComboBox<String> typeBox = new JComboBox<>(new String[] {
                "Routine Consultation", "Follow-up", "Vaccination",
                "Urgent Consultation", "Specialist Consultation",
                "Emergency", "Health Check"
        });

        JComboBox<String> statusBox = new JComboBox<>(new String[] {
                "Scheduled", "Completed", "Cancelled"
        });

        JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));

        panel.add(new JLabel("Appointment ID"));
        panel.add(idField);
        panel.add(new JLabel("Patient ID"));
        panel.add(patientField);
        panel.add(new JLabel("Clinician ID"));
        panel.add(clinicianField);
        panel.add(new JLabel("Facility ID"));
        panel.add(facilityField);
        panel.add(new JLabel("Date (YYYY-MM-DD)"));
        panel.add(dateField);
        panel.add(new JLabel("Time (HH:MM)"));
        panel.add(timeField);
        panel.add(new JLabel("Duration (minutes)"));
        panel.add(durationField);
        panel.add(new JLabel("Appointment Type"));
        panel.add(typeBox);
        panel.add(new JLabel("Status"));
        panel.add(statusBox);
        panel.add(new JLabel("Reason for Visit"));
        panel.add(reasonField);
        panel.add(new JLabel("Notes"));
        panel.add(new JScrollPane(notesArea));

        if (a != null) {
            idField.setText(a.getAppointmentId());
            idField.setEditable(false);
            patientField.setText(a.getPatientId());
            clinicianField.setText(a.getClinicianId());
            facilityField.setText(a.getFacilityId());
            dateField.setText(a.getAppointmentDate());
            timeField.setText(a.getAppointmentTime());
            durationField.setText(a.getDurationMinutes());
            typeBox.setSelectedItem(a.getAppointmentType());
            statusBox.setSelectedItem(a.getStatus());
            reasonField.setText(a.getReasonForVisit());
            notesArea.setText(a.getNotes());
        } else {
            dateField.setText(LocalDate.now().toString());
        }

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setPreferredSize(new Dimension(520, 420));

        while (true) {

            int ok = JOptionPane.showConfirmDialog(
                    this,
                    scroll,
                    a == null ? "Add Appointment" : "Edit Appointment",
                    JOptionPane.OK_CANCEL_OPTION);

            if (ok != JOptionPane.OK_OPTION)
                return null;

            /* ===== VALIDATION ===== */

            if (!idField.getText().matches("A\\d{3}")) {
                showError("Appointment ID must be like A001");
                continue;
            }

            if (!patientField.getText().matches("P\\d{3}")) {
                showError("Patient ID must be like P001");
                continue;
            }

            if (!clinicianField.getText().matches("C\\d{3}")) {
                showError("Clinician ID must be like C001");
                continue;
            }

            if (!facilityField.getText().matches("[SH]\\d{3}")) {
                showError("Facility ID must be S001 or H001");
                continue;
            }

            try {
                LocalDate.parse(dateField.getText());
            } catch (DateTimeParseException e) {
                showError("Invalid date format");
                continue;
            }

            if (!timeField.getText().matches("\\d{2}:\\d{2}")) {
                showError("Time must be HH:MM");
                continue;
            }

            if (!durationField.getText().matches("\\d+")) {
                showError("Duration must be numeric");
                continue;
            }

            if (reasonField.getText().trim().isEmpty()) {
                showError("Reason for visit required");
                continue;
            }

            return new Appointment(
                    idField.getText(),
                    patientField.getText(),
                    clinicianField.getText(),
                    facilityField.getText(),
                    dateField.getText(),
                    timeField.getText(),
                    durationField.getText(),
                    typeBox.getSelectedItem().toString(),
                    statusBox.getSelectedItem().toString(),
                    reasonField.getText(),
                    notesArea.getText(),
                    LocalDate.now().toString(),
                    LocalDate.now().toString());
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }
}
