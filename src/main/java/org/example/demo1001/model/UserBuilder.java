package org.example.demo1001.model;

public class UserBuilder {
     int id;
    protected String userName;
    protected String password;
    protected String role;
    protected boolean pending;
    protected String status;

    public UserBuilder(String userName, String password, String role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public UserBuilder id(int id) {
        this.id = id;
        return this;
    }

    public UserBuilder pending(boolean pending) {
        this.pending = pending;
        return this;
    }

    public UserBuilder status(String status) {
        this.status = status;
        return this;
    }

    public User build() {
        switch (role.toLowerCase()) {
            case "viewer":
                return new Viewer(this);
            case "editor":
                return new Editor(this);
            case "admin":
                return new Admin(this);
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}
