package ru.sbt.testTask;

import java.io.IOException;

public interface ExecutionManager {
    ResultsThreads execute(String filename, String path, int limitSpeedBytesSecond, int countThread) throws IOException;
}
