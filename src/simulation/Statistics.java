package simulation;

import java.util.concurrent.atomic.AtomicInteger;

// Счётчики событий за текущий такт
public final class Statistics {
    private Statistics() {
    }

    private static final AtomicInteger BORN = new AtomicInteger();
    private static final AtomicInteger EATEN = new AtomicInteger();
    private static final AtomicInteger STARVED = new AtomicInteger();

    public static void born() {
        BORN.incrementAndGet();
    }

    public static void eaten() {
        EATEN.incrementAndGet();
    }

    public static void starved() {
        STARVED.incrementAndGet();
    }

    public static void reset() {
        BORN.set(0);
        EATEN.set(0);
        STARVED.set(0);
    }

    public static int getBorn() {
        return BORN.get();
    }

    public static int getEaten() {
        return EATEN.get();
    }

    public static int getStarved() {
        return STARVED.get();
    }
}
