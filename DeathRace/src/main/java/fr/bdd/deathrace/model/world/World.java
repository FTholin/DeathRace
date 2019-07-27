package fr.bdd.deathrace.model.world;

import fr.bdd.deathrace.model.body.AbstractMobileBody;
import fr.bdd.deathrace.model.body.AbstractPhysicBody;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World implements Serializable {
    static final long serialVersionUID = 42L;

    private Point2D.Double size;
    private Map<Integer, AbstractPhysicBody> objects;
    private List<AbstractPhysicBody> objectsSorted;
    protected Rectangle2D.Double shape;

    // store enter et exit collisions
    private List<AbstractPhysicBody> collisionEnter;
    private List<AbstractPhysicBody> collisionExit;

    /**
     * World's constructor.
     */
    public World() {
        size = new Point2D.Double();
        shape = new Rectangle2D.Double();
        objects = new HashMap<>();
        objectsSorted = new ArrayList<>();
        collisionEnter = new ArrayList<>();
        collisionExit = new ArrayList<>();
    }

    /**
     * Remove object in objects.
     * @param uid uid of object
     */
    public void removeObject(Integer uid) {
        objects.remove(uid);
    }

    /**
     * Returns the size of the map.
     * @return size size of the map.
     */
    public Point2D.Double getSize() {
        return size;
    }

    /**
     * Sets the map's size.
     * @param size the map's size.
     */
    public void setSize(Point2D.Double size) {
        this.size = size;
    }

    /**
     * Adds a body to the list of objects.
     * @param body body that will be added.
     */
    public void addBody(AbstractPhysicBody body) {
        objects.putIfAbsent(body.getUid(), body);
    }

    /**
     * Returns the map's list of objects
     * @return objects list of map's objects.
     */
    public Map<Integer, AbstractPhysicBody> getObjects() {
        return objects;
    }

    /**
     * Checks if the movement is possible.
     * @param direction movement direction.
     * @param body body that wants to move.
     * @return if there's something at the wanted position.
     */
    public boolean thereIsNothing(Point2D.Double direction, AbstractMobileBody body) {
        collisionEnter.clear();
        collisionExit.clear();

        Rectangle2D.Double recNextBody = new Rectangle2D.Double(
                body.getPosition().x + direction.x,
                body.getPosition().y + direction.y,
                body.getShape().width,
                body.getShape().height);

        // check if in map
        if (!shape.contains(recNextBody)) {
            return false;
        }

        boolean currentIntersectObject;
        boolean nextIntersectObject;

        for (AbstractPhysicBody object : objectsSorted) {
            if (object == body) {
                continue;
            }

            // compute collision
            currentIntersectObject = body.getShape().intersects(object.getShape());
            nextIntersectObject = recNextBody.intersects(object.getShape());

            if (!currentIntersectObject && nextIntersectObject) {
                // if enter collision
                if (!object.getIsTraversable()) {
                    // collision on untraversable object, movement is impossible
                    return false;
                }
                collisionEnter.add(object);

            } else if (currentIntersectObject && !nextIntersectObject) {
                // if exit collision
                collisionExit.add(object);
            }
        }

        // process enter and exit collisions if movement is possible
        collisionEnter.forEach(body::onCollisionEnter);
        collisionExit.forEach(body::onCollisionExit);

        return true;
    }

    /**
     * Sorts the list of objects by layer.
     */
    public void sortObjects() {
        objectsSorted = new ArrayList<>(objects.values());
        objectsSorted.sort((object, object2) -> object2.getLayer() - object.getLayer());
    }
}
