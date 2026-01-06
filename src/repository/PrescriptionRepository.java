package repository;

import model.Prescription;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionRepository {

    private List<Prescription> prescriptions = new ArrayList<>();

    public void addPrescription(Prescription prescription) {
        prescriptions.add(prescription);
        saveToFile(prescription);
    }

    public void loadPrescriptions(String path) {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        String line;
        br.readLine(); // header
        while ((line = br.readLine()) != null) {
            String[] d = line.split(",");
            prescriptions.add(new Prescription(
                    d[0], d[1], d[2], d[3], d[4], d[5]
            ));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    private void saveToFile(Prescription prescription) {
        try (FileWriter fw = new FileWriter("prescriptions.txt", true)) {
            fw.write(prescription.toText());
            fw.write("\n-----------------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Prescription> getAllPrescriptions() {
        return prescriptions;
    }
}
