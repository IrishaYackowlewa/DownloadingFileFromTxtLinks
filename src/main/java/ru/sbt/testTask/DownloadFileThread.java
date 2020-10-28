package ru.sbt.testTask;

import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.Callable;

public class DownloadFileThread implements Callable<Boolean> {
    private final String url;
    private final String directoryPath;
    private final int limitBytesSecond;

    public DownloadFileThread(String url, String directoryPath, int limitBytesSecond) {
        this.url = url;
        this.directoryPath = directoryPath;
        this.limitBytesSecond = limitBytesSecond;
    }

    @Override
    public Boolean call() throws IOException {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             OutputStream out = new ThrottledOutputStream(
                     new FileOutputStream(directoryPath + generateFilename()
                             + getFileExtension(url)), limitBytesSecond)
             // TODO: продумать реализацию через StringBuild?
        ) {
            byte data[] = new byte[1024];
            int count;
            while((count = in.read(data,0,1024)) != -1) {
                out.write(data, 0, count);
            }
        }
        return true;
    }

    private String generateFilename() {
        return UUID.randomUUID().toString().replace("-","").substring(0,10);
    }

    private String getFileExtension(String url) throws IOException {
        String mimeType = new URL(url).openConnection().getContentType();
        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
        try {
            return allTypes.forName(mimeType).getExtension();
        } catch (MimeTypeException e) {
            return "";
        }
    }
}
