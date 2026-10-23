package model.herbivore;

import model.Herbivore;

public class Boar extends Herbivore {
    public Boar() {
        // имя, эмодзи, вес (кг), макс. на клетке, скорость (клеток/ход), еда для насыщения (кг)
        super("Boar", "\uD83D\uDC17", 400.0, 50, 2, 50.0);
    }

    @Override
    protected Boar createChild() {
        return new Boar();
    }
}
