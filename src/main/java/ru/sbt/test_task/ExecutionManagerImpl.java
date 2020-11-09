package ru.sbt.test_task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExecutionManagerImpl implements ExecutionManager{
    private static Logger log = Logger.getLogger(ExecutionManagerImpl.class.getName());

    private final String filename;
    private final String path;
    private final int limitSpeedBytesSecond;
    private final int countThread;

    private ExecutionManagerImpl(Builder builder) {
        this.filename = builder.filename;
        this.path = builder.path;
        this.limitSpeedBytesSecond = builder.limitSpeedBytesSecond;
        this.countThread = builder.countThread;
    }

    @Override
    public ResultsThreads execute()
            throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(countThread);
        List<DownloadFileThread> listDownloadLink = new ArrayList<>();
        ArrayList<String> links = new FileLinksImpl(filename).readLinks();
        List<Future<Boolean>> futureLinks = null;

        for (String link: links ) {
            listDownloadLink.add(new DownloadFileThread(link, path, limitSpeedBytesSecond));
        }

        try {
            futureLinks = executorService.invokeAll(listDownloadLink);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.log(Level.WARNING,"download of files interrupted ", e);
        }

        executorService.shutdown();
        return new ResultsThreadsImpl(futureLinks, links);
    }

    public static class Builder {//надо правильно назвать
        private final String filename;
        private final String path;
        private int limitSpeedBytesSecond = 500 * 1024;
        private int countThread = 3;

        public Builder(String filename, String path) {
            this.filename = filename;
            this.path = path;
        }

        public Builder limitSpeedBytesSecond(int limitSpeedBytesSecond) {
            this.limitSpeedBytesSecond = limitSpeedBytesSecond;
            return this;
        }

        public Builder countThread(int countThread) {
            this.countThread = countThread;
            return this;
        }

        public ExecutionManagerImpl buidl() {
            return new ExecutionManagerImpl(this);
        }
    }
}
