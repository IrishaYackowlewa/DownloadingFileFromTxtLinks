package ru.sbt.testTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class Main {
    static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        final int COUNT_THREADS = 3;
        int BYTES_PER_SECOND = 500 * 1024;
        String fileName = "";
        String path = "";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Введите полный путь и название файла для обработки(Пример: D:/files/file.txt): ");
            fileName = reader.readLine();
            System.out.println("Введите полный путь по которому надо сохранять файлы(Пример: D:/files/):");
            path = reader.readLine();

            if (!Files.exists(Paths.get(path))) {
                throw new InvalidPathException(path, "path is not correct");
            }

            ExecutionManager executionManager = new ExecutionManagerImpl();
            ResultsThreads serviceThreadPool =
                    executionManager.execute(fileName, path, BYTES_PER_SECOND, COUNT_THREADS);

            System.out.println("Колличество загруженных файлов " + serviceThreadPool.getCompletedTaskCount());
            System.out.println("Колличество ошибок при загрузке " + serviceThreadPool.getFailedTaskCount());
            System.out.println("Колличество прерваных загрузок " + serviceThreadPool.getInterruptedTaskCount());
            System.out.println("Ссылки, по которым не были загружены файлы " + serviceThreadPool.getFailedTasks());
            System.out.println("Корректные ссылки " + serviceThreadPool.getSuccessTasks());

        } catch (IOException e) {
            logger.error("Error main file of links = {}", fileName, e);
        } catch (InvalidPathException e) {
            logger.error("Path is not correct = {}", path, e);
        }
    }
}
