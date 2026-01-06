package repository;

import model.Appointment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepository {

    private List<Appointment> appointments = new ArrayList<>();

    public void loadAppointments(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                appointments.add(new Appointment(
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        data[4],
                        data[5]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Appointment> getAllAppointments() {
        return appointments;
    }
}
