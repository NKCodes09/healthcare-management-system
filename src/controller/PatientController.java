package controller;

import model.Patient;
import repository.PatientRepository;

import java.util.List;

public class PatientController {

    private PatientRepository repository;

    public PatientController(PatientRepository repository) {
        this.repository = repository;
    }

    public void loadPatients(String path) {
        repository.loadPatients(path);
    }

    public List<Patient> getPatients() {
        return repository.getAllPatients();
    }

    public void addPatient(Patient patient) {
        repository.addPatient(patient);
    }
}
