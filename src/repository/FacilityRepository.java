package repository;

import model.Facility;
import java.io.*;
import java.util.*;

public class FacilityRepository {

    private static final String CSV_PATH = "data/facilities.csv";

    private final List<Facility> facilities = new ArrayList<>();
    private final List<String[]> rawRows = new ArrayList<>();
    private String header;

    public FacilityRepository() {
        load();
    }

    private void load() {

        facilities.clear();
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

                facilities.add(new Facility(
                        CsvUtil.get(c, index.get("facility_id")),
                        CsvUtil.get(c, index.get("facility_name")),
                        CsvUtil.get(c, index.get("facility_type")),
                        CsvUtil.get(c, index.get("address")),
                        CsvUtil.get(c, index.get("postcode")),
                        CsvUtil.get(c, index.get("phone_number")),
                        CsvUtil.get(c, index.get("email")),
                        CsvUtil.get(c, index.get("opening_hours")),
                        CsvUtil.get(c, index.get("manager_name")),
                        CsvUtil.get(c, index.get("capacity")),
                        CsvUtil.get(c, index.get("specialities_offered"))));
            }

        } catch (IOException e) {
            System.err.println("Failed to load facilities.csv");
        }
    }

    public List<Facility> getAll() {
        return facilities;
    }

    public void add(Facility f) throws IOException {

        facilities.add(f);

        rawRows.add(new String[] {
                f.getFacilityId(),
                f.getFacilityName(),
                f.getFacilityType(),
                f.getAddress(),
                f.getPostcode(),
                f.getPhoneNumber(),
                f.getEmail(),
                f.getOpeningHours(),
                f.getManagerName(),
                f.getCapacity(),
                f.getSpecialitiesOffered()
        });

        writeAll();
    }

    public void updateAll() throws IOException {
        writeAll();
    }

    public void delete(int index) throws IOException {
        facilities.remove(index);
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
