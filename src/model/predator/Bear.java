package model.predator;

import model.Predator;

public class Bear extends Predator {
    public Bear() {
        // имя, эмодзи, вес (кг), макс. на клетке, скорость (клеток/ход), еда для насыщения (кг)
        super("Bear", "\uD83D\uDC3B", 500.0, 5, 2, 80.0);
    }

    @Override
    protected Bear createChild() {
        return new Bear();
    }
}
