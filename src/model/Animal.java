package model;

import constant.Constants;
import constant.Settings;
import model.land.Island;
import model.land.Location;
import simulation.Statistics;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

// Общее поведение всех животных: покушать, размножиться, выбрать направление, передвинуться
public abstract class Animal extends Organism {
    private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    private static final int MATE_SEARCH_ATTEMPTS = 6;   //попытки поиска пары

    protected final int maxPerCell;     // максимум особей вида в одной локации
    protected final int speed;          // сколько клеток максимум за ход
    protected final double foodNeeded;  // сколько кг еды нужно для полного насыщения
    protected double satiety;           // текущая сытость, кг

    // служебные поля, которыми управляет Location
    public int slot;                    // позиция в списке вида внутри локации
    private volatile boolean moved;     // уже ходил в этом такте
    private boolean bred;               // уже размножался в этом такте

    protected Animal(String name, String emoji, double weight, int maxPerCell, int speed, double foodNeeded) {
        super(name, emoji, weight);
        this.maxPerCell = maxPerCell;
        this.speed = speed;
        this.foodNeeded = foodNeeded;
        this.satiety = foodNeeded * Settings.START_SATIETY;
    }

    // Создаёт детёныша того же вида.
    protected abstract Animal createChild();

    public int getMaxPerCell() {
        return maxPerCell;
    }

    public void beginTick() {
        moved = false;
        bred = false;
    }

    // ---------- еда ----------

    // Порядок, в котором животное пробует разные виды пищи.
    protected List<String> menu() {
        return Constants.menuOf(name);
    }

    public void eat() {
        Location loc = location;
        if (loc == null || foodNeeded <= 0) {
            return;
        }
        for (String food : menu()) {
            if (satiety >= foodNeeded) {
                return;
            }
            if (Constants.PLANT.equals(food)) {
                eatPlants(loc);
            } else {
                eatAnimals(loc, food);
            }
        }
    }

    private void eatPlants(Location loc) {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        // растение весит PLANT_WEIGHT кг; маленькому животному нужно меньше, поэтому оно "откусывает":
        // растение исчезает с вероятностью (съеденная доля), так расход травы честно соответствует аппетиту
        int attempts = (int) Math.ceil((foodNeeded - satiety) / Settings.PLANT_WEIGHT);
        for (int i = 0; i < attempts && satiety < foodNeeded && loc.plantCount() > 0; i++) {
            if (Constants.tryEat(name, Constants.PLANT)) {
                double bite = Math.min(foodNeeded - satiety, Settings.PLANT_WEIGHT);
                if (rnd.nextDouble() < bite / Settings.PLANT_WEIGHT) {
                    Plant plant = loc.takePlant();
                    if (plant == null) {
                        return;
                    }
                    plant.alive = false;
                }
                gainFood(bite);
            }
        }
    }

    private void eatAnimals(Location loc, String preyName) {
        List<Animal> prey = loc.speciesList(preyName);
        // идём с конца: удаление "своп с последним" не пропускает ещё не просмотренных
        for (int i = prey.size() - 1; i >= 0 && satiety < foodNeeded; i--) {
            if (i >= prey.size()) {
                continue;
            }
            Animal victim = prey.get(i);
            if (victim != this && Constants.tryEat(name, preyName)) {
                victim.die(false);
                gainFood(victim.getWeight());
                Statistics.eaten();
            }
        }
    }

    private void gainFood(double kg) {
        satiety = Math.min(foodNeeded, satiety + kg);
    }

    // ---------- размножение ----------

    private boolean canBreed() {
        return alive && !bred && satiety >= foodNeeded * Settings.MIN_SATIETY_TO_BREED;
    }

    public void reproduce() {
        Location loc = location;
        if (loc == null || !canBreed()) {
            return;
        }
        Animal mate = findMate(loc);
        Settings.SpeciesParams params = Settings.params(name);
        if (mate == null || ThreadLocalRandom.current().nextDouble() >= params.breedChance()) {
            return;
        }
        this.bred = true;
        mate.bred = true;
        int room = maxPerCell - loc.count(name);
        for (int i = 0; i < Math.min(params.offspring(), room); i++) {
            Animal child = createChild();
            child.bred = true; // детёныш в этот такт уже не участвует
            loc.addAnimal(child);
            Statistics.born();
        }
    }

    private Animal findMate(Location loc) {
        List<Animal> same = loc.speciesList(name);
        if (same.size() < 2) {
            return null;
        }
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = 0; i < MATE_SEARCH_ATTEMPTS; i++) {
            Animal candidate = same.get(rnd.nextInt(same.size()));
            if (candidate != this && candidate.canBreed()) {
                return candidate;
            }
        }
        return null;
    }

    // ---------- передвижение ----------

    // Выбирает направление шага: {dx, dy}. Виды могут переопределить.
    public int[] chooseDirection() {
        return DIRECTIONS[ThreadLocalRandom.current().nextInt(DIRECTIONS.length)];
    }

    public void move() {
        Location from = location;
        if (!alive || moved || speed <= 0 || from == null) {
            return;
        }
        Island island = from.getIsland();
        Location target = from;
        int steps = ThreadLocalRandom.current().nextInt(speed + 1);
        for (int s = 0; s < steps; s++) {
            int[] dir = chooseDirection();
            Location next = island.getLocation(target.getX() + dir[0], target.getY() + dir[1]);
            if (next != null) {
                target = next;
            }
        }
        if (target != from && island.moveAnimal(this, from, target)) {
            moved = true;
        }
    }

    // голод
    public void hungerStep() {
        if (!alive || foodNeeded <= 0) {
            return;
        }
        satiety -= foodNeeded * Settings.HUNGER_PER_TICK;
        if (satiety <= 0) {
            die(true);
        }
    }

    // Смерть: животное убирается с острова.
    public void die(boolean starved) {
        alive = false;
        Location loc = location;
        if (loc != null) {
            loc.removeAnimal(this);
        }
        if (starved) {
            Statistics.starved();
        }
    }
}
