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

    private void load() {

        referrals.clear();
        rawRows.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {

            header = br.readLine();
            if (header == null)
                return;

            String[] h = CsvUtil.splitCsvLine(header);
            Map<String, Integer> idx = new HashMap<>();
            for (int i = 0; i < h.length; i++)
                idx.put(h[i], i);

            String line;
            while ((line = br.readLine()) != null) {

                String[] c = CsvUtil.splitCsvLine(line);
                rawRows.add(c);

                referrals.add(new Referral(
                        CsvUtil.get(c, idx.get("referral_id")),
                        CsvUtil.get(c, idx.get("patient_id")),
                        CsvUtil.get(c, idx.get("referring_clinician_id")),
                        CsvUtil.get(c, idx.get("referred_to_clinician_id")),
                        CsvUtil.get(c, idx.get("referring_facility_id")),
                        CsvUtil.get(c, idx.get("referred_to_facility_id")),
                        CsvUtil.get(c, idx.get("referral_date")),
                        CsvUtil.get(c, idx.get("urgency_level")),
                        CsvUtil.get(c, idx.get("referral_reason")),
                        CsvUtil.get(c, idx.get("clinical_summary")),
                        CsvUtil.get(c, idx.get("requested_investigations")),
                        CsvUtil.get(c, idx.get("status")),
                        CsvUtil.get(c, idx.get("appointment_id")),
                        CsvUtil.get(c, idx.get("notes")),
                        CsvUtil.get(c, idx.get("created_date")),
                        CsvUtil.get(c, idx.get("last_updated"))));
            }
        } catch (IOException e) {
            System.err.println("Failed to load referrals.csv");
        }
    }

    public List<Referral> getAll() {
        return referrals;
    }

    public void add(Referral r) throws IOException {

        referrals.add(r);
        rawRows.add(new String[] {
                r.getReferralId(), r.getPatientId(),
                r.getReferringClinicianId(), r.getReferredToClinicianId(),
                r.getReferringFacilityId(), r.getReferredToFacilityId(),
                r.getReferralDate(), r.getUrgencyLevel(),
                r.getReferralReason(), r.getClinicalSummary(),
                r.getRequestedInvestigations(), r.getStatus(),
                r.getAppointmentId(), r.getNotes(),
                r.getCreatedDate(), r.getLastUpdated()
        });
        writeAll();
    }

    public void delete(int index) throws IOException {
        referrals.remove(index);
        rawRows.remove(index);
        writeAll();
    }

    private void writeAll() throws IOException {

        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_PATH))) {
            pw.println(header);
            for (String[] r : rawRows) {
                for (int i = 0; i < r.length; i++)
                    r[i] = CsvUtil.escape(r[i]);
                pw.println(String.join(",", r));
            }
        }
    }
}
