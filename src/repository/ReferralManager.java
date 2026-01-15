package repository;

import model.Referral;
import java.io.*;

public class ReferralManager {

    private static ReferralManager instance;

    private ReferralManager() {
    }

    public static synchronized ReferralManager getInstance() {
        if (instance == null)
            instance = new ReferralManager();
        return instance;
    }

    /**
     * Saves referral to CSV and generates TXT output
     */
    public void processReferral(Referral r, ReferralRepository repo) throws IOException {

        // ✅ Correct repository method
        repo.addReferral(r);

        // ✅ Generate output file
        writeFile(r);
    }

    private void writeFile(Referral r) throws IOException {

        File dir = new File("output/referrals");
        if (!dir.exists())
            dir.mkdirs();

        File f = new File(dir, "referral_" + r.getReferralId() + ".txt");

        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {

            pw.println("REFERRAL");
            pw.println("==============================");
            pw.println("Referral ID: " + r.getReferralId());
            pw.println("Patient ID: " + r.getPatientId());
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
