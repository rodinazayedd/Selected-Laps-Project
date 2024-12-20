package org.example.demo1001.model;

import java.io.File;

public class ExcelDocument extends Document{


    public ExcelDocument(String name, String type, String date, File file) {
        super(type, name, date, file);
    }
    public ExcelDocument(int id, String name, String type, File file, String date) {
        super(id, name, type, file, date);
    }

    public String getExcelType(){
        return "xlsx";
    }

    @Override
    public void open() {

    }
}
