package fr.bdd.deathrace.model;

import fr.bdd.deathrace.view.IUpdatable;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton. Update only the model.
 */
public class ModelTimer implements Runnable {

    private static final long MS_PER_UPDATE = 16; // 60 frame per second
    private static ModelTimer instance;

    /**
     * Get the lone instance.
     * @return lone instance
     */
    public static ModelTimer getInstance() {
        if (instance == null) {
            instance = new ModelTimer();
            start();
        }
        return instance;
    }

    /**
     * Register updatable to list of object to update.
     * @param updatable to register
     */
    public static void register(IUpdatable updatable) {
        getInstance().add(updatable);
    }

    /**
     * Unregister updatable to list of object to update.
     * @param updatable to unregister
     */
    public static void unregister(IUpdatable updatable) {
        getInstance().remove(updatable);
    }

    /**
     * Start a thread that update all registered objects.
     */
    public static void start() {
        ModelTimer modelTimer = getInstance();
        if (!modelTimer.run) {
            modelTimer.run = true;
            new Thread(modelTimer).start();
        }
    }

    /**
     * Stop thread that update all registered objects.
     */
    public static void stop() {
        ModelTimer modelTimer = getInstance();
        if (modelTimer.run) {
            modelTimer.run = false;
        }
    }

    /**
     * Stop thread, clear all registered objects.
     */
    public static void clear() {
        stop();
        getInstance().deleteAll();
    }

    private final List<IUpdatable> thingsToUpdate;
    private boolean run;

    /**
     * Initialize properties.
     */
    private ModelTimer() {
        this.thingsToUpdate = new ArrayList<>();
        this.run = false;
    }

    @Override
    public void run() {
        long previous = System.currentTimeMillis();
        long lag = 0;
        while (run) {
            long current = System.currentTimeMillis();
            long elapsed = current - previous;
            previous = current;
            lag += elapsed;

            // update the model until lag isn't balance
            while (lag >= MS_PER_UPDATE) {
                updateModel();
                lag -= MS_PER_UPDATE;
            }

            long time = current + MS_PER_UPDATE - System.currentTimeMillis();
            try {
                if (time > 0) {
                    Thread.sleep(time);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                this.run = false;
            }
        }
    }

    /**
     * Update all objects to update.
     */
    private void updateModel() {
        synchronized (this.thingsToUpdate) {
            for (IUpdatable things : thingsToUpdate) {
                things.update();
            }
        }
    }

    /**
     * Add updatable to list of objects to update.
     * @param updatable to add
     */
    private void add(IUpdatable updatable) {
        synchronized (this.thingsToUpdate) {
            this.thingsToUpdate.add(updatable);
        }
    }

    /**
     * Remove updatable to list of objects to update.
     * @param updatable to remove
     */
    private void remove(IUpdatable updatable) {
        synchronized (this.thingsToUpdate) {
            this.thingsToUpdate.remove(updatable);
        }
    }

    /**
     * Clear list of objects.
     */
    private void deleteAll() {
        synchronized (this.thingsToUpdate) {
            this.thingsToUpdate.clear();
        }
    }
}
