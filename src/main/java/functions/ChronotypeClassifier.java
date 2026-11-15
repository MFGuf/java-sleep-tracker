package functions;

import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChronotypeClassifier implements Function {
    private enum NightType { OWL, LARK, PIGEON }

    @Override
    public SleepAnalysisResult<String> apply(List<SleepingSession> sessions) {
        Map<NightType, Long> counts = sessions.stream()
                .filter(this::isNightSession)
                .map(this::classifyNight)
                .collect(Collectors.groupingBy(t -> t, Collectors.counting()));

        long owls = counts.getOrDefault(NightType.OWL, 0L);
        long larks = counts.getOrDefault(NightType.LARK, 0L);
        long pigeons = counts.getOrDefault(NightType.PIGEON, 0L);

        String chronotype;
        if (owls > larks && owls > pigeons) {
            chronotype = "Сова";
        } else if (larks > owls && larks > pigeons) {
            chronotype = "Жаворонок";
        } else {
            // По ТЗ: при сомнениях или равенстве — Голубь
            chronotype = "Голубь";
        }

        return new SleepAnalysisResult<>(chronotype, "Хронотип пользователя");
    }

    private boolean isNightSession(SleepingSession session) {
        LocalTime startTime = session.getStart().toLocalTime();
        LocalTime finishTime = session.getFinish().toLocalTime();

        return startTime.isBefore(LocalTime.of(6, 0))
                || finishTime.isAfter(LocalTime.MIDNIGHT);
    }

    private NightType classifyNight(SleepingSession session) {
        LocalTime fallAsleep = session.getStart().toLocalTime();
        LocalTime wakeUp = session.getFinish().toLocalTime();

        boolean isOwl = fallAsleep.isAfter(LocalTime.of(23, 0))
                && wakeUp.isAfter(LocalTime.of(9, 0));
        boolean isLark = fallAsleep.isBefore(LocalTime.of(22, 0))
                && wakeUp.isBefore(LocalTime.of(7, 0));

        if (isOwl) return NightType.OWL;
        if (isLark) return NightType.LARK;
        return NightType.PIGEON;
    }
}