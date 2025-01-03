package org.example.demo1001.AccessControl;

import org.example.demo1001.model.User;
import org.example.demo1001.repository.SessionRepo;
import org.example.demo1001.repository.UserRepo;

public class UserAccessControl {
    // Private static instance
    private static UserAccessControl instance;

    // Private constructor to prevent instantiation
    private UserAccessControl() {}

    // Public method to get the instance of the class
    public static UserAccessControl getInstance() {
        if (instance == null) {
            synchronized (UserAccessControl.class) {
                if (instance == null) {
                    instance = new UserAccessControl();
                }
            }
        }
        return instance;
    }

    public String PermissionPage(String role){
        switch (role.toLowerCase()){
            case "admin":return ("AdminDashboard.fxml");
            case "editor": return("editor-view.fxml");
            case "viewer": return("viewer-view.fxml");
        }
        return "LoginPage";
    }

    public void assignRole(User user, String role) {
        UserRepo.getInstance().changeRole(user,role);
    }
    public boolean isAdmin(){
        if("admin".equals(SessionRepo.getCurrentUser().getRole().toLowerCase())){
            return true;
        }
        return false;
    }
}

