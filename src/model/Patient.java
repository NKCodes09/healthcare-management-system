package model;

public class Patient {

    private String patientId;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String nhsNumber;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private String postcode;
    private String emergencyContact;
    private String emergencyPhone;
    private String registrationDate;
    private String gpSurgeryId;

    public Patient(
            String patientId,
            String firstName,
            String lastName,
            String dateOfBirth,
            String nhsNumber,
            String gender,
            String phone,
            String email,
            String address,
            String postcode,
            String emergencyContact,
            String emergencyPhone,
            String registrationDate,
            String gpSurgeryId) {

        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.nhsNumber = nhsNumber;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.emergencyContact = emergencyContact;
        this.emergencyPhone = emergencyPhone;
        this.registrationDate = registrationDate;
        this.gpSurgeryId = gpSurgeryId;
    }

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

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public String getEmergencyPhone() {
        return emergencyPhone;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public String getGpSurgeryId() {
        return gpSurgeryId;
    }
}
