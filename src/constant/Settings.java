package constant;

import java.util.LinkedHashMap;
import java.util.Map;


public final class Settings {
    private Settings() {
    }

    //размер острова
    public static final int WIDTH = Integer.getInteger("island.width", 2);
    public static final int HEIGHT = Integer.getInteger("island.height", 1);

    //длительность такта симуляции, мс
    public static final long TICK_MILLIS = Long.getLong("island.tickMillis", 1000L);

    //условие остановки: максимальное число тактов (0 = без ограничения).
    //Симуляция в любом случае останавливается, когда умерли все животные.
    public static final int MAX_TICKS = Integer.getInteger("island.maxTicks", 100);

    //размер пула потоков, обрабатывающего локации внутри такта
    public static final int WORKER_THREADS = Runtime.getRuntime().availableProcessors();

    //растения
    public static final int PLANT_MAX_PER_CELL = 50;
    public static final int PLANT_START_PER_CELL = 10;
    public static final int PLANT_GROWTH_PER_TICK = 8;
    public static final double PLANT_WEIGHT = 1.0;

    //какую долю от нормы еды животное теряет за один такт
    public static final double HUNGER_PER_TICK = 0.1;
    //доля сытости, с которой стартуют (и рождаются) животные
    public static final double START_SATIETY = 0.5;
    //размножаться могут только животные, сытые не меньше этой доли
    public static final double MIN_SATIETY_TO_BREED = 0.5;

    // Параметры вида: сколько на старте, сколько детёнышей за раз, шанс размножения пары за такт
    public record SpeciesParams(int startCount, int offspring, double breedChance) {
    }

    private static final Map<String, SpeciesParams> SPECIES = new LinkedHashMap<>();

    static {
        //хищники
        SPECIES.put("Wolf", new SpeciesParams(3, 1, 0.12));
        SPECIES.put("Snake", new SpeciesParams(4, 1, 0.12));
        SPECIES.put("Fox", new SpeciesParams(3, 1, 0.12));
        SPECIES.put("Bear", new SpeciesParams(1, 1, 0.08));
        SPECIES.put("Eagle", new SpeciesParams(2, 1, 0.12));
        //травоядные
        SPECIES.put("Horse", new SpeciesParams(4, 1, 0.08));
        SPECIES.put("Deer", new SpeciesParams(4, 1, 0.08));
        SPECIES.put("Rabbit", new SpeciesParams(3, 2, 0.12));
        SPECIES.put("Mouse", new SpeciesParams(5, 2, 0.12));
        SPECIES.put("Goat", new SpeciesParams(6, 1, 0.10));
        SPECIES.put("Sheep", new SpeciesParams(6, 1, 0.10));
        SPECIES.put("Boar", new SpeciesParams(3, 2, 0.10));
        SPECIES.put("Buffalo", new SpeciesParams(2, 1, 0.06));
        SPECIES.put("Duck", new SpeciesParams(1, 2, 0.12));
        SPECIES.put("Caterpillar", new SpeciesParams(3, 2, 0.06));
    }

    public static SpeciesParams params(String species) {
        return SPECIES.get(species);
    }
}
