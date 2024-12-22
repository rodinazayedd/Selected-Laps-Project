package org.example.demo1001.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.example.demo1001.MainApplication;
import org.example.demo1001.repository.SessionRepo;

import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private Button manageUsersBtn;

    @FXML
    private Button manageFilesBtn;

    /**
     * Handle the Logout action. Redirects to the login page.
     */
    public void handleLogout(ActionEvent event) {
        navigateTo("LoginPage.fxml", event);
        SessionRepo.logout();
    }

    /**
     * Exits the application when the "Exit" menu item is clicked.
     */
    public void exitApplication(ActionEvent event) {
        Stage stage = (Stage) manageUsersBtn.getScene().getWindow();
        stage.close(); // Closes the application window
    }

    /**
     * Navigate to the Manage Users page.
     */
    public void navigateToManageUsers() {
        navigateTo("admin-manage-users.fxml", null);
    }

    /**
     * Navigate to the Manage Files page.
     */
    public void navigateToManageFiles() {
        navigateTo("hello-view.fxml", null);
    }

    /**
     * General navigation method.
     *
     * @param fxmlFile the FXML file to navigate to.
     * @param event    the ActionEvent (can be null if triggered from a button).
     */
    private void navigateTo(String fxmlFile, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage;
            if (event != null) {
                stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
            } else {
                stage = (Stage) manageUsersBtn.getScene().getWindow();
            }

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
