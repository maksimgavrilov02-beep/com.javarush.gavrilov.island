package model.predator;

import model.Predator;

public class Wolf extends Predator {
    public Wolf() {
        // имя, эмодзи, вес (кг), макс. на клетке, скорость (клеток/ход), еда для насыщения (кг)
        super("Wolf", "\uD83D\uDC3A", 50.0, 30, 3, 8.0);
    }

    @Override
    protected Wolf createChild() {
        return new Wolf();
    }
}
