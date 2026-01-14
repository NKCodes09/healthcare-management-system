package model;

public class Prescription {

    private String prescriptionId;
    private String patientId;
    private String clinicianId;
    private String appointmentId;
    private String prescriptionDate;
    private String medicationName;
    private String dosage;
    private String frequency;
    private String durationDays;
    private String quantity;
    private String instructions;
    private String pharmacyName;
    private String status;
    private String issueDate;
    private String collectionDate;

    public Prescription(
            String prescriptionId,
            String patientId,
            String clinicianId,
            String appointmentId,
            String prescriptionDate,
            String medicationName,
            String dosage,
            String frequency,
            String durationDays,
            String quantity,
            String instructions,
            String pharmacyName,
            String status,
            String issueDate,
            String collectionDate) {
        this.prescriptionId = prescriptionId;
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.appointmentId = appointmentId;
        this.prescriptionDate = prescriptionDate;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.durationDays = durationDays;
        this.quantity = quantity;
        this.instructions = instructions;
        this.pharmacyName = pharmacyName;
        this.status = status;
        this.issueDate = issueDate;
        this.collectionDate = collectionDate;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getClinicianId() {
        return clinicianId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getPrescriptionDate() {
        return prescriptionDate;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public String getDosage() {
        return dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getDurationDays() {
        return durationDays;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public String getStatus() {
        return status;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public String getCollectionDate() {
        return collectionDate;
    }
}
