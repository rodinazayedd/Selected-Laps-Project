package org.example.demo1001.model;

public class AdminRole extends UserRole{


    public AdminRole( String userName, String password, String role, String status) {
        super(userName, password, role,status);
    }
    public AdminRole(int id, String userName, String password, String role, String status){
        super(id, userName, password, role, status);

    }
}
