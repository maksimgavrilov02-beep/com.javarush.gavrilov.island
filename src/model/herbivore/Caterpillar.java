package model.herbivore;

import model.Herbivore;

public class Caterpillar extends Herbivore {
    public Caterpillar() {
        // имя, эмодзи, вес (кг), макс. на клетке, скорость (клеток/ход), еда для насыщения (кг)
        super("Caterpillar", "\uD83D\uDC1B", 0.01, 1000, 0, 0.005); // по ТЗ еды нужно 0, но тогда гусеницы бессмертны, поэтому минимальный аппетит
    }

    @Override
    protected Caterpillar createChild() {
        return new Caterpillar();
    }
}
