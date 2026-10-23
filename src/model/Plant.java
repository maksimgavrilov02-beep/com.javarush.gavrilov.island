package model;

import constant.Constants;
import constant.Settings;

public class Plant extends Organism {
    public Plant() {
        super(Constants.PLANT, "🌱", Settings.PLANT_WEIGHT);
    }
}
