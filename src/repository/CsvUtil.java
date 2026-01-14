package repository;

import java.util.ArrayList;
import java.util.List;

public class CsvUtil {

    public static String[] splitCsvLine(String line) {

        List<String> values = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(sb.toString().trim().replace("\"", ""));
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }

        values.add(sb.toString().trim().replace("\"", ""));
        return values.toArray(new String[0]);
    }

    public static String escape(String value) {
        if (value == null)
            return "";
        if (value.contains(",") || value.contains("\"")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}
