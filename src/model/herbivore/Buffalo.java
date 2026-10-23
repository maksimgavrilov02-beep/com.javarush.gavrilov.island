package model.herbivore;

import model.Herbivore;

public class Buffalo extends Herbivore {
    public Buffalo() {
        // имя, эмодзи, вес (кг), макс. на клетке, скорость (клеток/ход), еда для насыщения (кг)
        super("Buffalo", "\uD83D\uDC03", 700.0, 10, 3, 100.0);
    }

    @Override
    protected Buffalo createChild() {
        return new Buffalo();
    }
}
