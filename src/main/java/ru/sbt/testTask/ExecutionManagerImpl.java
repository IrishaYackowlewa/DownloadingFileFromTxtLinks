package ru.sbt.testTask;

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

    @Override
    public ResultsThreads execute(String filename, String path, int limitSpeedBytesSecond, int countThread)
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
}
