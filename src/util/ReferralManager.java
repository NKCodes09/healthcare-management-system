package util;

import model.Referral;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReferralManager {

    private static ReferralManager instance;
    private List<Referral> referralQueue;

    private ReferralManager() {
        referralQueue = new ArrayList<>();
    }

    public static synchronized ReferralManager getInstance() {
        if (instance == null) {
            instance = new ReferralManager();
        }
        return instance;
    }

    public void processReferral(Referral referral) {
        referralQueue.add(referral);
        writeToFile(referral);
    }

    private void writeToFile(Referral referral) {
        try (FileWriter writer = new FileWriter("referrals.txt", true)) {
            writer.write(referral.toText());
            writer.write("\n-----------------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
