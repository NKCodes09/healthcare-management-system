package controller;

import model.Prescription;
import repository.PrescriptionRepository;

public class PrescriptionController {

    private PrescriptionRepository repository;

    public PrescriptionController(PrescriptionRepository repository) {
        this.repository = repository;
    }

    public void createPrescription(Prescription prescription) {
        repository.addPrescription(prescription);
    }
}
