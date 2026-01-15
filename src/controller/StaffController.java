package controller;

import model.Staff;
import repository.StaffRepository;

import java.io.IOException;
import java.util.List;

public class StaffController {

    private final StaffRepository repository = new StaffRepository();

    public List<Staff> getAll() {
        return repository.getAll();
    }

    public void add(Staff s) throws IOException {
        repository.add(s);
    }

    public void update(int index, Staff s) throws IOException {
        repository.getAll().set(index, s);
        repository.updateAll();
    }

    public void delete(int index) throws IOException {
        repository.delete(index);
    }
}
