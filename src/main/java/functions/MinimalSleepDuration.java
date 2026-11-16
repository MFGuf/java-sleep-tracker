package functions;

import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.List;

public class MinimalSleepDuration implements Function {
    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long minimalDuration = sessions.stream()
                .mapToLong(session -> Duration.between(session.getStart(), session.getFinish()).toMinutes())
                .min()
                .orElse(0L);
        return new SleepAnalysisResult<>(minimalDuration, "Минимальная длительность сна");
    }
}
