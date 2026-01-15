package controller;

import model.Patient;
import repository.PatientRepository;

import java.io.IOException;
import java.util.List;

public class PatientController {

    private final PatientRepository repository = new PatientRepository();

    public List<Patient> getAll() {
        return repository.getAll();
    }

    public void add(Patient p) throws IOException {
        repository.add(p);
    }

    public void update(int index, Patient p) throws IOException {
        repository.getAll().set(index, p);
        repository.updateAll();
    }

    public void delete(int index) throws IOException {
        repository.delete(index);
    }
}
