package model.herbivore;

import model.Herbivore;

public class Mouse extends Herbivore {
    public Mouse() {
        // имя, эмодзи, вес (кг), макс. на клетке, скорость (клеток/ход), еда для насыщения (кг)
        super("Mouse", "\uD83D\uDC01", 0.05, 500, 1, 0.01);
    }

    @Override
    protected Mouse createChild() {
        return new Mouse();
    }
}
