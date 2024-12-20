package org.example.demo1001.model;

public abstract class UserRole {

    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String userName;
    String password;
    String role;
    boolean pending ;
    String status;

    public UserRole(int id, String userName, String password, String role, String status) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public UserRole(String userName, String password, String role,String status) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.status=status;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }


    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }
}