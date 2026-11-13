package ru.yandex.practicum.sleeptracker;

public enum SleepQuality {
    GOOD,
    NORMAL,
    BAD,
    UNKNOWN;

    public static SleepQuality fromString(String value) {
        switch (value.toUpperCase()) {
            case "GOOD":
                return GOOD;
            case "NORMAL":
                return NORMAL;
            case "BAD":
                return BAD;
            default:
                return UNKNOWN;
        }
    }
}
