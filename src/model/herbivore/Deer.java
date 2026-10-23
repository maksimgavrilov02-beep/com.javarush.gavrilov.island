package model.herbivore;

import model.Herbivore;

public class Deer extends Herbivore {
    public Deer() {
        // имя, эмодзи, вес (кг), макс. на клетке, скорость (клеток/ход), еда для насыщения (кг)
        super("Deer", "\uD83E\uDD8C", 300.0, 20, 4, 50.0);
    }

    @Override
    protected Deer createChild() {
        return new Deer();
    }
}
