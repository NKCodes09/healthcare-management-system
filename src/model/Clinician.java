package model;

public class Clinician {

    private String clinicianId;
    private String name;
    private String role;
    private String speciality;
    private String workplace;

    public Clinician(String clinicianId, String name, String role,
            String speciality, String workplace) {
        this.clinicianId = clinicianId;
        this.name = name;
        this.role = role;
        this.speciality = speciality;
        this.workplace = workplace;
    }

    public String getClinicianId() {
        return clinicianId;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getWorkplace() {
        return workplace;
    }

    @Override
    public String toString() {
        return name + " (" + speciality + ")";
    }
}
