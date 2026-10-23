package constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


 //Матрица поедания
public final class Constants {
    private Constants() {
    }

    public static final String PLANT = "Plant";

    //строки матрицы (кто ест)
    private static final Map<String, Integer> ROWS = Map.ofEntries(
            Map.entry("Wolf", 0),
            Map.entry("Snake", 1),
            Map.entry("Fox", 2),
            Map.entry("Bear", 3),
            Map.entry("Eagle", 4),
            Map.entry("Horse", 5),
            Map.entry("Deer", 6),
            Map.entry("Rabbit", 7),
            Map.entry("Mouse", 8),
            Map.entry("Goat", 9),
            Map.entry("Sheep", 10),
            Map.entry("Boar", 11),
            Map.entry("Buffalo", 12),
            Map.entry("Duck", 13),
            Map.entry("Caterpillar", 14)
    );

    //столбцы матрицы (кого едят)
    private static final String[] COLUMNS = {
            "Wolf", "Snake", "Fox", "Bear", "Eagle", "Horse", "Deer", "Rabbit",
            "Mouse", "Goat", "Sheep", "Boar", "Buffalo", "Duck", "Caterpillar", PLANT
    };

    private static final int[][] CONSUMPTION_MATRIX = new int[][]{
            {0, 0, 0, 0, 0, 10, 15, 60, 80, 60, 70, 15, 10, 40, 0, 0},  //Волк
            {0, 0, 15, 0, 0, 0, 0, 20, 40, 0, 0, 0, 0, 10, 0, 0},       //Змея
            {0, 0, 0, 0, 0, 0, 0, 70, 90, 0, 0, 0, 0, 60, 40, 0},       //Лиса
            {0, 80, 0, 0, 0, 40, 80, 80, 90, 70, 70, 50, 20, 10, 0, 0}, //Медведь
            {0, 0, 10, 0, 0, 0, 0, 90, 90, 0, 0, 0, 0, 80, 0, 0},       //Орел
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100},         //Лошадь
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100},         //Олень
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100},         //Кролик
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 90, 100},        //Мышь
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100},         //Коза
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100},         //Овца
            {0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 0, 0, 0, 0, 90, 100},       //Кабан
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100},         //Буйвол
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 90, 100},        //Утка
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100},         //Гусеница
    };

    // Шанс (0..100)
    public static int chance(String eater, String food) {
        Integer row = ROWS.get(eater);
        if (row == null) {
            return 0;
        }
        for (int col = 0; col < COLUMNS.length; col++) {
            if (COLUMNS[col].equals(food)) {
                return CONSUMPTION_MATRIX[row][col];
            }
        }
        return 0;
    }

    // Бросок кубика по матрице
    public static boolean tryEat(String eater, String food) {
        int chance = chance(eater, food);
        return chance > 0 && ThreadLocalRandom.current().nextInt(100) < chance;
    }

    // Список названий всего, что данный вид вообще может есть
    public static List<String> menuOf(String eater) {
        List<String> menu = new ArrayList<>();
        for (String food : COLUMNS) {
            if (chance(eater, food) > 0) {
                menu.add(food);
            }
        }
        Collections.shuffle(menu, ThreadLocalRandom.current());
        return menu;
    }
}
