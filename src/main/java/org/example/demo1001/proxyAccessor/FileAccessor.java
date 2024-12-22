package org.example.demo1001.proxyAccessor;

import org.example.demo1001.model.Document;

import java.io.File;

public interface FileAccessor {
    File loadFile(Document document);
}
