package org.example.demo1001.model;

import java.io.File;

public class DocumentBuilder {
    int id;
    String name;
    String type;
    File file;
    String date;
    boolean isPrivate;

    public DocumentBuilder(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public DocumentBuilder id(int id) {
        this.id = id;
        return this;
    }

    public DocumentBuilder file(File file) {
        this.file = file;
        return this;
    }

    public DocumentBuilder date(String date) {
        this.date = date;
        return this;
    }

    public DocumentBuilder isPrivate(boolean isPrivate){
        this.isPrivate =isPrivate;
        return this;
    }
    public Document build() {
        if (type.equalsIgnoreCase("PDF")) {
            return new PDFDocument(this);
        } else if (type.equalsIgnoreCase("Word")) {
            return new WordDocument(this);
        } else if (type.equalsIgnoreCase("Excel")) {
            return new ExcelDocument(this);
        }
        throw new IllegalArgumentException("Unknown document type: " + type);
    }
}
