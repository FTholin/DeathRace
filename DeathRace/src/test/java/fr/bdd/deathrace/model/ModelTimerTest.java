package fr.bdd.deathrace.model;

import fr.bdd.deathrace.view.IUpdatable;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertNotNull;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;

class Incrementer implements IUpdatable {

    private int compteur;

    Incrementer() {
        compteur = 0;
    }

    @Override
    public void update() {
        compteur++;
    }

    int getCompteur() {
        return compteur;
    }
}

public class ModelTimerTest {

    private Incrementer incrementer;

    @Before
    public void setup() {
        incrementer = new Incrementer();
    }

    @Test
    public void getInstance() {
        ModelTimer modelTimer = ModelTimer.getInstance();
        assertNotNull(modelTimer);
        assertEquals(modelTimer, ModelTimer.getInstance());
    }

    @Test
    public void register() {

        int compteurSave = incrementer.getCompteur();
        ModelTimer.register(incrementer);

        await().atMost(500, TimeUnit.MILLISECONDS).until(() -> {
            int compteurSave2 = incrementer.getCompteur();
            return compteurSave != compteurSave2;
        });

        await().atMost(500, TimeUnit.MILLISECONDS).until(() -> {
            int compteurSave2 = incrementer.getCompteur();
            incrementer.update();
            return compteurSave2 != incrementer.getCompteur();
        });

        ModelTimer.unregister(incrementer);
    }

    @Test
    public void unregister() {
        // register and check it works
        int compteurSave1 = incrementer.getCompteur();
        ModelTimer.register(incrementer);

        await().atMost(500, TimeUnit.MILLISECONDS).until(() -> {
            int compteurSave2 = incrementer.getCompteur();
            return compteurSave1 != compteurSave2;
        });

        // unregister
        ModelTimer.unregister(incrementer);

        int compteurSave3 = incrementer.getCompteur();

        await().atMost(500, TimeUnit.MILLISECONDS).until(() -> {
            int compteurSave4 = incrementer.getCompteur();
            return compteurSave3 == compteurSave4;
        });
    }

    @Test
    public void start() {
        ModelTimer.getInstance();
        ModelTimer.stop();
        int compteurSave1 = incrementer.getCompteur();
        ModelTimer.register(incrementer);

        await().atMost(500, TimeUnit.MILLISECONDS).until(() -> {
            int compteurSave2 = incrementer.getCompteur();
            return compteurSave1 == compteurSave2;
        });

        ModelTimer.start();

        int compteurSave3 = incrementer.getCompteur();

        await().atMost(500, TimeUnit.MILLISECONDS).until(() -> {
            int compteurSave2 = incrementer.getCompteur();
            return compteurSave3 != compteurSave2;
        });
        ModelTimer.unregister(incrementer);

        ModelTimer.unregister(incrementer);
    }

    @Test
    public void doubleStart() {
        ModelTimer.getInstance();
        ModelTimer.stop();
        int compteurSave1 = incrementer.getCompteur();
        ModelTimer.register(incrementer);

        await().atMost(500, TimeUnit.MILLISECONDS).until(() -> {
            int compteurSave2 = incrementer.getCompteur();
            return compteurSave1 == compteurSave2;
        });

        ModelTimer.start();
        ModelTimer.start();

        int compteurSave3 = incrementer.getCompteur();

        await().atMost(500, TimeUnit.MILLISECONDS).until(() -> {
            int compteurSave2 = incrementer.getCompteur();
            return compteurSave3 != compteurSave2;
        });

        ModelTimer.unregister(incrementer);
    }

    @Test
    public void stop() {
        ModelTimer.getInstance();
        ModelTimer.stop();
        int compteurSave1 = incrementer.getCompteur();
        ModelTimer.register(incrementer);

        await().atMost(500, TimeUnit.MILLISECONDS).until(() -> {
            int compteurSave2 = incrementer.getCompteur();
            return compteurSave1 == compteurSave2;
        });

        ModelTimer.start();

        int compteurSave3 = incrementer.getCompteur();

        await().atMost(500, TimeUnit.MILLISECONDS).until(() -> {
            int compteurSave2 = incrementer.getCompteur();
            return compteurSave3 != compteurSave2;
        });

        ModelTimer.unregister(incrementer);
        ModelTimer.start();
    }

    @Test
    public void doubleStop() {
        ModelTimer.getInstance();
        ModelTimer.stop();
        ModelTimer.stop();

        int compteurSave1 = incrementer.getCompteur();
        ModelTimer.register(incrementer);

        await().atMost(500, TimeUnit.MILLISECONDS).until(() -> {
            int compteurSave2 = incrementer.getCompteur();
            return compteurSave1 == compteurSave2;
        });

        ModelTimer.start();

        int compteurSave3 = incrementer.getCompteur();

        await().atMost(500, TimeUnit.MILLISECONDS).until(() -> {
            int compteurSave2 = incrementer.getCompteur();
            return compteurSave3 != compteurSave2;
        });

        ModelTimer.unregister(incrementer);
        ModelTimer.start();
    }

    @Test
    public void clear() {
        ModelTimer.getInstance();
        int compteurSave1 = incrementer.getCompteur();
        ModelTimer.register(incrementer);

        await().atMost(500, TimeUnit.MILLISECONDS).until(() -> {
            int compteurSave2 = incrementer.getCompteur();
            return compteurSave1 != compteurSave2;
        });

        ModelTimer.register(incrementer);

        await().atMost(500, TimeUnit.MILLISECONDS).until(() -> {
            int compteurSave3 = incrementer.getCompteur();
            incrementer.update();
            int compteurSave2 = incrementer.getCompteur();
            return compteurSave2 != compteurSave3;
        });

        ModelTimer.clear();

        int compteurSave4 = incrementer.getCompteur();

        await().atMost(500, TimeUnit.MILLISECONDS).until(() -> {
            int compteurSave2 = incrementer.getCompteur();
            return compteurSave4 == compteurSave2;
        });

        ModelTimer.start();

        await().atMost(500, TimeUnit.MILLISECONDS).until(() -> {
            int compteurSave2 = incrementer.getCompteur();
            return compteurSave4 == compteurSave2;
        });
    }
}