package functions;

import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.LongStream;

public class SleeplessNightsCounter implements Function {
    private static final LocalTime NIGHT_WINDOW_END = LocalTime.of(6, 0);

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult<>(0L, "Количество бессонных ночей");
        }

        LocalDateTime firstStart = sessions.get(0).getStart();
        LocalDateTime lastFinish = sessions.get(sessions.size() - 1).getFinish();

        // Если первая сессия началась после 12:00, первая потенциальная ночь — следующая; иначе — текущая
        LocalDate periodStart = firstStart.toLocalTime().isAfter(LocalTime.NOON)
                ? firstStart.toLocalDate().plusDays(1)
                : firstStart.toLocalDate();

        // Если последняя сессия закончилась ровно в 00:00, ночь этого дня ещё не началась
        LocalDate periodEnd = lastFinish.toLocalTime().equals(LocalTime.MIDNIGHT)
                ? lastFinish.toLocalDate().minusDays(1)
                : lastFinish.toLocalDate();

        long totalNights = ChronoUnit.DAYS.between(periodStart, periodEnd) + 1;

        long sleeplessCount = LongStream.range(0, Math.max(totalNights, 0))
                .mapToObj(periodStart::plusDays)
                .filter(nightDate -> !hasNightSleep(sessions, nightDate))
                .count();

        return new SleepAnalysisResult<>(sleeplessCount, "Количество бессонных ночей");
    }

    private boolean hasNightSleep(List<SleepingSession> sessions, LocalDate nightDate) {
        LocalDateTime windowStart = nightDate.atStartOfDay();
        LocalDateTime windowEnd = nightDate.atTime(NIGHT_WINDOW_END);

        return sessions.stream()
                .anyMatch(session -> session.getStart().isBefore(windowEnd)
                        && session.getFinish().isAfter(windowStart));
    }
}
