package repository;

import model.Staff;
import java.io.*;
import java.util.*;

public class StaffRepository {

    private static final String CSV_PATH = "data/staff.csv";

    private final List<Staff> staff = new ArrayList<>();
    private final List<String[]> rawRows = new ArrayList<>();
    private String header;

    public StaffRepository() {
        load();
    }

    private void load() {

        staff.clear();
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

                staff.add(new Staff(
                        CsvUtil.get(c, index.get("staff_id")),
                        CsvUtil.get(c, index.get("first_name")),
                        CsvUtil.get(c, index.get("last_name")),
                        CsvUtil.get(c, index.get("role")),
                        CsvUtil.get(c, index.get("department")),
                        CsvUtil.get(c, index.get("facility_id")),
                        CsvUtil.get(c, index.get("phone_number")),
                        CsvUtil.get(c, index.get("email")),
                        CsvUtil.get(c, index.get("employment_status")),
                        CsvUtil.get(c, index.get("start_date")),
                        CsvUtil.get(c, index.get("line_manager")),
                        CsvUtil.get(c, index.get("access_level"))));
            }

        } catch (IOException e) {
            System.err.println("Failed to load staff.csv");
        }
    }

    public List<Staff> getAll() {
        return staff;
    }

    public void add(Staff s) throws IOException {

        staff.add(s);

        rawRows.add(new String[] {
                s.getStaffId(),
                s.getFirstName(),
                s.getLastName(),
                s.getRole(),
                s.getDepartment(),
                s.getFacilityId(),
                s.getPhoneNumber(),
                s.getEmail(),
                s.getEmploymentStatus(),
                s.getStartDate(),
                s.getLineManager(),
                s.getAccessLevel()
        });

        writeAll();
    }

    public void updateAll() throws IOException {
        writeAll();
    }

    public void delete(int index) throws IOException {
        staff.remove(index);
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
