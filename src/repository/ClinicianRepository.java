package repository;

import model.Clinician;
import java.io.*;
import java.util.*;

public class ClinicianRepository {

    private static final String CSV_PATH = "data/clinicians.csv";
    private final List<Clinician> clinicians = new ArrayList<>();

    public ClinicianRepository() {
        load();
    }

    private void load() {
        clinicians.clear();

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

                clinicians.add(new Clinician(
                        get(c, index, "clinician_id"),
                        get(c, index, "first_name"),
                        get(c, index, "last_name"),
                        get(c, index, "title"),
                        get(c, index, "speciality"),
                        get(c, index, "gmc_number"),
                        get(c, index, "phone_number"),
                        get(c, index, "email"),
                        get(c, index, "workplace_id"),
                        get(c, index, "workplace_type"),
                        get(c, index, "employment_status"),
                        get(c, index, "start_date")));
            }

        } catch (IOException e) {
            System.err.println("Failed to load clinicians.csv: " + e.getMessage());
        }
    }

    private String get(String[] row, Map<String, Integer> index, String key) {
        Integer i = index.get(key);
        return (i != null && i < row.length) ? row[i].trim() : "";
    }

    public List<Clinician> getAll() {
        return clinicians;
    }

    public void addClinician(Clinician c) throws IOException {
        clinicians.add(c);
        writeAll();
    }

    public void updateAll() throws IOException {
        writeAll();
    }

    public void deleteClinician(int index) throws IOException {
        clinicians.remove(index);
        writeAll();
    }

    private void writeAll() throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_PATH))) {

            pw.println(String.join(",",
                    "clinician_id", "first_name", "last_name", "title", "speciality",
                    "gmc_number", "phone_number", "email",
                    "workplace_id", "workplace_type",
                    "employment_status", "start_date"));

            for (Clinician c : clinicians) {
                pw.println(String.join(",",
                        c.getClinicianId(),
                        c.getFirstName(),
                        c.getLastName(),
                        c.getTitle(),
                        c.getSpeciality(),
                        c.getGmcNumber(),
                        c.getPhoneNumber(),
                        c.getEmail(),
                        c.getWorkplaceId(),
                        c.getWorkplaceType(),
                        c.getEmploymentStatus(),
                        c.getStartDate()));
            }
        }
    }
}
