package ru.sbt.testTask;

import org.junit.*;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class FileLinksImplTest {

    private FileLinksImpl fileLinksImpl;

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

    @Before
    public void initTest() {
    }

    @Test
    public void testReadLinks() throws IOException {
        fileLinksImpl = new FileLinksImpl("fileTest.txt");
        List<String> listLinks = Files.newBufferedReader(Paths.get("testFile.txt")).lines().collect(Collectors.toList());
        List<String> listLinksResult = fileLinksImpl.readLinks();

        assertEquals(listLinks, listLinksResult);
    }

    @Test
    public void testCreateFile() throws IOException {
        thrown.expect(IOException.class);
        fileLinksImpl = new FileLinksImpl("tele.txt");
        fileLinksImpl.readLinks();
    }
}