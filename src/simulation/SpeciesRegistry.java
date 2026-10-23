package simulation;

import model.Animal;
import model.herbivore.*;
import model.predator.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

// Список всех видов животных
public final class SpeciesRegistry {
    private SpeciesRegistry() {
    }

    public static final Map<String, Supplier<? extends Animal>> ALL = new LinkedHashMap<>();

    static {
        ALL.put("Wolf", Wolf::new);
        ALL.put("Snake", Snake::new);
        ALL.put("Fox", Fox::new);
        ALL.put("Bear", Bear::new);
        ALL.put("Eagle", Eagle::new);
        ALL.put("Horse", Horse::new);
        ALL.put("Deer", Deer::new);
        ALL.put("Rabbit", Rabbit::new);
        ALL.put("Mouse", Mouse::new);
        ALL.put("Goat", Goat::new);
        ALL.put("Sheep", Sheep::new);
        ALL.put("Boar", Boar::new);
        ALL.put("Buffalo", Buffalo::new);
        ALL.put("Duck", Duck::new);
        ALL.put("Caterpillar", Caterpillar::new);
    }
}
