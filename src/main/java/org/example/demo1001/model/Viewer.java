package org.example.demo1001.model;

public class Viewer extends User {
    boolean pending = false;

    public Viewer(String userName, String password, String role, String status) {
        super(userName, password, role, status);
    }
    public Viewer(int id, String userName, String password, String role, String status){
        super(id, userName, password, role, status);

    }

    public Viewer(UserBuilder builder) {
        super(builder);
    }
}
