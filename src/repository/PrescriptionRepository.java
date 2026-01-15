package repository;

import model.Prescription;
import java.io.*;
import java.util.*;

public class PrescriptionRepository {

    private static final String CSV_PATH = "data/prescriptions.csv";

    private final List<Prescription> prescriptions = new ArrayList<>();
    private final List<String[]> rawRows = new ArrayList<>();
    private String header;

    public PrescriptionRepository() {
        load();
    }

    private void load() {

        prescriptions.clear();
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

                prescriptions.add(new Prescription(
                        CsvUtil.get(c, index.get("prescription_id")),
                        CsvUtil.get(c, index.get("patient_id")),
                        CsvUtil.get(c, index.get("clinician_id")),
                        CsvUtil.get(c, index.get("appointment_id")),
                        CsvUtil.get(c, index.get("prescription_date")),
                        CsvUtil.get(c, index.get("medication_name")),
                        CsvUtil.get(c, index.get("dosage")),
                        CsvUtil.get(c, index.get("frequency")),
                        CsvUtil.get(c, index.get("duration_days")),
                        CsvUtil.get(c, index.get("quantity")),
                        CsvUtil.get(c, index.get("instructions")),
                        CsvUtil.get(c, index.get("pharmacy_name")),
                        CsvUtil.get(c, index.get("status")),
                        CsvUtil.get(c, index.get("issue_date")),
                        CsvUtil.get(c, index.get("collection_date"))));
            }

        } catch (IOException e) {
            System.err.println("Failed to load prescriptions.csv: " + e.getMessage());
        }
    }

    public List<Prescription> getAll() {
        return prescriptions;
    }

    public void add(Prescription p) throws IOException {

        prescriptions.add(p);

        rawRows.add(new String[] {
                p.getPrescriptionId(),
                p.getPatientId(),
                p.getClinicianId(),
                p.getAppointmentId(),
                p.getPrescriptionDate(),
                p.getMedicationName(),
                p.getDosage(),
                p.getFrequency(),
                p.getDurationDays(),
                p.getQuantity(),
                p.getInstructions(),
                p.getPharmacyName(),
                p.getStatus(),
                p.getIssueDate(),
                p.getCollectionDate()
        });

        writeAll();
    }

    public void update(int index, Prescription p) throws IOException {
        prescriptions.set(index, p);
        rawRows.set(index, new String[] {
                p.getPrescriptionId(),
                p.getPatientId(),
                p.getClinicianId(),
                p.getAppointmentId(),
                p.getPrescriptionDate(),
                p.getMedicationName(),
                p.getDosage(),
                p.getFrequency(),
                p.getDurationDays(),
                p.getQuantity(),
                p.getInstructions(),
                p.getPharmacyName(),
                p.getStatus(),
                p.getIssueDate(),
                p.getCollectionDate()
        });
        writeAll();
    }

    public void delete(int index) throws IOException {
        prescriptions.remove(index);
        rawRows.remove(index);
        writeAll();
    }

    private void writeAll() throws IOException {

        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_PATH))) {
            pw.println(header);

            for (String[] r : rawRows) {
                for (int i = 0; i < r.length; i++) {
                    r[i] = CsvUtil.escape(r[i]);
                }
                pw.println(String.join(",", r));
            }
        }
    }
}
