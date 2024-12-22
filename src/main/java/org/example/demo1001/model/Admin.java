package org.example.demo1001.model;

public class Admin extends User {


    public Admin(String userName, String password, String role, String status) {
        super(userName, password, role,status);
    }
    public Admin(int id, String userName, String password, String role, String status){
        super(id, userName, password, role, status);

    }
    public Admin(UserBuilder builder) {
        super(builder);
    }
}
