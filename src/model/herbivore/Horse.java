package model.herbivore;

import model.Herbivore;

public class Horse extends Herbivore {
    public Horse() {
        // имя, эмодзи, вес (кг), макс. на клетке, скорость (клеток/ход), еда для насыщения (кг)
        super("Horse", "\uD83D\uDC0E", 400.0, 20, 4, 60.0);
    }

    @Override
    protected Horse createChild() {
        return new Horse();
    }
}
