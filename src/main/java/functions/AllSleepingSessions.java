package functions;

import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class AllSleepingSessions implements Function {
    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sessions) {
        return new SleepAnalysisResult<>(sessions.size(), "Общее количество сессий сна");
    }
}

