package model.predator;

import model.Predator;

public class Snake extends Predator {
    public Snake() {
        // имя, эмодзи, вес (кг), макс. на клетке, скорость (клеток/ход), еда для насыщения (кг)
        super("Snake", "\uD83D\uDC0D", 15.0, 30, 1, 3.0);
    }

    @Override
    protected Snake createChild() {
        return new Snake();
    }
}
