package ru.sbt.test_task;

import java.io.IOException;

public interface ExecutionManager {
    ResultsThreads execute() throws IOException;
}
