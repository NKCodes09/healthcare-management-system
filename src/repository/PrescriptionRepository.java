package repository;

import model.Prescription;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PrescriptionRepository
 * ----------------------
 * MODEL layer class responsible for:
 *  - Loading prescriptions from prescriptions.csv
 *  - Storing prescriptions in memory
 *  - Writing updates back to CSV
 *
 * IMPORTANT:
 *  - This class MUST match Prescription.java exactly
 *  - Column order MUST match CSV
 */
public class PrescriptionRepository {

    private final List<Prescription> prescriptions = new ArrayList<>();

    /* =====================================================
       LOAD FROM CSV
       Expected CSV header:
       prescriptionId,patientNhsNumber,clinicianId,medication,dosage,pharmacy,collectionStatus
       ===================================================== */

    public void load(String filePath) throws IOException {
        prescriptions.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // skip header

            while ((line = reader.readLine()) != null) {
                String[] cols = line.split(",");

                Prescription p = new Prescription(
                        cols[0].trim(), // prescriptionId
                        cols[1].trim(), // patientNhsNumber
                        cols[2].trim(), // clinicianId
                        cols[3].trim(), // medication
                        cols[4].trim(), // dosage
                        cols[5].trim(), // pharmacy
                        cols[6].trim()  // collectionStatus
                );

                prescriptions.add(p);
            }
        }
    }

    /* =====================================================
       CRUD OPERATIONS
       ===================================================== */

    public List<Prescription> getAll() {
        return prescriptions;
    }
    
    private void writePrescriptionText(Prescription p) throws IOException {

        String fileName = "prescription_" + p.getPrescriptionId() + ".txt";

        try (FileWriter writer = new FileWriter(fileName)) {

            writer.write("=== PRESCRIPTION ===\n\n");
            writer.write("Prescription ID: " + p.getPrescriptionId() + "\n");
            writer.write("Patient NHS Number: " + p.getPatientNhsNumber() + "\n");
            writer.write("Clinician ID: " + p.getClinicianId() + "\n\n");

            writer.write("Medication: " + p.getMedication() + "\n");
            writer.write("Dosage: " + p.getDosage() + "\n");
            writer.write("Pharmacy: " + p.getPharmacy() + "\n");
            writer.write("Collection Status: " + p.getCollectionStatus() + "\n\n");

            writer.write("=== END OF PRESCRIPTION ===\n");
        }
    }

    private void writeAll() throws IOException {

        try (PrintWriter pw = new PrintWriter(new FileWriter("data/prescriptions.csv"))) {

            pw.println("prescriptionId,patientNhsNumber,clinicianId,medication,dosage,pharmacy,collectionStatus");

            for (Prescription p : prescriptions) {
                pw.println(String.join(",",
                        p.getPrescriptionId(),
                        p.getPatientNhsNumber(),
                        p.getClinicianId(),
                        p.getMedication(),
                        p.getDosage(),
                        p.getPharmacy(),
                        p.getCollectionStatus()));
            }
        }
    }

    public void addPrescription(Prescription p) throws IOException {
        prescriptions.add(p);
        writeAll(); // CSV
        writePrescriptionText(p); // TXT
    }

    public void updatePrescription(Prescription updated) throws IOException {
        for (int i = 0; i < prescriptions.size(); i++) {
            if (prescriptions.get(i).getPrescriptionId()
                    .equals(updated.getPrescriptionId())) {
                prescriptions.set(i, updated);
                break;
            }
        }
        save("data/prescriptions.csv");
    }

    public void deletePrescription(String prescriptionId) throws IOException {
        prescriptions.removeIf(p ->
                p.getPrescriptionId().equals(prescriptionId));
        save("data/prescriptions.csv");
    }

    /* =====================================================
       SAVE TO CSV
       ===================================================== */

    private void save(String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {

            writer.println("prescriptionId,patientNhsNumber,clinicianId,medication,dosage,pharmacy,collectionStatus");

            for (Prescription p : prescriptions) {
                writer.println(
                        p.getPrescriptionId() + "," +
                        p.getPatientNhsNumber() + "," +
                        p.getClinicianId() + "," +
                        p.getMedication() + "," +
                        p.getDosage() + "," +
                        p.getPharmacy() + "," +
                        p.getCollectionStatus()
                );
            }
        }
    }
}
