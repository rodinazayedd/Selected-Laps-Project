package org.example.demo1001.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo1001.MainApplication;
import org.example.demo1001.factory.DocumentFactory;
import org.example.demo1001.model.Document;
import org.example.demo1001.proxyAccessor.FileAccessor;
import org.example.demo1001.proxyAccessor.FileAccessorProxy;
import org.example.demo1001.repository.DocumentRepository;
import org.example.demo1001.repository.SessionRepo;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class ViewerViewController implements Initializable {

    @FXML
    private VBox fileContainer;



    private Document createDocument(File file) {
        DocumentFactory factory = DocumentFactory.getInstance();
        return factory.createDocument(file.getName(), LocalDateTime.now().toString(), file);
    }

    private void showAlert(String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public void addFileComponent(Document document) {
        HBox fileComponent = createFileComponent(document);
        fileComponent.setId(String.valueOf(document.getId()));
        fileContainer.getChildren().add(fileComponent);
    }



    private HBox createFileComponent(Document document) {
        HBox fileComponent = new HBox(110);
        fileComponent.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1; -fx-background-color: #f0f0f0;");
        fileComponent.setMinHeight(60);
        fileComponent.setPrefHeight(60);
        fileComponent.setMaxHeight(60);

        Label nameLabel = createLabel(document.getName());
        Label typeLabel = createLabel(document.getType());
        Label dateLabel = createLabel("created at\n" + document.getDate());

        HBox buttonContainer = createButtonContainer(document);

        fileComponent.getChildren().addAll(nameLabel, typeLabel, dateLabel, buttonContainer);
        return fileComponent;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
        label.setPrefWidth(130);
        label.setMinWidth(130);
        label.setMaxWidth(130);
        return label;
    }

    private HBox createButtonContainer(Document document) {
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.setSpacing(20);

        Button openButton = createOpenButton(document);

        buttonContainer.getChildren().addAll(openButton);
        return buttonContainer;
    }

    private Button createOpenButton(Document document) {
        Button openButton = new Button("Open");
        openButton.setStyle("-fx-font-size: 12px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 5px 10px;");
        openButton.setMinWidth(Region.USE_COMPUTED_SIZE);
        openButton.setPrefWidth(Region.USE_COMPUTED_SIZE);
        openButton.setMaxWidth(Double.MAX_VALUE);

        openButton.setOnAction(event -> openFile(document));
        return openButton;
    }


    private void openFile(Document document) {
        FileAccessor fileAccessor = new FileAccessorProxy();
        document.setFile(fileAccessor.loadFile(document));

        if (document.getFile() != null && document.getFile().exists()) {
            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(document.getFile());  // Opens the file with the default application
            } catch (IOException e) {
                showAlert("Error Opening File", "Unable to open the file: " + e.getMessage());
            }
        }
    }



    @FXML
    protected void logout() {
        try {
            // Load the dashboard FXML file (make sure the path is correct)
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("LoginPage.fxml"));
            Parent dashboardRoot = fxmlLoader.load();

            // Get the current stage
            Stage stage = (Stage) fileContainer.getScene().getWindow();

            // Set the new scene (dashboard scene) on the stage
            Scene dashboardScene = new Scene(dashboardRoot);
            stage.setScene(dashboardScene);
            stage.show();
            SessionRepo.logout();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load dashboard: " + e.getMessage());
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DocumentRepository documentRepository = DocumentRepository.getInstance();
        List<Document> documentList = documentRepository.getAllDocuments();
        documentList.forEach(this::addFileComponent);
    }
}
