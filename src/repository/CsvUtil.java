package repository;

import java.util.ArrayList;
import java.util.List;

public class CsvUtil {

    /**
     * Splits a CSV line safely, respecting quoted values.
     * Example:
     * "Hello, world",Test → [Hello, world] [Test]
     */
    public static String[] splitCsvLine(String line) {

        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                tokens.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        tokens.add(current.toString().trim());
        return tokens.toArray(new String[0]);
    }

    /**
     * Safely gets a column value by index.
     * Prevents IndexOutOfBounds and null errors.
     */
    public static String get(String[] cols, Integer index) {

        if (cols == null || index == null)
            return "";
        if (index < 0 || index >= cols.length)
            return "";

        return cols[index].replace("\"", "").trim();
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
