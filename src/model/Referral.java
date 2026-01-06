package model;

public class Referral {

    private Patient patient;
    private Clinician clinician;
    private String urgency;
    private String clinicalSummary;

    public Referral(Patient patient, Clinician clinician,
            String urgency, String clinicalSummary) {
        this.patient = patient;
        this.clinician = clinician;
        this.urgency = urgency;
        this.clinicalSummary = clinicalSummary;
    }

    public String toText() {
        return "Referral\n" +
                "Patient: " + patient.getName() +
                "\nNHS Number: " + patient.getNhsNumber() +
                "\nReferred By: " + clinician.getName() +
                "\nUrgency: " + urgency +
                "\nSummary: " + clinicalSummary;
    }
}
