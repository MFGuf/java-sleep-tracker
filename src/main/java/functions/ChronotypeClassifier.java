package functions;

import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChronotypeClassifier implements Function {
    private enum NightType { OWL, LARK, PIGEON }

    @Override
    public SleepAnalysisResult<String> apply(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Голубь", "Хронотип пользователя");
        }

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
            chronotype = "Голубь";
        }

        return new SleepAnalysisResult<>(chronotype, "Хронотип пользователя");
    }

    private boolean isNightSession(SleepingSession session) {
        LocalDate startDate = session.getStart().toLocalDate();
        LocalDate finishDate = session.getFinish().toLocalDate();

        return overlapsNightWindow(session, startDate)
                || overlapsNightWindow(session, finishDate);
    }

    private boolean overlapsNightWindow(SleepingSession session, LocalDate date) {
        LocalDateTime windowStart = date.atStartOfDay();
        LocalDateTime windowEnd = date.atTime(6, 0);
        return session.getStart().isBefore(windowEnd)
                && session.getFinish().isAfter(windowStart);
    }

    private NightType classifyNight(SleepingSession session) {
        LocalTime fallAsleep = session.getStart().toLocalTime();
        LocalTime wakeUp = session.getFinish().toLocalTime();

        boolean lateFallAsleep = fallAsleep.isAfter(LocalTime.of(23, 0))
                || fallAsleep.isBefore(LocalTime.of(6, 0));
        boolean isOwl = lateFallAsleep && wakeUp.isAfter(LocalTime.of(9, 0));

        boolean eveningFallAsleep = fallAsleep.isAfter(LocalTime.of(6, 0))
                && fallAsleep.isBefore(LocalTime.of(22, 0));
        boolean isLark = eveningFallAsleep && wakeUp.isBefore(LocalTime.of(7, 0));

        if (isOwl) return NightType.OWL;
        if (isLark) return NightType.LARK;
        return NightType.PIGEON;
    }
}
