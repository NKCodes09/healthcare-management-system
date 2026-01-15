package repository;

import model.Prescription;
import java.io.*;

public class PrescriptionWriter {

    private static final String OUTPUT_DIR = "output/prescriptions";

    public static void write(Prescription p) throws IOException {

        File dir = new File(OUTPUT_DIR);
        if (!dir.exists())
            dir.mkdirs();

        File file = new File(dir,
                "prescription_" + p.getPrescriptionId() + ".txt");

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {

            pw.println("PRESCRIPTION RECORD");
            pw.println("---------------------------");
            pw.println("Prescription ID: " + p.getPrescriptionId());
            pw.println("Patient ID: " + p.getPatientId());
            pw.println("Clinician ID: " + p.getClinicianId());
            pw.println("Appointment ID: " + p.getAppointmentId());
            pw.println("Prescription Date: " + p.getPrescriptionDate());
            pw.println();

            pw.println("Medication: " + p.getMedicationName());
            pw.println("Dosage: " + p.getDosage());
            pw.println("Frequency: " + p.getFrequency());
            pw.println("Duration: " + p.getDurationDays());
            pw.println("Quantity: " + p.getQuantity());
            pw.println();

            pw.println("Instructions:");
            pw.println(p.getInstructions());
            pw.println();

            pw.println("Pharmacy: " + p.getPharmacyName());
            pw.println("Status: " + p.getStatus());
            pw.println("Issued: " + p.getIssueDate());
            pw.println("Collected: " + p.getCollectionDate());
        }
    }
}
