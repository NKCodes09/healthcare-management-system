package repository;

import model.Referral;
import java.io.*;
import java.util.*;

public class ReferralRepository {

    private static final String CSV_PATH = "data/referrals.csv";

    private final List<Referral> referrals = new ArrayList<>();
    private final List<String[]> rawRows = new ArrayList<>();
    private String originalHeader;

    public ReferralRepository() {
        load();
    }

    private void load() {

        referrals.clear();
        rawRows.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {

            originalHeader = br.readLine();
            if (originalHeader == null)
                return;

            String[] headers = CsvUtil.splitCsvLine(originalHeader);
            Map<String, Integer> index = new HashMap<>();

            for (int i = 0; i < headers.length; i++) {
                index.put(headers[i], i);
            }

            String line;
            while ((line = br.readLine()) != null) {

                String[] cols = CsvUtil.splitCsvLine(line);
                rawRows.add(cols);

                Referral r = new Referral(
                        CsvUtil.get(cols, index.get("referral_id")),
                        CsvUtil.get(cols, index.get("patient_id")),
                        CsvUtil.get(cols, index.get("referring_clinician_id")),
                        CsvUtil.get(cols, index.get("referred_to_clinician_id")),
                        CsvUtil.get(cols, index.get("referring_facility_id")),
                        CsvUtil.get(cols, index.get("referred_to_facility_id")),
                        CsvUtil.get(cols, index.get("referral_date")),
                        CsvUtil.get(cols, index.get("urgency_level")),
                        CsvUtil.get(cols, index.get("referral_reason")),
                        CsvUtil.get(cols, index.get("clinical_summary")),
                        CsvUtil.get(cols, index.get("requested_investigations")),
                        CsvUtil.get(cols, index.get("status")),
                        CsvUtil.get(cols, index.get("appointment_id")),
                        CsvUtil.get(cols, index.get("notes")),
                        CsvUtil.get(cols, index.get("created_date")),
                        CsvUtil.get(cols, index.get("last_updated")));

                referrals.add(r);
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

        // Parse header to get column count and create index map
        String[] headers = CsvUtil.splitCsvLine(originalHeader);
        Map<String, Integer> headerIndex = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            headerIndex.put(headers[i], i);
        }

        // Create row array with proper size
        String[] row = new String[headers.length];

        // Populate row using header index to ensure correct column placement
        row[headerIndex.get("referral_id")] = r.getReferralId();
        row[headerIndex.get("patient_id")] = r.getPatientId();
        row[headerIndex.get("referring_clinician_id")] = r.getReferringClinicianId();
        row[headerIndex.get("referred_to_clinician_id")] = r.getReferredToClinicianId();
        row[headerIndex.get("referring_facility_id")] = r.getReferringFacilityId();
        row[headerIndex.get("referred_to_facility_id")] = r.getReferredToFacilityId();
        row[headerIndex.get("referral_date")] = r.getReferralDate();
        row[headerIndex.get("urgency_level")] = r.getUrgencyLevel();
        row[headerIndex.get("referral_reason")] = r.getReferralReason();
        row[headerIndex.get("clinical_summary")] = r.getClinicalSummary();
        row[headerIndex.get("requested_investigations")] = r.getRequestedInvestigations();
        row[headerIndex.get("status")] = r.getStatus();
        row[headerIndex.get("appointment_id")] = r.getAppointmentId();
        row[headerIndex.get("notes")] = r.getNotes();
        row[headerIndex.get("created_date")] = r.getCreatedDate();
        row[headerIndex.get("last_updated")] = r.getLastUpdated();

        rawRows.add(row);
        writeAll();
    }

    public void deleteReferral(int index) throws IOException {
        referrals.remove(index);
        rawRows.remove(index);
        writeAll();
    }

    private void writeAll() throws IOException {

        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_PATH))) {

            pw.println(originalHeader);

            for (String[] row : rawRows) {

                String[] safeRow = new String[row.length];
                for (int i = 0; i < row.length; i++) {
                    safeRow[i] = csvSafe(row[i]);
                }

                pw.println(String.join(",", safeRow));
            }
        }
    }

    private String csvSafe(String value) {
        if (value == null)
            return "";
        if (value.contains(",") || value.contains("\"")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}
