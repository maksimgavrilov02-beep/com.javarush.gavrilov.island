package model.predator;

import model.Predator;

public class Fox extends Predator {
    public Fox() {
        // имя, эмодзи, вес (кг), макс. на клетке, скорость (клеток/ход), еда для насыщения (кг)
        super("Fox", "\uD83E\uDD8A", 8.0, 30, 2, 2.0);
    }

    @Override
    protected Fox createChild() {
        return new Fox();
    }
}
