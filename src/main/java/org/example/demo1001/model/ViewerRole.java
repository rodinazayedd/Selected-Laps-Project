package org.example.demo1001.model;

public class ViewerRole extends UserRole{
    boolean pending = false;

    public ViewerRole( String userName, String password, String role,String status) {
        super(userName, password, role, status);
    }
    public ViewerRole(int id, String userName, String password, String role, String status){
        super(id, userName, password, role, status);

    }
}
