package ru.yandex.practicum.sleeptracker;

import functions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SleepTrackerApp {
    private static final List<Function> FUNCTIONS = Arrays.asList(
            new AllSleepingSessions(),
            new MinimalSleepDuration(),
            new MaximalSleepDuration(),
            new AverageSleepDuration(),
            new CountBadQualitySessions(),
            new SleeplessNightsCounter(),
            new ChronotypeClassifier()
    );

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
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            List<SleepingSession> sessions = parseSession(lines);

            System.out.println("Файл успешно загружен и преобразован в сессии сна.");
            System.out.println("=== Результаты анализа ===");

            FUNCTIONS.stream()
                    .map(func -> func.apply(sessions))
                    .forEach(System.out::println);

        } catch (IOException ex) {
            System.out.println("Ошибка при чтении файла");
        } catch (Exception ex) {
            System.out.println("Произошла ошибка при анализе данных");
        }
    }

    private static List<SleepingSession> parseSession(List<String> lines) {
        List<SleepingSession> sessions = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        for (String line : lines) {
            String[] parts = line.split(";");
            if (parts.length < 3) continue;

            LocalDateTime start = LocalDateTime.parse(parts[0].trim(), formatter);
            LocalDateTime finish = LocalDateTime.parse(parts[1].trim(), formatter);
            SleepQuality quality = SleepQuality.fromString(parts[2].trim());

            sessions.add(new SleepingSession(start, finish, quality));
        }
        return sessions;
    }
}