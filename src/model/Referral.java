package model;

/**
 * Referral
 * --------
 * Domain model representing a referral.
 * MODEL layer only.
 */
public class Referral {

    private String referralId;
    private String patientId;
    private String referringClinicianId;
    private String referralDate;
    private String urgencyLevel;
    private String referralReason;
    private String clinicalSummary;
    private String status;

    public Referral(
            String referralId,
            String patientId,
            String referringClinicianId,
            String referralDate,
            String urgencyLevel,
            String referralReason,
            String clinicalSummary,
            String status) {
        this.referralId = referralId;
        this.patientId = patientId;
        this.referringClinicianId = referringClinicianId;
        this.referralDate = referralDate;
        this.urgencyLevel = urgencyLevel;
        this.referralReason = referralReason;
        this.clinicalSummary = clinicalSummary;
        this.status = status;
    }

    public String getReferralId() {
        return referralId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getReferringClinicianId() {
        return referringClinicianId;
    }

    public String getReferralDate() {
        return referralDate;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public String getReferralReason() {
        return referralReason;
    }

    public String getClinicalSummary() {
        return clinicalSummary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
