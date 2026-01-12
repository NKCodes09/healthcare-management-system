package repository;

import model.Referral;
import java.io.FileWriter;
import java.io.IOException;

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
        writeReferralTextFile(r);
    }

    private void writeReferralTextFile(Referral r) throws IOException {

        String fileName = "referral_" + r.getReferralId() + ".txt";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("=== REFERRAL DOCUMENT ===\n\n");
            writer.write("Referral ID: " + r.getReferralId() + "\n");
            writer.write("Urgency: " + r.getUrgencyLevel() + "\n");
            writer.write("Clinical Summary:\n" + r.getClinicalSummary() + "\n");
            writer.write("\n=== END ===\n");
        }
    }
}
