package repository;

import model.Referral;

import java.io.*;
import java.util.*;

public class ReferralRepository {

    private static final String CSV_PATH = "data/referrals.csv";

    private final List<Referral> referrals = new ArrayList<>();
    private final List<String[]> rawRows = new ArrayList<>();
    private String header;

    public ReferralRepository() {
        load();
    }

    /* ================= LOAD ================= */

    private void load() {

        referrals.clear();
        rawRows.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {

            header = br.readLine();
            if (header == null)
                return;

            String[] headers = CsvUtil.splitCsvLine(header);
            Map<String, Integer> index = new HashMap<>();

            for (int i = 0; i < headers.length; i++) {
                index.put(headers[i], i);
            }

            String line;
            while ((line = br.readLine()) != null) {

                String[] c = CsvUtil.splitCsvLine(line);
                rawRows.add(c);

                referrals.add(new Referral(
                        get(c, index, "referral_id"),
                        get(c, index, "patient_id"),
                        get(c, index, "referring_clinician_id"),
                        get(c, index, "referred_to_clinician_id"),
                        get(c, index, "referring_facility_id"),
                        get(c, index, "referred_to_facility_id"),
                        get(c, index, "referral_date"),
                        get(c, index, "urgency_level"),
                        get(c, index, "referral_reason"),
                        get(c, index, "clinical_summary"),
                        get(c, index, "requested_investigations"),
                        get(c, index, "status"),
                        get(c, index, "appointment_id"),
                        get(c, index, "notes"),
                        get(c, index, "created_date"),
                        get(c, index, "last_updated")));
            }

        } catch (IOException e) {
            System.err.println("Failed to load referrals.csv: " + e.getMessage());
        }
    }

    /* ================= UTIL ================= */

    private String get(String[] row, Map<String, Integer> index, String key) {
        Integer i = index.get(key);
        return (i != null && i < row.length) ? row[i].trim() : "";
    }

    private String csvSafe(String v) {
        if (v == null)
            return "";
        if (v.contains(",") || v.contains("\"")) {
            v = v.replace("\"", "\"\"");
            return "\"" + v + "\"";
        }
        return v;
    }

    /* ================= CRUD ================= */

    public List<Referral> getAll() {
        return referrals;
    }

    public void addReferral(Referral r) throws IOException {

        referrals.add(r);

        String[] row = new String[header.split(",").length];
        row[0] = r.getReferralId();
        row[1] = r.getPatientId();
        row[2] = r.getReferringClinicianId();
        row[3] = r.getReferredToClinicianId();
        row[4] = r.getReferringFacilityId();
        row[5] = r.getReferredToFacilityId();
        row[6] = r.getReferralDate();
        row[7] = r.getUrgencyLevel();
        row[8] = r.getReferralReason();
        row[9] = r.getClinicalSummary();
        row[10] = r.getRequestedInvestigations();
        row[11] = r.getStatus();
        row[12] = r.getAppointmentId();
        row[13] = r.getNotes();
        row[14] = r.getCreatedDate();
        row[15] = r.getLastUpdated();

        rawRows.add(row);
        writeAll();
    }

    /* ================= REQUIRED BY CONTROLLER ================= */

    public void updateAll() throws IOException {
        writeAll();
    }

    public void deleteReferral(int index) throws IOException {

        referrals.remove(index);
        rawRows.remove(index);
        writeAll();
    }

    /* ================= WRITE ================= */

    private void writeAll() throws IOException {

        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_PATH))) {

            pw.println(header);

            for (String[] r : rawRows) {
                String[] safe = new String[r.length];
                for (int i = 0; i < r.length; i++)
                    safe[i] = csvSafe(r[i]);

                pw.println(String.join(",", safe));
            }
        }
    }
}
