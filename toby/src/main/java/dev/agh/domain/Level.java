package dev.agh.domain;

public enum Level {
    GOLD(3,null),SLIVER(2,GOLD),BASIC(1,SLIVER);
    private final int value;
    private final Level next;

    Level(int value,Level next) {
        this.value = value;
        this.next = next;
    }

    public Level nextLevel() {
        return this.next;
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
