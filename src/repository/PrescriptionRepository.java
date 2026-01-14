package repository;

import model.Prescription;
import java.io.*;
import java.util.*;

public class PrescriptionRepository {

    private static final String CSV_PATH = "data/prescriptions.csv";
    private final List<Prescription> prescriptions = new ArrayList<>();

    public PrescriptionRepository() {
        load();
    }

    private void load() {
        prescriptions.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {

            String headerLine = br.readLine();
            if (headerLine == null)
                return;

            String[] headers = headerLine.split(",");
            Map<String, Integer> index = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                index.put(headers[i].trim(), i);
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] c = CsvUtil.splitCsvLine(line);

                prescriptions.add(new Prescription(
                        get(c, index, "prescription_id"),
                        get(c, index, "patient_id"),
                        get(c, index, "clinician_id"),
                        get(c, index, "appointment_id"),
                        get(c, index, "prescription_date"),
                        get(c, index, "medication_name"),
                        get(c, index, "dosage"),
                        get(c, index, "frequency"),
                        get(c, index, "duration_days"),
                        get(c, index, "quantity"),
                        get(c, index, "instructions"),
                        get(c, index, "pharmacy_name"),
                        get(c, index, "status"),
                        get(c, index, "issue_date"),
                        get(c, index, "collection_date")));
            }

        } catch (IOException e) {
            System.err.println("Failed to load prescriptions.csv: " + e.getMessage());
        }
    }

    private String get(String[] row, Map<String, Integer> index, String key) {
        Integer i = index.get(key);
        return (i != null && i < row.length) ? row[i].trim() : "";
    }

    public List<Prescription> getAll() {
        return prescriptions;
    }

    public void addPrescription(Prescription p) throws IOException {
        prescriptions.add(p);
        writeAll();
    }

    public void updateAll() throws IOException {
        writeAll();
    }

    public void deletePrescription(int index) throws IOException {
        prescriptions.remove(index);
        writeAll();
    }

    private void writeAll() throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_PATH))) {

            pw.println(String.join(",",
                    "prescription_id", "patient_id", "clinician_id", "appointment_id",
                    "prescription_date", "medication_name", "dosage", "frequency",
                    "duration_days", "quantity", "instructions", "pharmacy_name",
                    "status", "issue_date", "collection_date"));

            for (Prescription p : prescriptions) {
                pw.println(String.join(",",
                        CsvUtil.escape(p.getPrescriptionId()),
                        CsvUtil.escape(p.getPatientId()),
                        CsvUtil.escape(p.getClinicianId()),
                        CsvUtil.escape(p.getAppointmentId()),
                        CsvUtil.escape(p.getPrescriptionDate()),
                        CsvUtil.escape(p.getMedicationName()),
                        CsvUtil.escape(p.getDosage()),
                        CsvUtil.escape(p.getFrequency()),
                        CsvUtil.escape(p.getDurationDays()),
                        CsvUtil.escape(p.getQuantity()),
                        CsvUtil.escape(p.getInstructions()),
                        CsvUtil.escape(p.getPharmacyName()),
                        CsvUtil.escape(p.getStatus()),
                        CsvUtil.escape(p.getIssueDate()),
                        CsvUtil.escape(p.getCollectionDate())));
            }
        }
    }
}
