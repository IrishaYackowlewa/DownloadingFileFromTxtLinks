package ru.sbt.testTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        final int COUNT_THREADS = 3;
        int BYTES_PER_SECOND = 500 * 1024;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String fileName = getData("Введите полный путь и название файла для обработки (Пример: D:/files/file.txt) или stop, чтобы завершить",
                    "Файл не найден", "stop", reader);

            if (!fileName.equals("stop")) {
                String path = getData("Введите полный путь по которому надо сохранять файлы (Пример: D:/files/) или stop, чтобы завершить",
                        "Путь не найден", "stop", reader);

                if (!(path.equals("stop"))) {
                    ExecutionManager executionManager = new ExecutionManagerImpl();
                    ResultsThreads serviceThreadPool =
                            executionManager.execute(fileName, path, BYTES_PER_SECOND, COUNT_THREADS);

                    System.out.println("Колличество загруженных файлов " + serviceThreadPool.getCompletedTaskCount());
                    System.out.println("Колличество ошибок при загрузке " + serviceThreadPool.getFailedTaskCount());
                    System.out.println("Колличество прерваных загрузок " + serviceThreadPool.getInterruptedTaskCount());
                    System.out.println("Ссылки, по которым не были загружены файлы " + serviceThreadPool.getFailedTasks());
                    System.out.println("Корректные ссылки " + serviceThreadPool.getSuccessTasks());
                }
            }
        } catch (IOException e) {
            logger.error("Error main file of links ", e);
        }
    }

    private static String getData(String messRequest, String messError,
                                  String stopWord, BufferedReader reader) throws IOException {
        String result;
        while (true) {
            System.out.println(messRequest);
            result = reader.readLine();
            if (Files.exists(Paths.get(result)) || result.equals(stopWord)) break;
            System.out.println(messError);
        }
        return result;
    }
}
