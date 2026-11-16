package functions;

import ru.yandex.practicum.sleeptracker.SleepingSession;
import java.time.Duration;
import java.util.List;

public class AverageSleepDuration implements Function {
    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long totalMinutes = sessions.stream()
                .mapToLong(s -> Duration.between(s.getStart(), s.getFinish()).toMinutes())
                .sum();

        long average = totalMinutes / sessions.size();
        return new SleepAnalysisResult<>(average, "Средняя продолжительность сна (мин)");
    }
}