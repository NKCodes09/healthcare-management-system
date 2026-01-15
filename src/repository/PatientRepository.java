package repository;

import model.Patient;
import java.io.*;
import java.util.*;

public class PatientRepository {

    private static final String CSV_PATH = "data/patients.csv";

    private final List<Patient> patients = new ArrayList<>();
    private final List<String[]> rawRows = new ArrayList<>();
    private String header;

    public PatientRepository() {
        load();
    }

    private void load() {

        patients.clear();
        rawRows.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {

            header = br.readLine();
            if (header == null)
                return;

            String[] headers = CsvUtil.splitCsvLine(header);
            Map<String, Integer> index = new HashMap<>();

            for (int i = 0; i < headers.length; i++)
                index.put(headers[i], i);

            String line;
            while ((line = br.readLine()) != null) {

                String[] c = CsvUtil.splitCsvLine(line);
                rawRows.add(c);

                patients.add(new Patient(
                        CsvUtil.get(c, index.get("patient_id")),
                        CsvUtil.get(c, index.get("first_name")),
                        CsvUtil.get(c, index.get("last_name")),
                        CsvUtil.get(c, index.get("date_of_birth")),
                        CsvUtil.get(c, index.get("nhs_number")),
                        CsvUtil.get(c, index.get("gender")),
                        CsvUtil.get(c, index.get("phone_number")),
                        CsvUtil.get(c, index.get("email")),
                        CsvUtil.get(c, index.get("address")),
                        CsvUtil.get(c, index.get("postcode")),
                        CsvUtil.get(c, index.get("emergency_contact_name")),
                        CsvUtil.get(c, index.get("emergency_contact_phone")),
                        CsvUtil.get(c, index.get("registration_date")),
                        CsvUtil.get(c, index.get("gp_surgery_id"))));
            }

        } catch (IOException e) {
            System.err.println("Failed to load patients.csv");
        }
    }

    public List<Patient> getAll() {
        return patients;
    }

    public void add(Patient p) throws IOException {

        patients.add(p);

        rawRows.add(new String[] {
                p.getPatientId(),
                p.getFirstName(),
                p.getLastName(),
                p.getDateOfBirth(),
                p.getNhsNumber(),
                p.getGender(),
                p.getPhone(),
                p.getEmail(),
                p.getAddress(),
                p.getPostcode(),
                p.getEmergencyContact(),
                p.getEmergencyPhone(),
                p.getRegistrationDate(),
                p.getGpSurgeryId()
        });

        writeAll();
    }

    public void updateAll() throws IOException {
        writeAll();
    }

    public void delete(int index) throws IOException {
        patients.remove(index);
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
