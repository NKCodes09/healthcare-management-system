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

    private void load() {
        referrals.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {
            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");

                // ✅ SAFELY HANDLE VARIABLE CSV LENGTH
                String referralId = d.length > 0 ? d[0] : "";
                String patientNhs = d.length > 1 ? d[1] : "";
                String clinicianId = d.length > 2 ? d[2] : "";
                String referralDate = d.length > 3 ? d[3] : "";
                String urgency = d.length > 4 ? d[4] : "";
                String clinicalSummary = d.length > 5 ? d[5] : "";
                String status = d.length > 6 ? d[6] : "New";

                referrals.add(new Referral(
                        referralId,
                        patientNhs,
                        clinicianId,
                        "", // fromFacility
                        "", // toFacility
                        clinicalSummary,
                        urgency,
                        referralDate,
                        "", // reason
                        "", // investigations
                        status,
                        "" // notes
                ));
            }

        } catch (IOException e) {
            System.err.println("Failed to load referrals.csv: " + e.getMessage());
        }
    }

    public List<Referral> getAll() {
        return referrals;
    }

    public void addReferral(Referral r) throws IOException {
        referrals.add(r);
        writeAll();
    }
    
    public void updateReferral(Referral updated) throws IOException {

        for (int i = 0; i < referrals.size(); i++) {
            if (referrals.get(i).getReferralId().equals(updated.getReferralId())) {
                referrals.set(i, updated); // ✅ replace object
                break;
            }
        }

        writeAll(); // ✅ persist change to CSV
    }

    public void deleteReferral(String referralId) throws IOException {
        referrals.removeIf(r -> r.getReferralId().equals(referralId));
        writeAll();
    }

    private void writeAll() throws IOException {

    try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_PATH))) {

        pw.println("referralId,clinicalSummary,urgencyLevel,status");

        for (Referral r : referrals) {
            pw.println(String.join(",",
                    r.getReferralId(),
                    r.getClinicalSummary(),
                    r.getUrgencyLevel(),
                    r.getStatus()   // ✅ THIS IS CRITICAL
            ));
        }
    }
}

}
