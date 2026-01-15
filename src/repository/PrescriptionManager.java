package repository;

import model.Prescription;
import java.io.IOException;

/**
 * Singleton manager for prescription processing
 */
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

    /**
     * Saves prescription to CSV and generates TXT output
     */
    public void processPrescription(
            Prescription prescription,
            PrescriptionRepository repository) throws IOException {

        repository.addPrescription(prescription); // CSV
        PrescriptionWriter.write(prescription); // TXT
    }
}
