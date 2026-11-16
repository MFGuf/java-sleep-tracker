package functions;

import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public interface Function {
    SleepAnalysisResult<?> apply(List<SleepingSession> session);
}
