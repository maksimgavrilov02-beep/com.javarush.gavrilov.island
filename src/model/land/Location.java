package model.land;

import model.Animal;
import model.Plant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Клетка острова. Животные хранятся по видам; удаление за O(1) (своп с последним элементом).
 * Внутри одной фазы такта локацию обрабатывает один поток; lock() нужен только там,
 * где животные переходят между локациями (см. Island.moveAnimal).
 */
public class Location {
    private final int x;
    private final int y;
    private final int index;
    private final Island island;
    private final ReentrantLock lock = new ReentrantLock();

    private final Map<String, List<Animal>> animals = new HashMap<>();
    private final List<Plant> plants = new ArrayList<>();

    public Location(int x, int y, int index, Island island) {
        this.x = x;
        this.y = y;
        this.index = index;
        this.island = island;
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    // ---------- животные ----------

    public void addAnimal(Animal animal) {
        List<Animal> list = animals.computeIfAbsent(animal.getName(), k -> new ArrayList<>());
        animal.slot = list.size();
        list.add(animal);
        animal.setLocation(this);
    }

    public void removeAnimal(Animal animal) {
        List<Animal> list = animals.get(animal.getName());
        if (list == null || animal.slot >= list.size() || list.get(animal.slot) != animal) {
            return;
        }
        Animal last = list.remove(list.size() - 1);
        if (last != animal) {
            list.set(animal.slot, last);
            last.slot = animal.slot;
        }
    }

    public List<Animal> speciesList(String species) {
        return animals.getOrDefault(species, List.of());
    }

    public int count(String species) {
        return speciesList(species).size();
    }

    public int animalCount() {
        int sum = 0;
        for (List<Animal> list : animals.values()) {
            sum += list.size();
        }
        return sum;
    }

    // Копия всех животных
    public List<Animal> snapshotAnimals() {
        List<Animal> copy = new ArrayList<>(animalCount());
        animals.values().forEach(copy::addAll);
        return copy;
    }

    // ---------- растения ----------

    public void addPlant(Plant plant) {
        plants.add(plant);
        plant.setLocation(this);
    }

    public Plant takePlant() {
        return plants.isEmpty() ? null : plants.remove(plants.size() - 1);
    }

    public int plantCount() {
        return plants.size();
    }

    // Фаза 1: покушать, размножиться, проголодаться. Затрагивает только эту локацию
    public void lifeCycle() {
        List<Animal> snapshot = snapshotAnimals();
        snapshot.forEach(Animal::beginTick);
        for (Animal animal : snapshot) {
            if (!animal.isAlive()) {
                continue; // его уже съели в этом такте
            }
            animal.eat();
            animal.reproduce();
            animal.hungerStep();
        }
    }

    // Фаза 2: передвижение (может затрагивать соседние локации).
    public void moveAnimals() {
        lock();
        List<Animal> snapshot;
        try {
            snapshot = snapshotAnimals();
        } finally {
            unlock();
        }
        snapshot.forEach(Animal::move);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getIndex() {
        return index;
    }

    public Island getIsland() {
        return island;
    }
}
