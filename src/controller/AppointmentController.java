package controller;

import model.Appointment;
import repository.AppointmentRepository;

import java.io.IOException;
import java.util.List;

public class AppointmentController {

    private final AppointmentRepository repository = new AppointmentRepository();

    public List<Appointment> getAll() {
        return repository.getAll();
    }

    public void add(Appointment a) throws IOException {
        repository.add(a);
    }

    public void update(int index, Appointment a) throws IOException {
        repository.getAll().set(index, a);
        repository.updateAll();
    }

    public void delete(int index) throws IOException {
        repository.delete(index);
    }
}
