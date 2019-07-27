package fr.bdd.deathrace.model.body;

import fr.bdd.deathrace.model.world.World;
import org.apache.log4j.Logger;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract class AbstractMobileBody extends AbstractPhysicBody {

    private static final Logger logger = Logger.getLogger(AbstractMobileBody.class);

    public enum MoveState {
        MOVE, STOP
    }

    protected Point2D.Double direction;
    protected Point2D.Double velocity;
    protected Point2D.Double acceleration;

    /**
     * AbstractMobileBody's constructor.
     */
    public AbstractMobileBody(
            Point2D.Double position,
            Rectangle2D.Double shape,
            Type type,
            boolean isTraversable,
            Point2D.Double acceleration,
            int layer) {
        super(position, shape, type, isTraversable, layer);
        this.acceleration = acceleration;
        direction = new Point2D.Double(0, 0);
        velocity = new Point2D.Double(0, 0);
    }

    /**
     * Update method.
     * @param world the world.
     */
    public void update(World world) {
        if (velocity.y > 0) {
            moveYPositive(world);
        }
        if (velocity.y < 0) {
            moveYNegative(world);
        }
        if (velocity.x > 0) {
            moveXPositive(world);
        }
        if (velocity.x < 0) {
            moveXNegative(world);
        }
    }

    /**
     * Moves the body on X axis
     * @param world the world needed to check.
     */
    public void moveXNegative(World world) {
        for (int i = (int)velocity.x; i < 0; i++) {
            if (world.thereIsNothing(new Point2D.Double(-1, 0), this)) {
                position.setLocation(position.x - 1, position.y);
                alignRect();
            }
        }
    }

    /**
     * Moves the body on X axis
     * @param world the world needed to check.
     */
    public void moveXPositive(World world) {
        for (int i = 0; i < (int)velocity.x; i++) {
            if (world.thereIsNothing(new Point2D.Double(1, 0), this)) {
                position.setLocation(position.x + 1, position.y);
                alignRect();
            }
        }
    }

    /**
     * Moves the body on Y axis.
     * @param world the world needed to check.
     */
    public void moveYPositive(World world) {
        for (int i = 0; i < (int)velocity.y; i++) {
            if (world.thereIsNothing(new Point2D.Double(0, 1), this)) {
                position.setLocation(position.x, position.y + 1);
                alignRect();
            }
        }
    }

    /**
     * Moves the body on Y axis.
     * @param world the world needed to check.
     */
    public void moveYNegative(World world) {
        for (int i = (int)velocity.y; i < 0; i++) {
            if (world.thereIsNothing(new Point2D.Double(0, -1), this)) {
                position.setLocation(position.x, position.y - 1);
                alignRect();
            }
        }
    }

    /**
     * When the body enters in collision.
     * @param body the body that has been collided.
     */
    public void onCollisionEnter(AbstractPhysicBody body) {
        logger.info("A " + type + " has collided a " + body.getType() + ".");
    }

    /**
     * When the body exits collision.
     * @param body the body that has been exited.
     */
    public void onCollisionExit(AbstractPhysicBody body) {
        logger.info("A " + type + " doesn't collide a " + body.getType() + " anymore.");
    }

    /**
     * Copy method.
     * @param other the other body.
     */
    public void copy(AbstractMobileBody other) {
        super.copy(other);
        this.direction = other.direction;
        this.velocity = other.velocity;
    }
}
