package org.example.demo1001.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
    import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.demo1001.MainApplication;
import org.example.demo1001.factory.UserFactory;
import org.example.demo1001.model.UserRole;
import org.example.demo1001.repository.UserRepo;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminManageUsersController implements Initializable {

    @FXML
    private VBox userContainer;

    @FXML
    protected void onAddAdmin() {
            // Create a new Stage (window) for the small form
            Stage addAdminStage = new Stage();
            addAdminStage.setTitle("Add Admin");

            // Create the text fields for username and password
            TextField usernameField = new TextField();
            usernameField.setPromptText("Enter Username");

            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText("Enter Password");

            // Create the "Add" button
            Button addButton = new Button("Add");
            addButton.setOnAction(e -> {
                String username = usernameField.getText();
                String password = passwordField.getText();

                // Create a new user object
                if (!username.trim().isEmpty() && !password.trim().isEmpty()) {
                    UserRole newUser = UserFactory.getInstance().createUser(username,password,"admin","approved"); // Assuming UserRole constructor with username and password
                    UserRepo.getInstance().addAdmin(newUser); // Save the user to the database
                    addUserComponent(newUser); // Add the user to the UI

                    addAdminStage.close(); // Close the window after adding the user
                } else {
                    showAlert("Invalid Input", "Username and Password cannot be empty.");
                }
            });

            // Create a layout (VBox) to hold the text fields and button
            VBox layout = new VBox(10, usernameField, passwordField, addButton);
            layout.setAlignment(Pos.CENTER);

            // Create a scene for the new window
            Scene scene = new Scene(layout, 300, 200);
            addAdminStage.setScene(scene);

            // Show the window
            addAdminStage.show();

    }




    private void showAlert(String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public void addUserComponent(UserRole user) {
        HBox userComponent = createUserComponent(user);
        userComponent.setId(String.valueOf(user.getId()));
        userContainer.getChildren().add(userComponent);
    }

    private HBox createUserComponent(UserRole user) {
        HBox userComponent = new HBox(0);
        userComponent.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1; -fx-background-color: #f0f0f0;");
        userComponent.setMinHeight(60);
        userComponent.setPrefHeight(60);
        userComponent.setMaxHeight(60);

        userComponent.setAlignment(Pos.CENTER);
        userComponent.setMaxWidth(650);
        Label nameLabel = createLabel(user.getUserName());

        Label role = createLabel(user.getRole());


        Label statusLabel = createLabel(user.getStatus());
        HBox buttonContainer = createButtonContainer(user);
        if(user.getStatus().equals("approved")){
            userComponent.getChildren().addAll(nameLabel, role,statusLabel, buttonContainer);
        }
        else {
        HBox buttonCon =createRequestButtonContainer(user);
        Label label= new Label("   ");

            userComponent.getChildren().addAll(nameLabel, role,statusLabel, buttonContainer,label,buttonCon);
            userComponent.setMaxHeight(500);

        }
        return userComponent;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
        label.setPrefWidth(130);
        label.setMinWidth(130);
        label.setMaxWidth(130);
        return label;
    }

    private HBox createButtonContainer(UserRole user) {
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.setSpacing(20);

        Button editButton = createEditButton(user);

        buttonContainer.getChildren().addAll(editButton);
        return buttonContainer;
    }
    private HBox createRequestButtonContainer(UserRole user) {
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.setSpacing(20);

        Button editButton = createRequestButton(user);

        buttonContainer.getChildren().addAll(editButton);
        return buttonContainer;
    }

    private Button createEditButton(UserRole user) {
        Button editButton = new Button("Edit");
        editButton.setStyle("-fx-font-size: 12px; -fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 5px 10px;");
        editButton.setMaxWidth(Double.MAX_VALUE);

        editButton.setOnAction(event -> openEditWindow(user));
        return editButton;
    }
    private Button createRequestButton(UserRole user) {
        Button editButton = new Button("Request");
        editButton.setStyle("-fx-font-size: 12px; -fx-background-color: #f34e31; -fx-text-fill: white; -fx-padding: 5px 10px;");
        editButton.setMaxWidth(Double.MAX_VALUE);

        editButton.setOnAction(event -> openRequestWindow(user));
        return editButton;
    }

    private void openEditWindow(UserRole user) {
        Stage editWindow = new Stage();
        editWindow.initModality(Modality.APPLICATION_MODAL);
        editWindow.setTitle("Edit User");

        VBox editLayout = new VBox(20);
        editLayout.setAlignment(Pos.CENTER);

        Button deleteButton = new Button("Delete");
        Button changeNameButton = new Button("Change role");

        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 5px 10px;");
        changeNameButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 5px 10px;");

        // Handle delete button click
        deleteButton.setOnAction(deleteEvent -> {
            Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
            deleteAlert.setTitle("Delete User");
            deleteAlert.setHeaderText("Are you sure you want to delete this user?");
            deleteAlert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = deleteAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Delete the user from repository and remove the UI component
                UserRepo.getInstance().deleteUser(user);
                userContainer.getChildren().removeIf(node -> {
                    HBox hbox = (HBox) node;
                    Label label = (Label) hbox.getChildren().get(0);
                    return label.getText().equals(user.getUserName());
                });
                editWindow.close();
            }
        });

        changeNameButton.setOnAction(changeEvent -> openChangeRoleWindow(user));

        editLayout.getChildren().addAll(deleteButton, changeNameButton);

        Scene editScene = new Scene(editLayout, 300, 200);
        editWindow.setScene(editScene);
        editWindow.show();
    }
    private void openRequestWindow(UserRole user) {
        Stage editWindow = new Stage();
        editWindow.initModality(Modality.APPLICATION_MODAL);
        editWindow.setTitle("Request");

        VBox editLayout = new VBox(20);
        editLayout.setAlignment(Pos.CENTER);

        Button reject = new Button("Reject");
        Button accept = new Button("Accept");

        reject.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 5px 10px;");
        accept.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 5px 10px;");

        // Handle delete button click
        reject.setOnAction(deleteEvent -> {
            Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
            deleteAlert.setTitle("are you sure you want to Reject ");
            deleteAlert.setHeaderText("Are you sure you want to Reject this user?");
            deleteAlert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = deleteAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Delete the user from repository and remove the UI component
                UserRepo.getInstance().deleteUser(user);
                userContainer.getChildren().removeIf(node -> {
                    HBox hbox = (HBox) node;
                    Label label = (Label) hbox.getChildren().get(0);
                    return label.getText().equals(user.getUserName());
                });
                editWindow.close();
            }
        });

        accept.setOnAction(acceptEvent -> {
            Alert acceptAlert = new Alert(Alert.AlertType.CONFIRMATION);
            acceptAlert.setTitle("Are you sure you want to accept?");
            acceptAlert.setHeaderText("Are you sure you want to accept this user?");
            acceptAlert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = acceptAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Update status
                user.setPending(false);
                UserRepo.getInstance().acceptUser(user);

                // Now update the UI:
                for (Node node : userContainer.getChildren()) {
                    if (node instanceof HBox userComponent) {

                        // Check if the ID of the user component matches the user's ID
                        if (userComponent.getId().equals(String.valueOf(user.getId()))) {
                            // Find and update the role label
                            Label roleLabel = (Label) userComponent.getChildren().get(2); // Assuming it's at index 2
                            roleLabel.setText("approved");

                            // Remove the "Request" button
                            removeRequestButtonFromUserComponent(user);

                            // Optionally, trigger a UI refresh or re-add the updated user component
                            break;
                        }
                    }
                }
            }
        });


        editLayout.getChildren().addAll(reject, accept);

        Scene editScene = new Scene(editLayout, 300, 200);
        editWindow.setScene(editScene);
        editWindow.show();
    }


    private void removeRequestButtonFromUserComponent(UserRole user) {
        // Loop through all user components to find the one matching the user's ID
        for (Node node : userContainer.getChildren()) {
            if (node instanceof HBox userComponent) {

                // Check if the ID of the user component matches the user's ID
                if (userComponent.getId().equals(String.valueOf(user.getId()))) {
                    // Look through all child nodes of userComponent to find the 'Request' button container
                    for (Node childNode : userComponent.getChildren()) {
                        if (childNode instanceof HBox buttonContainer) {

                            // Check if the button container contains the "Request" button and remove it
                            buttonContainer.getChildren().removeIf(child ->
                                    child instanceof Button && ((Button) child).getText().equals("Request"));
                        }
                    }
                    break; // Exit after processing the correct user component
                }
            }
        }
    }



    //change role

    private void openChangeRoleWindow(UserRole user) {
        Stage changeRoleWindow = new Stage();
        changeRoleWindow.initModality(Modality.APPLICATION_MODAL);
        changeRoleWindow.setTitle("Change Role");

        // Create a ComboBox with the three roles
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("viewer", "editor", "admin");
        roleComboBox.setValue(user.getRole()); // Set the current role as the default value

        // Create buttons
        Button changeButton = new Button("Change");
        Button cancelButton = new Button("Cancel");

        // Handle the change button action
        changeButton.setOnAction(changeRoleEvent -> {
            String selectedRole = roleComboBox.getValue();
            if (selectedRole != null && !selectedRole.trim().isEmpty()) {
                // Assuming user has a setRole method to change the role
                changeRoleEvent(selectedRole,user);
                user.setRole(selectedRole);
                updateRoleInUI(user); // Update the UI with the new role
                changeRoleWindow.close();
            } else {
                showAlert("Invalid Role", "Please select a valid role.");
            }
        });

        // Handle the cancel button action
        cancelButton.setOnAction(cancelEvent -> changeRoleWindow.close());

        // Layout for the change role window
        VBox changeRoleLayout = new VBox(10, roleComboBox, changeButton, cancelButton);
        changeRoleLayout.setAlignment(Pos.CENTER);

        // Set up the scene
        Scene changeRoleScene = new Scene(changeRoleLayout, 300, 200);
        changeRoleWindow.setScene(changeRoleScene);
        changeRoleWindow.show();
    }


    private Boolean changeRoleEvent(String newRole, UserRole user) {
        if (!newRole.trim().isEmpty()) {
           UserRepo.getInstance().changeRole(user, newRole);
            user.setRole(newRole);
            updateUserComponentName(user);
            return true;
        } else {
            showAlert("Invalid Name", "newRole cannot be empty.");
            return false;
        }
    }

    private void updateUserComponentName(UserRole user) {
        // Loop through all user components to find the one matching the user's ID and update it
        for (Node node : userContainer.getChildren()) {
            if (node instanceof HBox) {
                HBox userComponent = (HBox) node;

                // Check if the ID of the user component matches the user's ID
                if (userComponent.getId().equals(String.valueOf(user.getId()))) {
                    // Get the name label (assuming it's the first child in HBox)
                    Label nameLabel = (Label) userComponent.getChildren().get(0);
                    nameLabel.setText(user.getUserName()); // Update the name displayed in the UI
                    break;
                }
            }
        }
    }

    private void updateRoleInUI(UserRole user) {
        // Loop through all user components to find the one matching the user's ID and update it
        for (Node node : userContainer.getChildren()) {
            if (node instanceof HBox) {
                HBox userComponent = (HBox) node;

                // Check if the ID of the user component matches the user's ID
                if (userComponent.getId().equals(String.valueOf(user.getId()))) {
                    // Get the name label (assuming it's the first child in HBox)
                    Label nameLabel = (Label) userComponent.getChildren().get(1);
                    nameLabel.setText(user.getRole()); // Update the name displayed in the UI
                    break;
                }
            }
        }
    }

    @FXML
    protected void onBackToDashboard() {
        try {
            // Load the dashboard FXML file (make sure the path is correct)
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("AdminDashboard.fxml"));
            Parent dashboardRoot = fxmlLoader.load();

            // Get the current stage
            Stage stage = (Stage) userContainer.getScene().getWindow();

            // Set the new scene (dashboard scene) on the stage
            Scene dashboardScene = new Scene(dashboardRoot);
            stage.setScene(dashboardScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load dashboard: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userContainer.setAlignment(Pos.CENTER);
        UserRepo userRepository = UserRepo.getInstance();
        List<UserRole> userList = userRepository.getAllUsers();
        userList.forEach(this::addUserComponent);
    }
}
