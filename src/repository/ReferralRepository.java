package repository;

import model.Referral;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReferralRepository {

    private static final String CSV_PATH = "data/referrals.csv";
    private final List<Referral> referrals = new ArrayList<>();

    public ReferralRepository() {
        load();
    }

    // ========================
    // LOAD FROM CSV
    // ========================
    private void load() {
        referrals.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {

            String line = br.readLine(); // skip header

            while ((line = br.readLine()) != null) {

                // ✅ Proper CSV split (handles quoted commas)
                String[] d = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                referrals.add(new Referral(
                        d[0], // referral_id
                        d[1], // patient_id
                        d[2], // referring_clinician_id
                        d[6], // referral_date
                        d[7], // urgency_level
                        d[8], // referral_reason
                        d[9].replace("\"", ""), // clinical_summary
                        d[11] // status
                ));
            }

        } catch (IOException e) {
            System.err.println("Failed to load referrals.csv: " + e.getMessage());
        }
    }

    public List<Referral> getAll() {
        return referrals;
    }

    // ========================
    // ADD REFERRAL
    // ========================
    public void addReferral(Referral r) throws IOException {
        referrals.add(r);
        writeAll();
    }

    // ========================
    // UPDATE STATUS
    // ========================
    public void updateStatus(String referralId, String newStatus) throws IOException {
        for (Referral r : referrals) {
            if (r.getReferralId().equals(referralId)) {
                r.setStatus(newStatus);
                break;
            }
        }
        writeAll();
    }

    // ========================
    // DELETE REFERRAL
    // ========================
    public void deleteReferral(String referralId) throws IOException {
        referrals.removeIf(r -> r.getReferralId().equals(referralId));
        writeAll();
    }

    // ========================
    // WRITE BACK TO CSV
    // ========================
    private void writeAll() throws IOException {

        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_PATH))) {

            // Write header exactly as original
            pw.println(
                    "referral_id,patient_id,referring_clinician_id,referred_to_clinician_id," +
                            "referring_facility_id,referred_to_facility_id,referral_date,urgency_level," +
                            "referral_reason,clinical_summary,requested_investigations,status," +
                            "appointment_id,notes,created_date,last_updated");

            for (Referral r : referrals) {
                pw.println(String.join(",",
                        r.getReferralId(),
                        r.getPatientId(),
                        r.getReferringClinicianId(),
                        "", "", // referred clinician/facility (not modelled)
                        r.getReferralDate(),
                        r.getUrgencyLevel(),
                        r.getReferralReason(),
                        "\"" + r.getClinicalSummary() + "\"",
                        "", // requested investigations
                        r.getStatus(),
                        "", "", "", "" // appointment, notes, dates
                ));
            }
        }
    }
}
