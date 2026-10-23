package model;


 //Хищник. Что именно он ест, определяет матрица поедания

public abstract class Predator extends Animal {
    protected Predator(String name, String emoji, double weight, int maxPerCell, int speed, double foodNeeded) {
        super(name, emoji, weight, maxPerCell, speed, foodNeeded);
    }
}
