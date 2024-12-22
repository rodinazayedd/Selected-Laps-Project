package org.example.demo1001.model;



import java.io.File;

public abstract class Document {
    protected int id;
    protected String name;
    protected String type;
    protected File file;
    protected String date;
    protected boolean isPrivate=false;


    //Builder
    public Document(DocumentBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.type = builder.type;
        this.file = builder.file;
        this.date = builder.date;
        this.isPrivate= builder.isPrivate;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public File getFile() {
        return file;
    }

    public static String getRealType(String type){
        switch (type){
            case "PDF": return type;

            case "Word": return "docx";

            case "Excel": return "xlsx";
            default: return "";
        }
    }

    public Document(String type, String name, String date, File file) {
        this.name = name;
        this.type = type;
        this.date = date;
        this.file = file;
    }

    public Document(int id, String name, String type, File file, String date) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.file = file;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract void open();

    public void setType(String type) {
        this.type = type;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }
}