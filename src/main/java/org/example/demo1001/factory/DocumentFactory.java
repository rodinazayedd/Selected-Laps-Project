package org.example.demo1001.factory;

import org.example.demo1001.model.*;

import java.io.File;

public class DocumentFactory {

    private static DocumentFactory instance;


    private DocumentFactory() {}


    public static DocumentFactory getInstance() {
        if (instance == null) {
            instance = new DocumentFactory();
        }
        return instance;
    }


    public Document createDocument(String name, String date, File file) {
        String type = getFileType(file);
        name = clearTypeFromName(name);
        switch (type.toLowerCase()) {
            case "pdf":
                return new PDFDocument(name, type, date, file);
            case "word":
                return new WordDocument(name, type, date, file);
            case "excel":
                return new ExcelDocument(name, type, date, file);
            default:
                throw new IllegalArgumentException("Unsupported document type: " + type);
        }
    }

    public Document createDocument(String name, String date, File file,String type,int id) {

        switch (type.toLowerCase()) {
            case "pdf":
                return new PDFDocument(id,name, type, file, date);
            case "word":
                return new WordDocument(id,name, type, file, date);
            case "excel":
                return new ExcelDocument(id,name, type, file, date);
            default:
                throw new IllegalArgumentException("Unsupported document type: " + type);
        }
    }


    private String getFileType(File file) {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".pdf")) {
            return "PDF";
        } else if (fileName.endsWith(".docx")) {
            return "Word";
        } else if (fileName.endsWith(".xlsx")) {
            return "Excel";
        } else {
            return "unknown";
        }
    }

    private String clearTypeFromName(String name){
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0) {
            return name.substring(0, dotIndex);
        }
        return name;
    }
}
