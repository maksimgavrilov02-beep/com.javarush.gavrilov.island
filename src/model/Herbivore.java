package model;

import constant.Constants;

import java.util.List;

 //Травоядное.

public abstract class Herbivore extends Animal {
    protected Herbivore(String name, String emoji, double weight, int maxPerCell, int speed, double foodNeeded) {
        super(name, emoji, weight, maxPerCell, speed, foodNeeded);
    }

    @Override
    protected List<String> menu() {
        List<String> menu = super.menu();
        if (menu.remove(Constants.PLANT)) {
            menu.add(0, Constants.PLANT);
        }
        return menu;
    }
}
