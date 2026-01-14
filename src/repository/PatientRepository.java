package repository;

import model.Patient;

import java.io.*;
import java.util.*;

public class PatientRepository {

    private static final String CSV_PATH = "data/patients.csv";
    private final List<Patient> patients = new ArrayList<>();

    public PatientRepository() {
        load();
    }

    private void load() {
        patients.clear();

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

                patients.add(new Patient(
                        get(c, index, "patient_id"),
                        get(c, index, "first_name"),
                        get(c, index, "last_name"),
                        get(c, index, "date_of_birth"),
                        get(c, index, "nhs_number"),
                        get(c, index, "gender"),
                        get(c, index, "phone_number"),
                        get(c, index, "email"),
                        get(c, index, "address"),
                        get(c, index, "postcode"),
                        get(c, index, "emergency_contact_name"),
                        get(c, index, "emergency_contact_phone"),
                        get(c, index, "registration_date"),
                        get(c, index, "gp_surgery_id")));
            }

        } catch (IOException e) {
            System.err.println("Failed to load patients.csv: " + e.getMessage());
        }
    }

    private String get(String[] row, Map<String, Integer> index, String key) {
        Integer i = index.get(key);
        return (i != null && i < row.length) ? row[i].trim() : "";
    }

    public List<Patient> getAll() {
        return patients;
    }

    public void addPatient(Patient p) throws IOException {
        patients.add(p);
        writeAll();
    }

    public void updateAll() throws IOException {
        writeAll();
    }

    public void deletePatient(int index) throws IOException {
        patients.remove(index);
        writeAll();
    }

    private void writeAll() throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_PATH))) {

            pw.println(String.join(",",
                    "patient_id", "first_name", "last_name", "date_of_birth", "nhs_number",
                    "gender", "phone_number", "email", "address", "postcode",
                    "emergency_contact_name", "emergency_contact_phone",
                    "registration_date", "gp_surgery_id"));

            for (Patient p : patients) {
                pw.println(String.join(",",
                        p.getPatientId(),
                        p.getFirstName(),
                        p.getLastName(),
                        p.getDateOfBirth(),
                        p.getNhsNumber(),
                        p.getGender(),
                        p.getPhoneNumber(),
                        p.getEmail(),
                        p.getAddress(),
                        p.getPostcode(),
                        p.getEmergencyContactName(),
                        p.getEmergencyContactPhone(),
                        p.getRegistrationDate(),
                        p.getGpSurgeryId()));
            }
        }
    }
}
