package model.herbivore;

import model.Herbivore;

public class Goat extends Herbivore {
    public Goat() {
        // имя, эмодзи, вес (кг), макс. на клетке, скорость (клеток/ход), еда для насыщения (кг)
        super("Goat", "\uD83D\uDC10", 60.0, 140, 3, 10.0);
    }

    @Override
    protected Goat createChild() {
        return new Goat();
    }
}
