package controller;

import model.Clinician;
import repository.ClinicianRepository;

import java.io.IOException;
import java.util.List;

public class ClinicianController {

    private final ClinicianRepository repository = new ClinicianRepository();

    public List<Clinician> getAll() {
        return repository.getAll();
    }

    public void add(Clinician c) throws IOException {
        repository.add(c);
    }

    public void update(int index, Clinician c) throws IOException {
        repository.getAll().set(index, c);
        repository.updateAll();
    }

    public void delete(int index) throws IOException {
        repository.delete(index);
    }
}
