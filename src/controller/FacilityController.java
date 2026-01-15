package controller;

import model.Facility;
import repository.FacilityRepository;

import java.io.IOException;
import java.util.List;

public class FacilityController {

    private final FacilityRepository repository = new FacilityRepository();

    public List<Facility> getAll() {
        return repository.getAll();
    }

    public void add(Facility f) throws IOException {
        repository.add(f);
    }

    public void update(int index, Facility f) throws IOException {
        repository.getAll().set(index, f);
        repository.updateAll();
    }

    public void delete(int index) throws IOException {
        repository.delete(index);
    }
}
