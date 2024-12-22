package org.example.demo1001.model;

import java.io.File;

public class WordDocument extends Document{


    public WordDocument(String name, String type, String date, File file) {
        super(type, name, date, file);
    }
    public WordDocument(int id, String name, String type, File file, String date) {
        super(id, name, type, file, date);
    }
   public String getWordType(){
        return "docs";
   }

    @Override
    public void open() {

    }

    //builder
    public WordDocument(DocumentBuilder documentBuilder){
        super(documentBuilder);
        System.out.println("word2"+documentBuilder.isPrivate);
        System.out.println("word"+this.isPrivate);
    }
}
