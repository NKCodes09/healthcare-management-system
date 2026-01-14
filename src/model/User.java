package model;

public class User {

    private String username;
    private String password;
    private String role;
    private String linkedId;

    public User(String username, String password, String role, String linkedId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.linkedId = linkedId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getLinkedId() {
        return linkedId;
    }
}
