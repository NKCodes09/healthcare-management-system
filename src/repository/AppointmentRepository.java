package repository;

import model.Appointment;
import java.io.*;
import java.util.*;

public class AppointmentRepository {

    private static final String CSV_PATH = "data/appointments.csv";

    private final List<Appointment> appointments = new ArrayList<>();
    private final List<String[]> rawRows = new ArrayList<>();
    private String header;

    public AppointmentRepository() {
        load();
    }

    private void load() {

        appointments.clear();
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

                appointments.add(new Appointment(
                        CsvUtil.get(c, index.get("appointment_id")),
                        CsvUtil.get(c, index.get("patient_id")),
                        CsvUtil.get(c, index.get("clinician_id")),
                        CsvUtil.get(c, index.get("facility_id")),
                        CsvUtil.get(c, index.get("appointment_date")),
                        CsvUtil.get(c, index.get("appointment_time")),
                        CsvUtil.get(c, index.get("duration_minutes")),
                        CsvUtil.get(c, index.get("appointment_type")),
                        CsvUtil.get(c, index.get("status")),
                        CsvUtil.get(c, index.get("reason_for_visit")),
                        CsvUtil.get(c, index.get("notes")),
                        CsvUtil.get(c, index.get("created_date")),
                        CsvUtil.get(c, index.get("last_modified"))));
            }

        } catch (IOException e) {
            System.err.println("Failed to load appointments.csv");
        }
    }

    public List<Appointment> getAll() {
        return appointments;
    }

    public void add(Appointment a) throws IOException {

        appointments.add(a);

        rawRows.add(new String[] {
                a.getAppointmentId(),
                a.getPatientId(),
                a.getClinicianId(),
                a.getFacilityId(),
                a.getAppointmentDate(),
                a.getAppointmentTime(),
                a.getDurationMinutes(),
                a.getAppointmentType(),
                a.getStatus(),
                a.getReasonForVisit(),
                a.getNotes(),
                a.getCreatedDate(),
                a.getLastModified()
        });

        writeAll();
    }

    public void updateAll() throws IOException {
        writeAll();
    }

    public void delete(int index) throws IOException {
        appointments.remove(index);
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
