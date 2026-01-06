package repository;

import model.Patient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class PatientRepository {

    private List<Patient> patients = new ArrayList<>();

    public void loadPatients(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Patient patient = new Patient(
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        data[4],
                        data[5]);
                patients.add(patient);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Patient> getAllPatients() {
        return patients;
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }
}
