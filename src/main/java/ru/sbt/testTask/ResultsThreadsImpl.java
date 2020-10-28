package ru.sbt.testTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResultsThreadsImpl implements ResultsThreads {
    private static Logger log = Logger.getLogger(ResultsThreadsImpl.class.getName());

    private List<Future<Boolean>> listFutures;
    private ArrayList<String> links;
    private ArrayList<String> failedTasks;
    private ArrayList<String> successTasks;
    private int failedTaskCount;
    private int interruptedTaskCount;
    private int completedTaskCount;

    public ResultsThreadsImpl(List<Future<Boolean>> tasks, ArrayList<String> links) {
        this.listFutures = tasks;
        this.links = links;
        this.failedTasks = new ArrayList<>();
        this.successTasks = new ArrayList<>();
        this.failedTaskCount = 0;
        this.interruptedTaskCount = 0;
        this.completedTaskCount = 0;
        getResults();
    }

    private void getResults() {
        for (int i = 0; i < links.size(); i++) {
            try {
                if (listFutures.get(i).get()) {
                    log.log(Level.INFO, "File downloaded by " + links.get(i));
                    successTasks.add(links.get(i));
                    this.completedTaskCount++;
                }
            } catch (CancellationException e) {
                log.log(Level.WARNING,"File download canceled " + links.get(i), e);
                this.interruptedTaskCount++;
                this.failedTasks.add(links.get(i));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                this.interruptedTaskCount++;
                this.failedTasks.add(links.get(i));
            } catch (ExecutionException e) {
                log.log(Level.WARNING,"File did not download " + links.get(i), e);
                this.failedTaskCount++;
                this.failedTasks.add(links.get(i));
            }
        }

    }

    @Override
    public List<String> getFailedTasks() {
        return failedTasks;
    }

    @Override
    public List<String> getSuccessTasks() {
        return successTasks;
    }

    @Override
    public int getCompletedTaskCount() {
        return completedTaskCount;
    }

    @Override
    public int getFailedTaskCount() {
        return failedTaskCount;
    }

    @Override
    public int getInterruptedTaskCount() {
        return interruptedTaskCount;
    }
}
