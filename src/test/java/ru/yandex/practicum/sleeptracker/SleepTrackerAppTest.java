package ru.yandex.practicum.sleeptracker;

import functions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SleepTrackerAppTest {

    @Test
    void allSleepingSessions_shouldReturnCorrectCount() {
        List<SleepingSession> sessions = createSessions(3);
        assertEquals(3, new AllSleepingSessions().apply(sessions).getValue());
    }

    @Test
    void allSleepingSessions_singleSession() {
        List<SleepingSession> sessions = createSessions(1);
        assertEquals(1, new AllSleepingSessions().apply(sessions).getValue());
    }

    @Test
    void minimalSleepDuration_shouldReturnShortest() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 22:00", "02.10.25 06:00", SleepQuality.GOOD),
                session("02.10.25 23:00", "03.10.25 02:00", SleepQuality.NORMAL),
                session("03.10.25 21:00", "04.10.25 07:00", SleepQuality.BAD)
        );
        assertEquals(180L, new MinimalSleepDuration().apply(sessions).getValue());
    }

    @Test
    void minimalSleepDuration_singleSession() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 22:00", "02.10.25 06:00", SleepQuality.GOOD)
        );
        assertEquals(480L, new MinimalSleepDuration().apply(sessions).getValue());
    }

    @Test
    void maximalSleepDuration_shouldReturnLongest() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 22:00", "02.10.25 06:00", SleepQuality.GOOD),
                session("02.10.25 23:00", "03.10.25 02:00", SleepQuality.NORMAL),
                session("03.10.25 21:00", "04.10.25 07:00", SleepQuality.BAD)
        );
        assertEquals(600L, new MaximalSleepDuration().apply(sessions).getValue());
    }

    @Test
    void maximalSleepDuration_singleSession() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 22:00", "02.10.25 06:00", SleepQuality.GOOD)
        );
        assertEquals(480L, new MaximalSleepDuration().apply(sessions).getValue());
    }

    @Test
    void averageSleepDuration_shouldCalculateCorrectly() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 22:00", "02.10.25 06:00", SleepQuality.GOOD),
                session("02.10.25 23:00", "03.10.25 02:00", SleepQuality.NORMAL),
                session("03.10.25 21:00", "04.10.25 07:00", SleepQuality.BAD)
        );
        assertEquals(420L, new AverageSleepDuration().apply(sessions).getValue());
    }

    @Test
    void averageSleepDuration_equalDurations() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 22:00", "02.10.25 06:00", SleepQuality.GOOD),
                session("02.10.25 22:00", "03.10.25 06:00", SleepQuality.GOOD)
        );
        assertEquals(480L, new AverageSleepDuration().apply(sessions).getValue());
    }

    @Test
    void countBadQuality_shouldCountOnlyBad() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 22:00", "02.10.25 06:00", SleepQuality.GOOD),
                session("02.10.25 23:00", "03.10.25 02:00", SleepQuality.BAD),
                session("03.10.25 21:00", "04.10.25 07:00", SleepQuality.BAD),
                session("04.10.25 22:00", "05.10.25 06:00", SleepQuality.NORMAL)
        );
        assertEquals(2L, new CountBadQualitySessions().apply(sessions).getValue());
    }

    @Test
    void countBadQuality_noBadSessions() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 22:00", "02.10.25 06:00", SleepQuality.GOOD),
                session("02.10.25 23:00", "03.10.25 02:00", SleepQuality.NORMAL)
        );
        assertEquals(0L, new CountBadQualitySessions().apply(sessions).getValue());
    }

    @Test
    void sleeplessNights_normalCaseWithMixedNights() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 23:00", "02.10.25 03:00", SleepQuality.GOOD),
                session("02.10.25 14:00", "02.10.25 16:00", SleepQuality.NORMAL),
                session("04.10.25 01:00", "04.10.25 05:00", SleepQuality.BAD)
        );
        assertEquals(1L, new SleeplessNightsCounter().apply(sessions).getValue());
    }


    @Test
    void sleeplessNights_allNightsHaveSleep() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 23:00", "02.10.25 07:00", SleepQuality.GOOD),
                session("02.10.25 22:00", "03.10.25 06:00", SleepQuality.NORMAL),
                session("03.10.25 23:30", "04.10.25 05:00", SleepQuality.BAD)
        );
        assertEquals(0L, new SleeplessNightsCounter().apply(sessions).getValue());
    }

    @Test
    void sleeplessNights_onlyDaySleepMultipleDays() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 14:00", "01.10.25 16:00", SleepQuality.NORMAL),
                session("02.10.25 13:00", "02.10.25 15:00", SleepQuality.NORMAL),
                session("03.10.25 15:00", "03.10.25 17:00", SleepQuality.NORMAL)
        );
        assertEquals(2L, new SleeplessNightsCounter().apply(sessions).getValue());
    }

    @Test
    void sleeplessNights_firstSessionAfterNoon_edgeCase() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 14:00", "01.10.25 16:00", SleepQuality.NORMAL),
                session("02.10.25 10:00", "02.10.25 12:00", SleepQuality.NORMAL)
        );
        assertEquals(1L, new SleeplessNightsCounter().apply(sessions).getValue());
    }


    @Test
    void sleeplessNights_sessionCrossesMidnight_countsForBothNights() {
                List<SleepingSession> sessions = List.of(
                session("01.10.25 23:00", "02.10.25 03:00", SleepQuality.GOOD),
                session("03.10.25 14:00", "03.10.25 16:00", SleepQuality.NORMAL)
        );
        assertEquals(1L, new SleeplessNightsCounter().apply(sessions).getValue());
    }

    @Test
    void chronotype_owlDominates() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 23:30", "02.10.25 09:30", SleepQuality.GOOD),
                session("02.10.25 23:45", "03.10.25 10:00", SleepQuality.GOOD),
                session("03.10.25 21:00", "04.10.25 06:30", SleepQuality.NORMAL)
        );
        assertEquals("Сова", new ChronotypeClassifier().apply(sessions).getValue());
    }

    @Test
    void chronotype_larkDominates() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 21:00", "02.10.25 06:00", SleepQuality.GOOD),
                session("02.10.25 21:30", "03.10.25 06:30", SleepQuality.GOOD),
                session("03.10.25 23:30", "04.10.25 09:30", SleepQuality.NORMAL)
        );
        assertEquals("Жаворонок", new ChronotypeClassifier().apply(sessions).getValue());
    }

    @Test
    void chronotype_tieReturnsPigeon() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 23:30", "02.10.25 09:30", SleepQuality.GOOD),
                session("02.10.25 21:00", "03.10.25 06:00", SleepQuality.GOOD)
        );
        assertEquals("Голубь", new ChronotypeClassifier().apply(sessions).getValue());
    }

    @Test
    void chronotype_daySessionsIgnored() {
        List<SleepingSession> sessions = List.of(
                session("01.10.25 14:00", "01.10.25 16:00", SleepQuality.NORMAL),
                session("01.10.25 23:30", "02.10.25 09:30", SleepQuality.GOOD)
        );
        assertEquals("Сова", new ChronotypeClassifier().apply(sessions).getValue());
    }

    private SleepingSession session(String start, String finish, SleepQuality quality) {
        var formatter = java.time.format.DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        return new SleepingSession(
                LocalDateTime.parse(start, formatter),
                LocalDateTime.parse(finish, formatter),
                quality
        );
    }

    private List<SleepingSession> createSessions(int count) {
        List<SleepingSession> sessions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            sessions.add(session(
                    String.format("%02d.10.25 22:00", i + 1),
                    String.format("%02d.10.25 06:00", i + 2 > 31 ? 1 : i + 2),
                    SleepQuality.GOOD
            ));
        }
        return sessions;
    }
}