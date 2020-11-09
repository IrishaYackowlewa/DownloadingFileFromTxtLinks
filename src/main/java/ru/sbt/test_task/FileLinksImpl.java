package ru.sbt.test_task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class FileLinksImpl implements FileLinks{
    private final File fileLinks;

    public FileLinksImpl(String fileName) {
        this.fileLinks = new File(fileName);
    }

    public ArrayList<String> readLinks() throws IOException {
        ArrayList<String> listLinks = new ArrayList<>();
        try (BufferedReader brFileLinks = new BufferedReader(new FileReader(fileLinks))) {
            String lineFile;
            while ((lineFile = brFileLinks.readLine()) != null) {
                lineFile = lineFile.trim();
                if (!lineFile.equals("")) {
                    Collections.addAll(listLinks, lineFile.split("[\\s]+"));
                }
            }
        }
        return listLinks;
    }
}


