package model;

public class Patient {

    private String patientId;
    private String name;
    private String dateOfBirth;
    private String nhsNumber;
    private String contactDetails;
    private String gpSurgery;

    public Patient(String patientId, String name, String dateOfBirth,
            String nhsNumber, String contactDetails, String gpSurgery) {
        this.patientId = patientId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.nhsNumber = nhsNumber;
        this.contactDetails = contactDetails;
        this.gpSurgery = gpSurgery;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getNhsNumber() {
        return nhsNumber;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public String getGpSurgery() {
        return gpSurgery;
    }

    @Override
    public String toString() {
        return name + " (NHS: " + nhsNumber + ")";
    }
}
