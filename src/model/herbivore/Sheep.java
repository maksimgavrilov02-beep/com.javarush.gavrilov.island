package model.herbivore;

import model.Herbivore;

public class Sheep extends Herbivore {
    public Sheep() {
        // имя, эмодзи, вес (кг), макс. на клетке, скорость (клеток/ход), еда для насыщения (кг)
        super("Sheep", "\uD83D\uDC11", 70.0, 140, 3, 15.0);
    }

    @Override
    protected Sheep createChild() {
        return new Sheep();
    }
}
