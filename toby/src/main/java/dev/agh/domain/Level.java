package dev.agh.domain;

public enum Level {
    BASIC(1),SLIVER(2),GOLD(3);
    private final int value;

    Level(int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }

    public static Level valueOf(int value) {
        switch (value) {
            case 1: return BASIC;
            case 2: return SLIVER;
            case 3: return GOLD;
            default:
                throw new AssertionError("Unknown value : " + value);
        }
    }
}
