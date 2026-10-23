package simulation;

import constant.Settings;
import model.Animal;
import model.Plant;
import model.land.Island;
import model.land.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;


public class Simulation {
    private final Island island = new Island();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
    private final ExecutorService workers = Executors.newFixedThreadPool(Settings.WORKER_THREADS);
    private final ReentrantLock tickLock = new ReentrantLock(true);
    private final CountDownLatch finished = new CountDownLatch(1);

    private int tick = 0;
    private int lastPrintedTick = -1;
    private volatile Throwable failure;

    public void run() throws InterruptedException {
        populate();
        long period = Settings.TICK_MILLIS;
        scheduler.scheduleAtFixedRate(guarded(this::growPlants), 0, period, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(guarded(this::lifeCycle), period / 4, period, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(guarded(this::printStatistics), period / 2, period, TimeUnit.MILLISECONDS);

        finished.await();
        shutdown();
        if (failure != null) {
            throw new IllegalStateException("Симуляция остановлена из-за ошибки", failure);
        }
    }

    // ---------- задания ----------

    // рост растений
    private void growPlants() {
        for (Location loc : island.getLocations()) {
            int toAdd = Math.min(Settings.PLANT_GROWTH_PER_TICK, Settings.PLANT_MAX_PER_CELL - loc.plantCount());
            for (int i = 0; i < toAdd; i++) {
                loc.addPlant(new Plant());
            }
        }
    }

    // жизненный цикл животных
    private void lifeCycle() {
        if (finished.getCount() == 0) {
            return;
        }
        tick++;
        Statistics.reset();
        runInPool(Location::lifeCycle);   // фаза 1: каждая локация независима
        runInPool(Location::moveAnimals); // фаза 2: перемещения между локациями
    }

    // статистика по острову
    private void printStatistics() {
        if (tick == lastPrintedTick) {
            return; // этот такт уже выводили
        }
        lastPrintedTick = tick;

        int[] perSpecies = new int[SpeciesRegistry.ALL.size()];
        List<String> names = new ArrayList<>(SpeciesRegistry.ALL.keySet());
        int plants = 0;
        int animals = 0;
        for (Location loc : island.getLocations()) {
            plants += loc.plantCount();
            for (int i = 0; i < names.size(); i++) {
                perSpecies[i] += loc.count(names.get(i));
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%n=== Такт %d ===%n", tick));
        for (int i = 0; i < names.size(); i++) {
            animals += perSpecies[i];
            Animal sample = SpeciesRegistry.ALL.get(names.get(i)).get();
            sb.append(sample.getEmoji()).append(' ').append(perSpecies[i]).append("  ");
            if (i == 4) {
                sb.append(System.lineSeparator());
            }
        }
        sb.append(String.format("%n🌱 %d | животных всего: %d%n", plants, animals));
        sb.append(String.format("За такт: родилось %d, съедено %d, умерло от голода %d",
                Statistics.getBorn(), Statistics.getEaten(), Statistics.getStarved()));
        System.out.println(sb);

        if (animals == 0) {
            System.out.println("\nВсе животные умерли. Симуляция остановлена.");
            finished.countDown();
        } else if (Settings.MAX_TICKS > 0 && tick >= Settings.MAX_TICKS) {
            System.out.println("\nДостигнут лимит тактов (" + Settings.MAX_TICKS + "). Симуляция остановлена.");
            finished.countDown();
        }
    }

    private void runInPool(Consumer<Location> action) {
        List<Callable<Void>> tasks = new ArrayList<>();
        for (Location loc : island.getLocations()) {
            tasks.add(() -> {
                action.accept(loc);
                return null;
            });
        }
        try {
            for (Future<Void> f : workers.invokeAll(tasks)) {
                f.get(); // пробрасываем исключения из рабочих потоков
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    // Оборачивает задание
    private Runnable guarded(Runnable task) {
        return () -> {
            tickLock.lock();
            try {
                task.run();
            } catch (Throwable t) {
                failure = t;
                finished.countDown();
            } finally {
                tickLock.unlock();
            }
        };
    }

    private void populate() {
        List<Location> cells = island.getLocations();
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (Location loc : cells) {
            for (int i = 0; i < Settings.PLANT_START_PER_CELL; i++) {
                loc.addPlant(new Plant());
            }
        }
        SpeciesRegistry.ALL.forEach((name, factory) -> {
            int count = Settings.params(name).startCount();
            for (int i = 0; i < count; i++) {
                Animal animal = factory.get();
                for (int attempt = 0; attempt < 20; attempt++) {
                    Location loc = cells.get(rnd.nextInt(cells.size()));
                    if (loc.count(name) < animal.getMaxPerCell()) {
                        loc.addAnimal(animal);
                        break;
                    }
                }
            }
        });
    }

    private void shutdown() {
        scheduler.shutdownNow();
        workers.shutdownNow();
    }
}
