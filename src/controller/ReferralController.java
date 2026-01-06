package controller;

import model.*;
import util.ReferralManager;

public class ReferralController {

    public void createReferral(Patient patient, Clinician clinician,
            String urgency, String summary) {

        Referral referral = new Referral(patient, clinician, urgency, summary);
        ReferralManager.getInstance().processReferral(referral);
    }
}
