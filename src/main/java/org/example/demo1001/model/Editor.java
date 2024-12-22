package org.example.demo1001.model;

public class Editor extends User {


    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public Editor(String userName, String password, String role, String status) {
        super(userName, password, role, status);
    }
    public Editor(int id, String userName, String password, String role, String status){
        super(id, userName, password, role, status);

    }
    public Editor(UserBuilder builder) {
        super(builder);
    }
}
