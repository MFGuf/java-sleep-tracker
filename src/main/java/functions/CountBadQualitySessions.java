package functions;

import ru.yandex.practicum.sleeptracker.SleepQuality;
import ru.yandex.practicum.sleeptracker.SleepingSession;
import java.util.List;

public class CountBadQualitySessions implements Function {
    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long badQualityCount = sessions.stream()
                .filter(session -> session.getQuality() == SleepQuality.BAD)
                .count();
        return new SleepAnalysisResult<>(badQualityCount, "Количество сессий с плохим качеством сна");
    }
}