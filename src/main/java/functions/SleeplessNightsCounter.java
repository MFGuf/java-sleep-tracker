package functions;

import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class SleeplessNightsCounter implements Function {
    private static final LocalTime NIGHT_WINDOW_END = LocalTime.of(6, 0);

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        LocalDate firstDate = sessions.get(0).getStart().toLocalDate();
        LocalDate lastDate = sessions.get(sessions.size() - 1).getFinish().toLocalDate();

        LocalTime firstTime = sessions.get(0).getStart().toLocalTime();
        LocalDate periodStart = firstTime.isAfter(LocalTime.NOON)
                ? firstDate.plusDays(1)
                : firstDate;

        long totalNights = Period.between(periodStart, lastDate).getDays();

        List<LocalDate> allNights = LongStream.range(0, Math.max(totalNights, 0))
                .mapToObj(periodStart::plusDays)
                .collect(Collectors.toList());

        long sleeplessCount = allNights.stream()
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