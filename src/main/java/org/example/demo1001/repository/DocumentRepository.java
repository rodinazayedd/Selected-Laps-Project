package org.example.demo1001.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.example.demo1001.model.Document;
import org.example.demo1001.factory.DocumentFactory;

public class DocumentRepository {


    private static DocumentRepository instance;

    private final Connection connection;


    private DocumentRepository() {
        this.connection = DatabaseConnection.getConnection();
    }


    public static synchronized DocumentRepository getInstance() {
        if (instance == null) {
            instance = new DocumentRepository();
        }
        return instance;
    }


    public Boolean saveDocument(Document document) {
        String sql = "INSERT INTO files (file_name, file_type, file_data, uploaded_at) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             FileInputStream fis = new FileInputStream(document.getFile())) {

            stmt.setString(1, document.getName());
            stmt.setString(2, document.getType());

            stmt.setBinaryStream(3, fis, (int) document.getFile().length());

            stmt.setString(4, document.getDate());

            stmt.executeUpdate();

            System.out.println("Document saved to database: " + document.getName());
            return true;

        } catch (SQLException | IOException e) {
            System.err.println("Error saving document: " + e.getMessage());
            return false;
        }
    }


    public List<Document> getAllDocuments() {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM files";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String fileType = rs.getString("file_type");
                String name = rs.getString("file_name");

                Blob blob = rs.getBlob("file_data");
                byte[] fileData = blob.getBytes(1, (int) blob.length());


                File file = new File("E:\\spring-course\\demo1001\\src\\main\\java\\org\\example\\demo1001\\saved_files\\" + name+"."+Document.getRealType(fileType));
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(fileData);
                } catch (IOException e) {
                    System.err.println("Error saving file to disk: " + e.getMessage());
                }

                String date = rs.getString("uploaded_at");


                DocumentFactory factory = DocumentFactory.getInstance();
                Document doc =factory.createDocument(name,date,file,fileType,id);

                documents.add(doc);
                System.out.println(doc.getName()+" fetched");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching documents: " + e.getMessage());
        }
        return documents;
    }

    public void deleteDocumentFromDick(Document document){
        File fileToDelete = document.getFile();
        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                System.out.println("File deleted: " + fileToDelete.getAbsolutePath());
            } else {
                System.err.println("Failed to delete file: " + fileToDelete.getAbsolutePath());
            }
        }

    }
    public void deleteDocumentFromDatabase(Document document){
        String sql = "DELETE FROM files WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, document.getId());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Document deleted from database: " + document.getName());
            } else {
                System.err.println("Document not found in database: " + document.getName());
            }
        } catch (SQLException e) {
            System.err.println("Error deleting document from database: " + e.getMessage());
        }
    }
    public void deleteDocument(Document document) {
        deleteDocumentFromDick(document);
        deleteDocumentFromDatabase(document);
    }

    public Boolean changeDocumentName(Document document, String newName) {
        // Get the current file and rename it on the disk
        File oldFile = document.getFile();
        String fileExtension = getFileExtension(oldFile);
        File newFile = new File(oldFile.getParent(), newName + fileExtension);

        // Rename the file on disk
        if (oldFile.renameTo(newFile)) {
            System.out.println("File renamed successfully: " + oldFile.getName() + " -> " + newFile.getName());

            // Update the document's file reference
            document.setName(newName);
            document.setFile(newFile); // Update the document with the new file path

            // Update the file name in the database
            String sql = "UPDATE files SET file_name = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, newName);
                stmt.setInt(2, document.getId());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Document name updated in database: " + newName);
                    return true;
                } else {
                    System.err.println("Document not found in database: " + document.getName());
                }
            } catch (SQLException e) {
                System.err.println("Error updating document name in database: " + e.getMessage());
            }
        } else {
            System.err.println("Failed to rename file: " + oldFile.getName());
        }
        return false;
    }

    // Helper method to get the file extension
    private String getFileExtension(File file) {
        String name = file.getName();
        int extensionIndex = name.lastIndexOf(".");
        if (extensionIndex > 0) {
            return name.substring(extensionIndex); // Return file extension (e.g., .pdf)
        }
        return ""; // Return empty string if no extension found
    }


}
