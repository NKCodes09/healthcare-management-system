package repository;

import model.Patient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PatientRepository {

    private static final String CSV_PATH = "data/patients.csv";
    private final List<Patient> patients = new ArrayList<>();

    public PatientRepository() {
        load(); // ✅ always load once
    }

    public void load() {
        patients.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {
            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                patients.add(new Patient(
                        d[0], d[1], d[2], d[3], d[4], d[5], d[6]));
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
        writeAll(); // ✅ writes using fixed CSV path
    }

    public void deletePatient(String nhs) throws IOException {
        patients.removeIf(p -> p.getNhsNumber().equals(nhs));
        writeAll();
    }

    private void writeAll() throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_PATH))) {

            pw.println("nhsNumber,firstName,lastName,dateOfBirth,phoneNumber,gender,registeredGpSurgery");

            for (Patient p : patients) {
                pw.println(String.join(",",
                        p.getNhsNumber(),
                        p.getFirstName(),
                        p.getLastName(),
                        p.getDateOfBirth(),
                        p.getPhoneNumber(),
                        p.getGender(),
                        p.getRegisteredGpSurgery()));
            }
        }
    }
}
