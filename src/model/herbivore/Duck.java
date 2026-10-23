package model.herbivore;

import model.Herbivore;

public class Duck extends Herbivore {
    public Duck() {
        // имя, эмодзи, вес (кг), макс. на клетке, скорость (клеток/ход), еда для насыщения (кг)
        super("Duck", "\uD83E\uDD86", 1.0, 200, 4, 0.15);
    }

    @Override
    protected Duck createChild() {
        return new Duck();
    }
}
