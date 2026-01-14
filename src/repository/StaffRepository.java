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

            String[] h = CsvUtil.splitCsvLine(header);
            Map<String, Integer> idx = new HashMap<>();
            for (int i = 0; i < h.length; i++)
                idx.put(h[i], i);

            String line;
            while ((line = br.readLine()) != null) {
                String[] c = CsvUtil.splitCsvLine(line);
                rawRows.add(c);

                staff.add(new Staff(
                        CsvUtil.get(c, idx.get("staff_id")),
                        CsvUtil.get(c, idx.get("first_name")),
                        CsvUtil.get(c, idx.get("last_name")),
                        CsvUtil.get(c, idx.get("role")),
                        CsvUtil.get(c, idx.get("department")),
                        CsvUtil.get(c, idx.get("facility_id")),
                        CsvUtil.get(c, idx.get("phone_number")),
                        CsvUtil.get(c, idx.get("email")),
                        CsvUtil.get(c, idx.get("employment_status")),
                        CsvUtil.get(c, idx.get("start_date")),
                        CsvUtil.get(c, idx.get("line_manager")),
                        CsvUtil.get(c, idx.get("access_level"))));
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
                s.getStaffId(), s.getFirstName(), s.getLastName(),
                s.getRole(), s.getDepartment(), s.getFacilityId(),
                s.getPhoneNumber(), s.getEmail(), s.getEmploymentStatus(),
                s.getStartDate(), s.getLineManager(), s.getAccessLevel()
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

            for (String[] row : rawRows) {

                String[] safe = new String[row.length];
                for (int i = 0; i < row.length; i++) {
                    safe[i] = csvSafe(row[i]);
                }

                pw.println(String.join(",", safe));
            }
        }
    }

    private String csvSafe(String value) {
        if (value == null)
            return "";
        if (value.contains(",") || value.contains("\"")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }

}
