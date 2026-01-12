package repository;

import model.Clinician;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClinicianRepository {

    private static final String CSV_PATH = "data/clinicians.csv";
    private final List<Clinician> clinicians = new ArrayList<>();

    public ClinicianRepository() {
        load(); // ✅ load once
    }

    public void load() {
        clinicians.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {
            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                clinicians.add(new Clinician(
                        d[0], // clinicianId
                        d[1], // name
                        d[2], // role
                        d[3], // specialty
                        d[4] // workplace
                ));
            }

        } catch (IOException e) {
            System.err.println("Failed to load clinicians.csv: " + e.getMessage());
        }
    }

    public List<Clinician> getAll() {
        return clinicians;
    }

    public void add(Clinician c) throws IOException {
        clinicians.add(c);
        writeAll();
    }

    public void delete(String clinicianId) throws IOException {
        clinicians.removeIf(c -> c.getClinicianId().equals(clinicianId));
        writeAll();
    }

    private void writeAll() throws IOException {

        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_PATH))) {

            pw.println("clinicianId,name,role,specialty,workplace");

            for (Clinician c : clinicians) {
                pw.println(String.join(",",
                        c.getClinicianId(),
                        c.getName(),
                        c.getRole(),
                        c.getSpecialty(),
                        c.getWorkplace()));
            }
        }
    }
}
