package functions;

import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import java.time.Duration;

public class AverageSleepDuration {
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sessions) {
        int sumDurations = sessions.stream()
                .mapToInt(session -> (int) Duration.between(session.getStart(), session.getFinish()).toMinutes())
                .sum();
        return new SleepAnalysisResult<>(sumDurations / sessions.size(), "Средняя продолжительность сна");
    }
}
