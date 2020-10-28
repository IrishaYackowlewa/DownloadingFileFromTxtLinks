package ru.sbt.testTask;

import java.io.IOException;
import java.util.ArrayList;

public interface FileLinks {
    public ArrayList<String> readLinks() throws IOException;
}
