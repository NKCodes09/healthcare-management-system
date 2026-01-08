package repository;

import model.Referral;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class ReferralManager {

    private static ReferralManager instance;

    private ReferralManager() {
        // private constructor
    }

    public static synchronized ReferralManager getInstance() {
        if (instance == null) {
            instance = new ReferralManager();
        }
        return instance;
    }

    /**
     * Processes referral using Singleton:
     * - Writes readable referral text file
     * - Does NOT send real email (per assignment)
     */
    public void processReferral(Referral r) throws IOException {
        writeReferralTextFile(r);
    }

    private void writeReferralTextFile(Referral r) throws IOException {

        String fileName = "referral_" + r.getReferralId() + ".txt";

        try (FileWriter writer = new FileWriter(fileName)) {

            writer.write("=== REFERRAL DOCUMENT ===\n");
            writer.write("Generated: " + LocalDateTime.now() + "\n\n");

            writer.write("Referral ID: " + r.getReferralId() + "\n");
            writer.write("Patient NHS Number: " + r.getPatientNhsNumber() + "\n");
            writer.write("Referring Clinician ID: " + r.getReferringClinicianId() + "\n");
            writer.write("From Facility: " + r.getFromFacilityId() + "\n");
            writer.write("To Facility: " + r.getToFacilityId() + "\n\n");

            writer.write("Urgency Level: " + r.getUrgencyLevel() + "\n");
            writer.write("Referral Date: " + r.getReferralDate() + "\n\n");

            writer.write("Reason for Referral:\n");
            writer.write(r.getReferralReason() + "\n\n");

            writer.write("Clinical Summary:\n");
            writer.write(r.getClinicalSummary() + "\n\n");

            writer.write("Requested Investigations:\n");
            writer.write(r.getRequestedInvestigations() + "\n\n");

            writer.write("Status: " + r.getStatus() + "\n");
            writer.write("Notes: " + r.getNotes() + "\n");

            writer.write("\n=== END OF REFERRAL ===\n");
        }
    }
}
