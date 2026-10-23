package model;

import model.land.Location;

//Любой живой объект острова
public abstract class Organism {
    protected final String name;
    protected final String emoji;
    protected final double weight;
    protected volatile boolean alive = true;
    protected volatile Location location;

    protected Organism(String name, String emoji, double weight) {
        this.name = name;
        this.emoji = emoji;
        this.weight = weight;
    }

    public String getName() {

        return name;
    }

    public String getEmoji() {

        return emoji;
    }

    public double getWeight() {

        return weight;
    }

    public boolean isAlive() {
        return alive;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
