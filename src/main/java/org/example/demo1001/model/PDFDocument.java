package org.example.demo1001.model;

import java.io.File;

public class PDFDocument extends Document{


    public PDFDocument(String name, String type, String date, File file) {

        super(type, name, date, file);
    }


    public PDFDocument(int id, String name, String type, File file, String date) {
        super(id, name, type, file, date);
    }

    @Override
    public void open() {

    }
}
