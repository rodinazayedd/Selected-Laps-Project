package org.example.demo1001.proxyAccessor;

import org.example.demo1001.model.Document;
import org.example.demo1001.repository.DocumentRepository;

import java.io.File;

public class RealFileAccessor implements FileAccessor {
    private File file;

    public RealFileAccessor(Document document) {
           loadFile(document);
    }


    public File getFile() {
        return file;
    }

    @Override
    public File loadFile(Document document) {
        this.file = DocumentRepository.getInstance().getFileFromDatabase(document);
        return this.file;
    }
}
