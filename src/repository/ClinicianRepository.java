package repository;

import model.Clinician;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ClinicianRepository {

    private List<Clinician> clinicians = new ArrayList<>();

    public void loadClinicians(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                clinicians.add(new Clinician(
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        data[4]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Clinician> getAllClinicians() {
        return clinicians;
    }
}
