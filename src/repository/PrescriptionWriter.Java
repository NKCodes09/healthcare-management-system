package repository;

import model.Prescription;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Generates prescription output text files
 */
public class PrescriptionWriter {

    private static final String OUTPUT_DIR = "output/prescriptions";

    public static void write(Prescription p) throws IOException {

        File dir = new File(OUTPUT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(
                OUTPUT_DIR + "/prescription_" + p.getPrescriptionId() + ".txt");

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {

            pw.println("PRESCRIPTION");
            pw.println("==============================");
            pw.println("Prescription ID: " + p.getPrescriptionId());
            pw.println("Patient ID: " + p.getPatientId());
            pw.println("Clinician ID: " + p.getClinicianId());
            pw.println("Appointment ID: " + p.getAppointmentId());
            pw.println("Prescription Date: " + p.getPrescriptionDate());
            pw.println();

            pw.println("Medication: " + p.getMedicationName());
            pw.println("Dosage: " + p.getDosage());
            pw.println("Frequency: " + p.getFrequency());
            pw.println("Duration: " + p.getDurationDays() + " days");
            pw.println("Quantity: " + p.getQuantity());
            pw.println();

            pw.println("Instructions:");
            pw.println(p.getInstructions());
            pw.println();

            pw.println("Pharmacy: " + p.getPharmacyName());
            pw.println("Status: " + p.getStatus());
            pw.println("Issue Date: " + p.getIssueDate());
            pw.println("Collection Date: " + p.getCollectionDate());
        }
    }
}
