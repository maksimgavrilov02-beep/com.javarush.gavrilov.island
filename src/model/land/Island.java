package model.land;

import constant.Settings;
import model.Animal;

import java.util.ArrayList;
import java.util.List;

public class Island {
    private final int width;
    private final int height;
    private final List<Location> locations = new ArrayList<>();

    public Island() {
        this.width = Settings.WIDTH;
        this.height = Settings.HEIGHT;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                locations.add(new Location(x, y, y * width + x, this));
            }
        }
    }

    public List<Location> getLocations() {
        return locations;
    }

    // Локация по координатам или null, если координаты за пределами острова.
    public Location getLocation(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        return locations.get(y * width + x);
    }


     //Перемещает животное между локациями.
    public boolean moveAnimal(Animal animal, Location from, Location to) {
        Location first = from.getIndex() < to.getIndex() ? from : to;
        Location second = first == from ? to : from;
        first.lock();
        second.lock();
        try {
            if (!animal.isAlive() || animal.getLocation() != from
                    || to.count(animal.getName()) >= animal.getMaxPerCell()) {
                return false;
            }
            from.removeAnimal(animal);
            to.addAnimal(animal);
            return true;
        } finally {
            second.unlock();
            first.unlock();
        }
    }
}
