package fr.bdd.deathrace.view;

import javafx.animation.AnimationTimer;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Singleton that update registered windows 60 time per seconds.
 */
public class UpdateTimer extends AnimationTimer {
    private static UpdateTimer instance = null;

    private ConcurrentLinkedQueue<IUpdatable> views;

    /**
     * Private constructor.
     */
    private UpdateTimer() {
        views = new ConcurrentLinkedQueue<>();
    }

    /**
     * Register a window that needs to be updated.
     * @param updatableView window
     */
    public synchronized void register(IUpdatable updatableView) {
        views.add(updatableView);
    }

    /**
     * Unregister a window that no more needs to be updated.
     * @param updatableView window
     */
    public synchronized void unregister(IUpdatable updatableView) {
        views.remove(updatableView);
    }

    @Override
    public synchronized void handle(long now) {
        for (IUpdatable updatableView : views) {
            updatableView.update();
        }
    }

    /**
     * Get the lone instance of timer. If first call, initialise and start timer.
     * @return instance
     */
    public static UpdateTimer getInstance() {
        if (instance == null) {
            instance = new UpdateTimer();
            instance.start();
        }
        return instance;
    }
}
