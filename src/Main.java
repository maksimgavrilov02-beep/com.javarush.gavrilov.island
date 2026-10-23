import constant.Settings;
import simulation.Simulation;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // чтобы эмодзи нормально печатались в консоли
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        System.out.printf("Остров %dx%d, такт %d мс, лимит тактов: %s%n",
                Settings.WIDTH, Settings.HEIGHT, Settings.TICK_MILLIS,
                Settings.MAX_TICKS > 0 ? Settings.MAX_TICKS : "без ограничения");
        new Simulation().run();
    }
}
