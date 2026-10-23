package model.herbivore;

import model.Herbivore;

public class Rabbit extends Herbivore {
    public Rabbit() {
        // имя, эмодзи, вес (кг), макс. на клетке, скорость (клеток/ход), еда для насыщения (кг)
        super("Rabbit", "\uD83D\uDC07", 2.0, 150, 2, 0.45);
    }

    @Override
    protected Rabbit createChild() {
        return new Rabbit();
    }
}
