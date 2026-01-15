package repository;

import model.Clinician;
import java.io.*;
import java.util.*;

public class ClinicianRepository {

    private static final String CSV_PATH = "data/clinicians.csv";

    private final List<Clinician> clinicians = new ArrayList<>();
    private final List<String[]> rawRows = new ArrayList<>();
    private String header;

    public ClinicianRepository() {
        load();
    }

    private void load() {

        clinicians.clear();
        rawRows.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {

            header = br.readLine();
            if (header == null)
                return;

            String[] headers = CsvUtil.splitCsvLine(header);
            Map<String, Integer> index = new HashMap<>();

            for (int i = 0; i < headers.length; i++)
                index.put(headers[i], i);

            String line;
            while ((line = br.readLine()) != null) {

                String[] c = CsvUtil.splitCsvLine(line);
                rawRows.add(c);

                clinicians.add(new Clinician(
                        CsvUtil.get(c, index.get("clinician_id")),
                        CsvUtil.get(c, index.get("first_name")),
                        CsvUtil.get(c, index.get("last_name")),
                        CsvUtil.get(c, index.get("title")),
                        CsvUtil.get(c, index.get("speciality")),
                        CsvUtil.get(c, index.get("gmc_number")),
                        CsvUtil.get(c, index.get("phone_number")),
                        CsvUtil.get(c, index.get("email")),
                        CsvUtil.get(c, index.get("workplace_id")),
                        CsvUtil.get(c, index.get("workplace_type")),
                        CsvUtil.get(c, index.get("employment_status")),
                        CsvUtil.get(c, index.get("start_date"))));
            }

        } catch (IOException e) {
            System.err.println("Failed to load clinicians.csv");
        }
    }

    public List<Clinician> getAll() {
        return clinicians;
    }

    public void add(Clinician c) throws IOException {

        clinicians.add(c);

        rawRows.add(new String[] {
                c.getClinicianId(),
                c.getFirstName(),
                c.getLastName(),
                c.getTitle(),
                c.getSpeciality(),
                c.getGmcNumber(),
                c.getPhoneNumber(),
                c.getEmail(),
                c.getWorkplaceId(),
                c.getWorkplaceType(),
                c.getEmploymentStatus(),
                c.getStartDate()
        });

        writeAll();
    }

    public void updateAll() throws IOException {
        writeAll();
    }

    public void delete(int index) throws IOException {
        clinicians.remove(index);
        rawRows.remove(index);
        writeAll();
    }

    private void writeAll() throws IOException {

        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_PATH))) {

            pw.println(header);

            for (String[] r : rawRows) {
                for (int i = 0; i < r.length; i++)
                    r[i] = CsvUtil.escape(r[i]);
                pw.println(String.join(",", r));
            }
        }
    }
}
