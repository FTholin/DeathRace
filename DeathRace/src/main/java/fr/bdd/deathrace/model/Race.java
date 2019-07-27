package fr.bdd.deathrace.model;

import fr.bdd.deathrace.model.body.AbstractMobileBody;
import fr.bdd.deathrace.model.body.Character;
import fr.bdd.deathrace.model.body.Runner;
import fr.bdd.deathrace.model.world.World;
import fr.bdd.deathrace.model.world.WorldFactory;
import fr.bdd.deathrace.view.IUpdatable;
import org.apache.log4j.Logger;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Race implements IUpdatable, Serializable {
    static final long serialVersionUID = 42L;
    public static final Logger logger = Logger.getLogger(Race.class);

    private World world;
    private Map<Integer, AbstractMobileBody> mobileBodies;
    private Map<Integer, Runner> runners;
    private boolean started;

    private List<Integer> arrivals;


    /**
     * Construct a race base on the lobby.
     *
     * @param lobby lobby
     */
    public Race(Lobby lobby) {
        WorldFactory worldFactory = new WorldFactory(lobby.getMapName());
        world = worldFactory.getWorld();
        mobileBodies = new HashMap<>();
        runners = new HashMap<>();
        arrivals = new ArrayList<>();
        started = false;

        List<Player> players = lobby.getPlayers();
        Player player;
        int i;

        for (i = 0; i < players.size(); i++) {
            player = players.get(i);
            Runner character = new Character(player.getType(), new Point2D.Double(
                    10, 50 + i * world.getSize().y / lobby.getMaxNbPlayers()
            ));

            player.setRunnerUid(character.getUid());
            player.setShowRunnerUid(character.getUid());

            runners.put(character.getUid(), character);
            this.world.addBody(character);
            world.sortObjects();
            mobileBodies.put(character.getUid(), character);
        }
    }

    /**
     * Remove object in race and world.
     * @param uid uid of object
     */
    public void removeObject(Integer uid) {
        if (runners.containsKey(uid)) {
            runners.remove(uid);
        }
        if (mobileBodies.containsKey(uid)) {
            mobileBodies.remove(uid);
        }
        world.removeObject(uid);
    }

    /**
     * Gets the world of the Race.
     *
     * @return the Race's world.
     */
    public World getWorld() {
        return world;
    }

    /**
     * Gets the runner of index i.
     *
     * @param i uid
     * @return the wanted runner.
     */
    public Runner getRunner(int i) {
        return runners.get(i);
    }

    /**
     * Gets the collection of Runners.
     *
     * @return runners.
     */
    public Map<Integer, Runner> getRunners() {
        return this.runners;
    }

    /**
     * Sets the Race's World.
     *
     * @param world world that will be set.
     */
    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Gets arrivals order.
     *
     * @return arrivals
     */
    public List<Integer> getArrivals() {
        return arrivals;
    }

    /**
     * Returns the Race's mobiles bodies.
     *
     * @return the race's mobiles bodies.
     */
    public Map<Integer, AbstractMobileBody> getMobileBodies() {
        return this.mobileBodies;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    /**
     * Copy updated objects to current objects when uid match.
     *
     * @param newMobileBodies updated objects
     */
    public void updateMobileBodies(Map<Integer, AbstractMobileBody> newMobileBodies) {
        newMobileBodies.forEach((key, value) -> {
            if (mobileBodies.containsKey(key)) {
                mobileBodies.get(key).copy(value);
            }
        });
    }

    /**
     * Add uid runner to arrivals.
     *
     * @param uid uid
     */
    public void addToArrivals(Integer uid) {
        if (!this.arrivals.contains(uid)) {
            this.arrivals.add(uid);
        }
    }

    /**
     * Updates function.
     */
    @Override
    public void update() {
        mobileBodies.forEach((key, value) -> value.update(world));
    }
}
