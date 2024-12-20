package org.example.demo1001.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.demo1001.MainApplication;
import org.example.demo1001.factory.UserFactory;
import org.example.demo1001.model.UserRole;
import org.example.demo1001.repository.UserRepo;

import java.io.IOException;

public class LoginPageController {

    @FXML
    private TextField userNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private RadioButton viewerRadio;

    @FXML
    private RadioButton editorRadio;

    @FXML
    private RadioButton adminRadio;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private ToggleGroup userTypeGroup;

    @FXML
    public void initialize() {
        userTypeGroup = new ToggleGroup();
        viewerRadio.setToggleGroup(userTypeGroup);
        editorRadio.setToggleGroup(userTypeGroup);
        adminRadio.setToggleGroup(userTypeGroup);

        loginButton.setOnAction(event -> login());
        registerButton.setOnAction(event -> register());
    }

    private void login() {
        String userName = userNameField.getText();
        String password = passwordField.getText();

        if (userName.isEmpty() || password.isEmpty()) {
            showError("Username or password cannot be empty.");
            return;
        }

        RadioButton selectedRadio = (RadioButton) userTypeGroup.getSelectedToggle();

        if (selectedRadio == null) {
            showError("Please select a user type.");
            return;
        }

        String selectedRole = selectedRadio.getText();
        String status ="unknown";
        UserRole user = UserFactory.getInstance().createUser(userName, password, selectedRole,status);

        // Check if the login credentials are valid
        if (UserRepo.getInstance().login(user)) {
            System.out.println("Login successful");
            navigateToPreProcessing(user);
        } else {
            showError("Username or password is incorrect.");
        }
    }



    private void register() {
        String userName = userNameField.getText();
        String password = passwordField.getText();
        String status ="approved";

        if (userName.isEmpty() || password.isEmpty()) {
            showError("Username or password cannot be empty.");
            return;
        }

        if (viewerRadio.isSelected()) {
            showInfo("Viewer registered successfully.");
        } else if (editorRadio.isSelected()) {
            status="pending";
            showInfo("Editor registration requested.");
        } else if (adminRadio.isSelected()) {
            showError("Admins cannot register.");
        } else {
            showError("Please select a user type.");
        }

        RadioButton selectedRadio = (RadioButton) userTypeGroup.getSelectedToggle();
        String selectedRole = selectedRadio.getText();

        System.out.println("in cotrl "+selectedRole);
        UserRole user = UserFactory.getInstance().createUser(userName,password,selectedRole,status);
        UserRepo.getInstance().register(user);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.show();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.show();
    }


    private void navigateToPreProcessing(UserRole user){
        System.out.println(user.getRole()+"-<<<<<<");
        switch (user.getRole().toLowerCase()){
            case "admin":navigateTo("AdminDashboard.fxml");
            break;
            case "editor": navigateTo("editor-view.fxml");
            break;
            case "viewer": navigateTo("viewer-view.fxml");
        }
    }

    private void navigateTo(String fxmlFile) {
        try {
            System.out.println(fxmlFile);
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(fxmlFile));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) userNameField.getScene().getWindow(); // userNameField is any node on the current scene

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            int dotIndex = fxmlFile.lastIndexOf('.');
            stage.setTitle(fxmlFile.substring(0, dotIndex));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
