package model.predator;

import model.Predator;

public class Eagle extends Predator {
    public Eagle() {
        // имя, эмодзи, вес (кг), макс. на клетке, скорость (клеток/ход), еда для насыщения (кг)
        super("Eagle", "\uD83E\uDD85", 6.0, 20, 3, 1.0);
    }

    @Override
    protected Eagle createChild() {
        return new Eagle();
    }
}
