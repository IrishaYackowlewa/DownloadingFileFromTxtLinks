package ru.sbt.testTask;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public class DownloadFileThreadTest {

    DownloadFileThread downloadFileThread;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void beforeClass() {
        System.out.println("Start testing FileLinksTest.class");
    }

    @AfterClass
    public  static void afterClass() {
        System.out.println("End testing FileLinksTest.class");
    }

    @Test
    public void testCall() throws IOException {
        thrown.expect(IOException.class);
        downloadFileThread = new DownloadFileThread("https://i.gifer.com/YtRA.gif",
                "D:\\files\\", 1024*500);
        downloadFileThread.call();
    }

    @Test
    public void testCallWithMalformedURLException() throws IOException {
        thrown.expect(MalformedURLException.class);
        DownloadFileThread downloadFileThread = new DownloadFileThread("jkl",
                "D:\\files\\", 1024*500);
        downloadFileThread.call();
    }

    @Test(expected = FileNotFoundException.class)
    public void testCallWithFileNotFoundException() throws IOException {
        DownloadFileThread downloadFileThread = new DownloadFileThread("https://i.gifer.com/YtRA.gif",
                "D:\\fi\\", 1024*500);
        downloadFileThread.call();
    }

    @Test
    public void testCallWithIOException() throws IOException {
        thrown.expect(IOException.class);
        downloadFileThread = new DownloadFileThread("https://i.gifer.com/Yhgvcbnm,",
                "D:\\files\\", 1024*500);
        downloadFileThread.call();
    }

}