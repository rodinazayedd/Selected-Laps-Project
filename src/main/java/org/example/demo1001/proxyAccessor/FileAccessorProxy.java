package org.example.demo1001.proxyAccessor;

import org.example.demo1001.model.Document;

import java.io.File;

public class FileAccessorProxy implements FileAccessor {
    private RealFileAccessor realFileAccessor;


    @Override
    public File loadFile(Document document) {
        if (realFileAccessor == null) {
            realFileAccessor = new RealFileAccessor(document);  // Lazy initialization of RealFileLoader
        }
        return realFileAccessor.getFile();
    }
}
