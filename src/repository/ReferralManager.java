package repository;

import model.Referral;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ReferralManager {

    private static ReferralManager instance;

    private ReferralManager() {
    }

    public static synchronized ReferralManager getInstance() {
        if (instance == null) {
            instance = new ReferralManager();
        }
        return instance;
    }

    /**
     * Process referral using the SAME repository instance
     */
    public void processReferral(Referral r, ReferralRepository repository) throws IOException {

        // 1️⃣ Persist to CSV via shared repository
        repository.addReferral(r);

        // 2️⃣ Generate referral text file
        writeReferralFile(r);
    }

    private static final String OUTPUT_DIR = "output/referrals";

    private void writeReferralFile(Referral r) throws IOException {

    File dir = new File(OUTPUT_DIR);
    if (!dir.exists()) {
        dir.mkdirs(); // ✅ auto-create folder
    }

    File file = new File(dir, "referral_" + r.getReferralId() + ".txt");

    try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
        pw.println("Referral ID: " + r.getReferralId());
        pw.println("Patient ID: " + r.getPatientId());
        pw.println("Clinician ID: " + r.getReferringClinicianId());
        pw.println("Date: " + r.getReferralDate());
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
