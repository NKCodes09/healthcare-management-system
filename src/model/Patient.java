package model;

public class Patient {

    // =========================
    // FIELDS
    // =========================
    private String patientId;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String nhsNumber;
    private String gender;
    private String phoneNumber;
    private String gpSurgeryId;

    // =========================
    // FULL CONSTRUCTOR (CSV)
    // =========================
    public Patient(
            String patientId,
            String firstName,
            String lastName,
            String dateOfBirth,
            String nhsNumber,
            String gender,
            String phoneNumber,
            String email,
            String address,
            String postcode,
            String emergencyContactName,
            String emergencyContactPhone,
            String registrationDate,
            String gpSurgeryId) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.nhsNumber = nhsNumber;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.gpSurgeryId = gpSurgeryId;
    }

    // =========================
    // SIMPLIFIED CONSTRUCTOR (GUI)
    // =========================
    public Patient(
            String patientId,
            String firstName,
            String lastName,
            String dateOfBirth,
            String nhsNumber,
            String gender,
            String phoneNumber,
            String gpSurgeryId) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.nhsNumber = nhsNumber;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.gpSurgeryId = gpSurgeryId;

        
    }

    // =========================
    // GETTERS
    // =========================
    public String getPatientId() {
        return patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getNhsNumber() {
        return nhsNumber;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getGpSurgeryId() {
        return gpSurgeryId;
    }
}
