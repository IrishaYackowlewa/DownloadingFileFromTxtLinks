package ru.sbt.test_task;

import java.util.List;

public interface ResultsThreads {
    List<String> getFailedTasks();
    List<String> getSuccessTasks();
    int getCompletedTaskCount();
    int getFailedTaskCount();
    int getInterruptedTaskCount();
}
