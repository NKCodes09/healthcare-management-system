package repository;

import model.Referral;
import java.io.*;

public class ReferralManager {

    private static ReferralManager instance;

    private static final String OUTPUT_DIR = "output/referrals";

    private ReferralManager() {
    }

    public static synchronized ReferralManager getInstance() {
        if (instance == null) {
            instance = new ReferralManager();
        }
        return instance;
    }

    /**
     * Process referral using shared repository instance
     */
    public void processReferral(Referral r, ReferralRepository repository) throws IOException {

        // Persist referral
        repository.addReferral(r);

        // Generate referral output text file
        writeReferralFile(r);
    }

    private void writeReferralFile(Referral r) throws IOException {

        File dir = new File(OUTPUT_DIR);
        if (!dir.exists())
            dir.mkdirs();

        File file = new File(dir, "referral_" + r.getReferralId() + ".txt");

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {

            pw.println("Referral ID: " + r.getReferralId());
            pw.println("Patient ID: " + r.getPatientId());
            pw.println("Referring Clinician ID: " + r.getReferringClinicianId());
            pw.println("Referral Date: " + r.getReferralDate());
            pw.println("Urgency: " + r.getUrgencyLevel());
            pw.println("Status: " + r.getStatus());
            pw.println();
            pw.println("Reason:");
            pw.println(r.getReferralReason());
            pw.println();
            pw.println("Clinical Summary:");
            pw.println(r.getClinicalSummary());
        }
    }
}
