package fr.bdd.deathrace.model.body;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.UUID;

public abstract class AbstractPhysicBody implements Serializable {

    private static final org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(AbstractPhysicBody.class);
    static final long serialVersionUID = 42L;

    private final int uid = UUID.randomUUID().hashCode();
    protected Point2D.Double position;
    protected Rectangle2D.Double shape;
    protected Type type;
    protected boolean isTraversable;
    protected int layer;

    /**
     * AbstractPhysicBody's constructor.
     */
    public AbstractPhysicBody(
            Point2D.Double position,
            Rectangle2D.Double shape,
            Type type,
            boolean isTraversable,
            int layer) {
        this.position = position;
        this.shape = shape;
        this.type = type;
        this.isTraversable = isTraversable;
        this.layer = layer;
        alignRect();
    }

    /**
     * Gets the body's UID.
     *
     * @return uid.
     */
    public int getUid() {
        return this.uid;
    }

    /**
     * Copy constructor.
     *
     * @param other : explicit physic body
     */
    public void copy(AbstractPhysicBody other) {
        this.position = other.position;
        this.shape = other.shape;
        this.type = other.type;
        this.isTraversable = other.isTraversable;
    }

    /**
     * Gets the body's size.
     *
     * @return size
     */
    public Rectangle2D.Double getShape() {
        return shape;
    }

    /**
     * Gets the body's position.
     *
     * @return position.
     */
    public Point2D.Double getPosition() {
        return position;
    }

    /**
     * Gets the body's type.
     *
     * @return type.
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the body's position.
     *
     * @param position body's position.
     */
    public void setPosition(Point2D.Double position) {
        this.position = position;
    }

    /**
     * Sets the body's shape.
     *
     * @param shape body's shape.
     */
    public void setShape(Rectangle2D.Double shape) {
        this.shape = shape;
    }

    /**
     * Sets the body's type.
     *
     * @param type body's type.
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Gets the boolean isTraversable.
     *
     * @return isTraversable
     */
    public boolean getIsTraversable() {
        return isTraversable;
    }

    /**
     * Sets the boolean isTraversable.
     *
     * @param isTraversable the boolean to set.
     */
    public void setIsTraversable(boolean isTraversable) {
        this.isTraversable = isTraversable;
    }

    /**
     * The effect that has this body on the runner that collides it.
     *
     * @param runner the runner that undergoes the effect.
     */
    public void effectEnter(Runner runner) {
        // Nothing to specify here
    }

    /**
     * The effect that has this body on the runner that exits it.
     *
     * @param runner the runner that leaves the effect.
     */
    public void effectExit(Runner runner) {
        // Nothing to specify here
    }

    /**
     * Displays all body's informations.
     */
    public void displayInformations() {
        logger.info("Body's uid : " + uid + ", position : " + position + ", shape : "
                + shape + ", type : " + type + ", isTraversable : " + isTraversable
                + " Position x and y : " + position.x + "," + position.y
                + "Shape position : " + shape.x + "," + shape.y);
    }

    /**
     * Aligns the rectangle with the body's position.
     */
    public void alignRect() {
        shape.setRect(position.x, position.y, shape.width, shape.height);
    }

    /**
     * Gets tha layer of the body.
     * @return layer.
     */
    public int getLayer() {
        return layer;
    }
}
