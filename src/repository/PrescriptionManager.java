package repository;

import model.Prescription;
import java.io.IOException;

public class PrescriptionManager {

    private static PrescriptionManager instance;

    private PrescriptionManager() {
    }
    

    public static synchronized PrescriptionManager getInstance() {
        if (instance == null) {
            instance = new PrescriptionManager();
        }
        return instance;
    }

    public void createPrescription(
            Prescription p,
            PrescriptionRepository repo) throws IOException {

        repo.add(p); // CSV
        PrescriptionWriter.write(p); // TXT
    }
}
