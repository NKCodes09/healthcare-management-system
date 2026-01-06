package model;

public class Prescription {

    private String prescriptionId;
    private String patientId;
    private String medication;
    private String dosage;
    private String pharmacy;
    private String status;

    public Prescription(String prescriptionId, String patientId,
            String medication, String dosage,
            String pharmacy, String status) {
        this.prescriptionId = prescriptionId;
        this.patientId = patientId;
        this.medication = medication;
        this.dosage = dosage;
        this.pharmacy = pharmacy;
        this.status = status;
    }

    public String toText() {
        return "Prescription ID: " + prescriptionId +
                "\nPatient ID: " + patientId +
                "\nMedication: " + medication +
                "\nDosage: " + dosage +
                "\nPharmacy: " + pharmacy +
                "\nStatus: " + status + "\n";
    }
}
