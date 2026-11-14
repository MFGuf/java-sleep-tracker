package functions;

import ru.yandex.practicum.sleeptracker.SleepQuality;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class CountBADQualuty implements Function {
    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long badQuality = sessions.stream()
                .filter(session -> session.getQuality() == SleepQuality.BAD)
                .count();
        return new SleepAnalysisResult<>(badQuality, "Количестко снов с плохим качеством сна");
    }
}
