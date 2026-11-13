package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SleepTrackerApp {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Вы не ввели путь к файлу");
            return;
        }

        Path filePath = Paths.get(args[0]);

        if (!Files.exists(filePath)) {
            System.out.println("Файл не найден");
            return;
        }

        try {
           List<String> lines = readFile(filePath);
           System.out.println("Файл успешно загружен");
           System.out.println("Количество строк: " + lines.size());
        } catch (IOException ex) {
            System.out.println("Ошибка при чтении файла");
        }
    }

    private static List<String> readFile(Path path) throws IOException {
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }
}