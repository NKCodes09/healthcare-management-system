package model;

public class Appointment {

    private String appointmentId;
    private String patientId;
    private String clinicianId;
    private String date;
    private String reason;
    private String status;

    public Appointment(String appointmentId, String patientId,
            String clinicianId, String date,
            String reason, String status) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.date = date;
        this.reason = reason;
        this.status = status;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getClinicianId() {
        return clinicianId;
    }

    public String getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }

    public String getStatus() {
        return status;
    }
}
