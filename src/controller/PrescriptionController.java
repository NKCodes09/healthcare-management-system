package controller;

import model.Prescription;
import repository.PrescriptionManager;
import repository.PrescriptionRepository;

import java.io.IOException;
import java.util.List;

public class PrescriptionController {

    private final PrescriptionRepository repository = new PrescriptionRepository();
    private final PrescriptionManager manager = PrescriptionManager.getInstance();

    public List<Prescription> getAll() {
        return repository.getAll();
    }

    public void add(Prescription p) throws IOException {
        manager.createPrescription(p, repository);
    }

    public void update(int index, Prescription p) throws IOException {
        repository.update(index, p);
    }

    public void delete(int index) throws IOException {
        repository.delete(index);
    }
}
