package repository;

import model.Patient;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PatientRepository {

    private static final String CSV_PATH = "data/patients.csv";
    private final List<Patient> patients = new ArrayList<>();

    public PatientRepository() {
        load();
    }

    private void load() {

        patients.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {
            String line = br.readLine(); // skip header

            while ((line = br.readLine()) != null) {

                // Expect EXACTLY 8 columns
                String[] d = line.split(",");

                if (d.length != 8) {
                    // skip corrupted rows safely
                    continue;
                }

                patients.add(new Patient(
                        d[0], // patient_id
                        d[1], // first_name
                        d[2], // last_name
                        d[3], // date_of_birth
                        d[4], // nhs_number
                        d[5], // gender
                        d[6], // phone_number
                        d[7] // gp_surgery_id ✅
                ));
            }

        } catch (IOException e) {
            System.err.println("Failed to load patients.csv: " + e.getMessage());
        }
    }

    public List<Patient> getAll() {
        return patients;
    }

    public void addPatient(Patient p) throws IOException {
        patients.add(p);
        writeAll();
    }

    public void updatePatient(Patient updated) throws IOException {

        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getPatientId().equals(updated.getPatientId())) {
                patients.set(i, updated);
                break;
            }
        }
        writeAll();
    }

    public void deletePatient(String patientId) throws IOException {
        patients.removeIf(p -> p.getPatientId().equals(patientId));
        writeAll();
    }

    private void writeAll() throws IOException {

        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_PATH))) {

            // ✅ 8-column header ONLY
            pw.println(
                    "patient_id,first_name,last_name,date_of_birth,nhs_number,gender,phone_number,gp_surgery_id");

            for (Patient p : patients) {
                pw.println(String.join(",",
                        p.getPatientId(),
                        p.getFirstName(),
                        p.getLastName(),
                        p.getDateOfBirth(),
                        p.getNhsNumber(),
                        p.getGender(),
                        p.getPhoneNumber(),
                        p.getGpSurgeryId()));
            }
        }
    }
}
