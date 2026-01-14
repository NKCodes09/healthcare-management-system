package repository;

import model.User;
import java.io.*;
import java.util.*;

public class UserRepository {

    private static final String CSV = "data/users.csv";
    private final List<User> users = new ArrayList<>();

    public UserRepository() {
        load();
    }

    private void load() {

        users.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV))) {

            br.readLine(); // header
            String line;

            while ((line = br.readLine()) != null) {
                String[] c = CsvUtil.splitCsvLine(line);
                users.add(new User(c[0], c[1], c[2], c.length > 3 ? c[3] : ""));
            }

        } catch (IOException e) {
            System.err.println("Failed to load users.csv");
        }
    }

    public User authenticate(String u, String p) {

        for (User user : users) {
            if (user.getUsername().equals(u) &&
                    user.getPassword().equals(p)) {
                return user;
            }
        }
        return null;
    }

    public boolean exists(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    public void add(User u) throws IOException {

        users.add(u);

        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV, true))) {
            pw.println(String.join(",",
                    u.getUsername(),
                    u.getPassword(),
                    u.getRole(),
                    u.getLinkedId()));
        }
    }
}
