package org.example.demo1001.factory;

import org.example.demo1001.model.AdminRole;
import org.example.demo1001.model.EditorRole;
import org.example.demo1001.model.UserRole;
import org.example.demo1001.model.ViewerRole;

public class UserFactory {
    private static UserFactory instance;


    private UserFactory() {}


    public static UserFactory getInstance() {
        if (instance == null) {
            instance = new UserFactory();
        }
        return instance;
    }

        public UserRole createUser(String userName, String password, String role, String status) {
            switch (role.toLowerCase()) {
                case "viewer":
                    return new ViewerRole(userName, password, role,status);
                case "editor":
                    return new EditorRole(userName, password, role,status);
                case "admin":
                    return new AdminRole(userName, password, role,status);
                default:
                    throw new IllegalArgumentException("Invalid role: " + role);
            }
        }

    public UserRole createUser(int id,String userName, String password, String role,String status) {
        switch (role.toLowerCase()) {
            case "viewer":
                return new ViewerRole(id,userName, password, role,status);
            case "editor":
                return new EditorRole(id,userName, password, role,status);
            case "admin":
                return new AdminRole(id,userName, password, role,status);
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }


}
