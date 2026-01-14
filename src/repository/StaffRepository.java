package repository;

import model.Staff;

import java.io.*;
import java.util.*;

public class StaffRepository {

    private static final String CSV_PATH = "data/staff.csv";

    private final List<Staff> staffList = new ArrayList<>();
    private final List<String[]> rawRows = new ArrayList<>();
    private String originalHeader;

    public StaffRepository() {
        load();
    }

    private void load() {

        staffList.clear();
        rawRows.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {

            originalHeader = br.readLine();
            if (originalHeader == null)
                return;

            String[] headers = CsvUtil.splitCsvLine(originalHeader);
            Map<String, Integer> index = new HashMap<>();

            for (int i = 0; i < headers.length; i++) {
                index.put(headers[i], i);
            }

            String line;
            while ((line = br.readLine()) != null) {

                String[] c = CsvUtil.splitCsvLine(line);
                rawRows.add(c);

                staffList.add(new Staff(
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
            System.err.println("Failed to load staff.csv: " + e.getMessage());
        }
    }

    public List<Staff> getAll() {
        return staffList;
    }

    public void addStaff(Staff s) throws IOException {

        staffList.add(s);

        String[] row = new String[originalHeader.split(",").length];
        row[0] = s.getStaffId();
        row[1] = s.getFirstName();
        row[2] = s.getLastName();
        row[3] = s.getRole();
        row[4] = s.getDepartment();
        row[5] = s.getFacilityId();
        row[6] = s.getPhoneNumber();
        row[7] = s.getEmail();
        row[8] = s.getEmploymentStatus();
        row[9] = s.getStartDate();
        row[10] = s.getLineManager();
        row[11] = s.getAccessLevel();

        rawRows.add(row);
        writeAll();
    }

    public void updateAll() throws IOException {
        writeAll();
    }

    public void deleteStaff(int index) throws IOException {
        staffList.remove(index);
        rawRows.remove(index);
        writeAll();
    }

    private void writeAll() throws IOException {

        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_PATH))) {

            pw.println(originalHeader);

            for (String[] row : rawRows) {
                String[] safe = new String[row.length];
                for (int i = 0; i < row.length; i++) {
                    safe[i] = csvSafe(row[i]);
                }
                pw.println(String.join(",", safe));
            }
        }
    }

    private String csvSafe(String v) {
        if (v == null)
            return "";
        if (v.contains(",") || v.contains("\"")) {
            v = v.replace("\"", "\"\"");
            return "\"" + v + "\"";
        }
        return v;
    }
}
