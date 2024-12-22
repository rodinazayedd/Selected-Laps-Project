package org.example.demo1001.factory;

import org.example.demo1001.model.DocumentBuilder;
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
        return new DocumentBuilder(clearTypeFromName(name),getFileType(file)).file(file).date(date).build();
    }

    public Document createDocument(String name, String date, File file,String type,int id) {
        return new DocumentBuilder(name,type).date(date).file(file).id(id).build();
    }

    public Document createDocument(String name, String date,String type,int id,boolean isPrivate) {
        return new DocumentBuilder(name,type).date(date).id(id).isPrivate(isPrivate).build();
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
