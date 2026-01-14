package repository;

import model.Prescription;
import java.io.*;
import java.util.*;

public class PrescriptionRepository {

    private static final String CSV_PATH = "data/prescriptions.csv";

    private final List<Prescription> prescriptions = new ArrayList<>();
    private final List<String[]> rawRows = new ArrayList<>();
    private String originalHeader;

    public PrescriptionRepository() {
        load();
    }

    private void load() {

        prescriptions.clear();
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

                Prescription p = new Prescription(
                        CsvUtil.get(cols, index.get("prescription_id")),
                        CsvUtil.get(cols, index.get("patient_id")),
                        CsvUtil.get(cols, index.get("clinician_id")),
                        CsvUtil.get(cols, index.get("appointment_id")),
                        CsvUtil.get(cols, index.get("prescription_date")),
                        CsvUtil.get(cols, index.get("medication_name")),
                        CsvUtil.get(cols, index.get("dosage")),
                        CsvUtil.get(cols, index.get("frequency")),
                        CsvUtil.get(cols, index.get("duration_days")),
                        CsvUtil.get(cols, index.get("quantity")),
                        CsvUtil.get(cols, index.get("instructions")),
                        CsvUtil.get(cols, index.get("pharmacy_name")),
                        CsvUtil.get(cols, index.get("status")),
                        CsvUtil.get(cols, index.get("issue_date")),
                        CsvUtil.get(cols, index.get("collection_date")));

                prescriptions.add(p);
            }

        } catch (IOException e) {
            System.err.println("Failed to load prescriptions.csv: " + e.getMessage());
        }
    }

    public List<Prescription> getAll() {
        return prescriptions;
    }

    public void addPrescription(Prescription p) throws IOException {

        prescriptions.add(p);

        String[] row = new String[originalHeader.split(",").length];
        row[0] = p.getPrescriptionId();
        row[1] = p.getPatientId();
        row[2] = p.getClinicianId();
        row[3] = p.getAppointmentId();
        row[4] = p.getPrescriptionDate();
        row[5] = p.getMedicationName();
        row[6] = p.getDosage();
        row[7] = p.getFrequency();
        row[8] = p.getDurationDays();
        row[9] = p.getQuantity();
        row[10] = p.getInstructions();
        row[11] = p.getPharmacyName();
        row[12] = p.getStatus();
        row[13] = p.getIssueDate();
        row[14] = p.getCollectionDate();

        rawRows.add(row);
        writeAll();
    }

    public void updateAll() throws IOException {
        writeAll();
    }

    public void deletePrescription(int index) throws IOException {

        prescriptions.remove(index);
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

    /**
     * Ensures CSV values containing commas or quotes are written safely.
     */
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
