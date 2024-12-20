package org.example.demo1001.model;

public class EditorRole extends UserRole{


    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public EditorRole( String userName, String password, String role, String status) {
        super(userName, password, role, status);
    }
    public EditorRole(int id, String userName, String password, String role, String status){
        super(id, userName, password, role, status);

    }
}
