package view;

import model.Referral;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ReferralFormDialog {

    private final JTextField[] f = new JTextField[16];
    private final JTextArea clinicalSummary = new JTextArea(3, 20);
    private final JComboBox<String> urgency = new JComboBox<>(new String[] { "Routine", "Urgent", "Non-urgent" });

    private final Referral original;

    public ReferralFormDialog(Referral r) {
        this.original = r;
    }

    public Referral showDialog() {

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 6, 6));

        String[] labels = {
                "Referral ID",
                "Patient ID",
                "Referring Clinician ID",
                "Referred Clinician ID",
                "Referring Facility ID",
                "Referred Facility ID",
                "Referral Date (YYYY-MM-DD)",
                "Urgency",
                "Referral Reason",
                "Clinical Summary",
                "Requested Investigations",
                "Status",
                "Appointment ID",
                "Notes",
                "Created Date (YYYY-MM-DD)",
                "Last Updated (YYYY-MM-DD)"
        };

        for (int i = 0; i < f.length; i++) {
            f[i] = new JTextField();
        }

        // ---- build form ----
        for (int i = 0; i < labels.length; i++) {

            formPanel.add(new JLabel(labels[i]));

            if (i == 7) { // urgency combo
                formPanel.add(urgency);
            } else if (i == 9) { // clinical summary
                clinicalSummary.setLineWrap(true);
                clinicalSummary.setWrapStyleWord(true);
                JScrollPane csScroll = new JScrollPane(clinicalSummary);
                csScroll.setPreferredSize(new Dimension(200, 70));
                formPanel.add(csScroll);
            } else {
                formPanel.add(f[i]);
            }
        }

        if (original != null)
            populate();
        else
            autoFillDates();

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setPreferredSize(new Dimension(520, 420));

        while (true) {

            int result = JOptionPane.showConfirmDialog(
                    null,
                    scrollPane,
                    original == null ? "Add Referral" : "Edit Referral",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result != JOptionPane.OK_OPTION)
                return null;

            if (validateInput())
                return buildReferral();
        }
    }

    /* ================= POPULATE ================= */

    private void populate() {

        f[0].setText(original.getReferralId());
        f[1].setText(original.getPatientId());
        f[2].setText(original.getReferringClinicianId());
        f[3].setText(original.getReferredToClinicianId());
        f[4].setText(original.getReferringFacilityId());
        f[5].setText(original.getReferredToFacilityId());
        f[6].setText(original.getReferralDate());
        urgency.setSelectedItem(original.getUrgencyLevel());
        f[8].setText(original.getReferralReason());
        clinicalSummary.setText(original.getClinicalSummary());
        f[10].setText(original.getRequestedInvestigations());
        f[11].setText(original.getStatus());
        f[12].setText(original.getAppointmentId());
        f[13].setText(original.getNotes());
        f[14].setText(original.getCreatedDate());
        f[15].setText(original.getLastUpdated());

        f[0].setEditable(false); // ID locked
    }

    private void autoFillDates() {
        String today = LocalDate.now().toString();
        f[6].setText(today);
        f[14].setText(today);
        f[15].setText(today);
    }

    /* ================= VALIDATION ================= */

    private boolean validateInput() {

        if (!f[0].getText().matches("R\\d{3}")) {
            error("Referral ID must be in format R001.");
            return false;
        }

        if (!f[1].getText().matches("P\\d{3}")) {
            error("Patient ID must be in format P001.");
            return false;
        }

        if (!f[2].getText().matches("C\\d{3}")) {
            error("Referring clinician ID must be like C001.");
            return false;
        }

        if (!f[4].getText().matches("S\\d{3}")) {
            error("Referring facility ID must be like S001.");
            return false;
        }

        if (f[8].getText().trim().length() < 5) {
            error("Referral reason is required.");
            return false;
        }

        if (clinicalSummary.getText().trim().length() < 10) {
            error("Clinical summary must be meaningful.");
            return false;
        }

        if (!f[11].getText().matches("New|Pending|In Progress|Completed")) {
            error("Status must be New, Pending, In Progress, or Completed.");
            return false;
        }

        if (!isValidDate(f[6].getText()) ||
                !isValidDate(f[14].getText()) ||
                !isValidDate(f[15].getText())) {

            error("Dates must be in YYYY-MM-DD format.");
            return false;
        }

        return true;
    }

    private boolean isValidDate(String input) {
        try {
            LocalDate.parse(input.trim(), DateTimeFormatter.ISO_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(
                null,
                msg,
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /* ================= CREATE OBJECT ================= */

    private Referral buildReferral() {

        return new Referral(
                f[0].getText().trim(),
                f[1].getText().trim(),
                f[2].getText().trim(),
                f[3].getText().trim(),
                f[4].getText().trim(),
                f[5].getText().trim(),
                f[6].getText().trim(),
                urgency.getSelectedItem().toString(),
                f[8].getText().trim(),
                clinicalSummary.getText().trim(),
                f[10].getText().trim(),
                f[11].getText().trim(),
                f[12].getText().trim(),
                f[13].getText().trim(),
                f[14].getText().trim(),
                f[15].getText().trim());
    }
}
