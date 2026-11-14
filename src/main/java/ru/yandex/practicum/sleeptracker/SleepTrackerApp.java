package ru.yandex.practicum.sleeptracker;

import functions.AllSleepingSessions;
import functions.Function;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class SleepTrackerApp {
    private final static List<Function> functions = new ArrayList<>();

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
            List<SleepingSession> sessions = parseSession(lines);
            System.out.println("Файл успешно преобразован в сессии сна");
        } catch (IOException ex) {
            System.out.println("Ошибка при чтении файла");
        } catch (DateTimeException ex) {
            System.out.println("Произошла ошибка при преобразовании файла в сессии сна");
        }
        functions.add(new AllSleepingSessions());
    }

    private static List<String> readFile(Path path) throws IOException {
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }

    private static List<SleepingSession> parseSession(List<String> lines) {
        List<SleepingSession> sessions = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        for (String line : lines) {
            String[] parts = line.split(";");

            if (parts.length < 3) {
                continue;
            }

            LocalDateTime start = LocalDateTime.parse(parts[0], formatter);
            LocalDateTime finish = LocalDateTime.parse(parts[1], formatter);
            SleepQuality quality = SleepQuality.fromString(parts[2]);

            sessions.add(new SleepingSession(start, finish, quality));
        }

        return sessions;
    }
}