package fr.bdd.deathrace.model.body;

import fr.bdd.deathrace.controller.InputEvent;
import fr.bdd.deathrace.model.world.World;
import fr.bdd.deathrace.view.IUpdatable;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public abstract class Runner extends AbstractMobileBody implements IUpdatable {

    private Set<InputEvent> horizontalMovement;
    private Set<InputEvent> verticalMovement;

    MoveState movestate = MoveState.STOP;

    Point2D.Double deceleration;
    Point2D.Double maxVelocity;

    /**
     * Runner's constructor.
     */
    public Runner(
            Point2D.Double position,
            Rectangle2D.Double shape,
            Type type,
            boolean isTraversable,
            Point2D.Double acceleration,
            Point2D.Double deceleration,
            Point2D.Double maxVelocity,
            int layer) {
        super(position, shape, type, isTraversable, acceleration, layer);
        this.deceleration = deceleration;
        this.maxVelocity = maxVelocity;
        this.horizontalMovement = Collections.synchronizedSet(EnumSet.noneOf(InputEvent.class));
        this.verticalMovement = Collections.synchronizedSet(EnumSet.noneOf(InputEvent.class));
    }

    /**
     * Event input handler.
     *
     * @param inputEvent : explicit declaration
     */
    public void keyPressedEvent(InputEvent inputEvent) {
        switch (inputEvent) {
            case UP:
                verticalMovement.add(inputEvent);
                direction.y = -1;
                break;
            case DOWN:
                verticalMovement.add(inputEvent);
                direction.y = 1;
                break;
            case RIGHT:
                horizontalMovement.add(inputEvent);
                direction.x = 1;
                break;
            case LEFT:
                horizontalMovement.add(inputEvent);
                direction.x = -1;
                break;
            default:
                break;
        }

        movestate = MoveState.MOVE;
    }

    /**
     * Event input handler.
     *
     * @param inputEvent : explicit declaration
     */
    public void keyReleasedEvent(InputEvent inputEvent) {
        switch (inputEvent) {
            case UP:
                verticalMovement.remove(inputEvent);
                if (verticalMovement.isEmpty()) {
                    direction.y = 0;
                } else {
                    direction.y = 1;
                }
                break;
            case DOWN:
                verticalMovement.remove(inputEvent);
                if (verticalMovement.isEmpty()) {
                    direction.y = 0;
                } else {
                    direction.y = -1;
                }
                break;
            case RIGHT:
                horizontalMovement.remove(inputEvent);
                if (horizontalMovement.isEmpty()) {
                    direction.x = 0;
                } else {
                    direction.x = -1;
                }
                break;
            case LEFT:
                horizontalMovement.remove(inputEvent);
                if (horizontalMovement.isEmpty()) {
                    direction.x = 0;
                } else {
                    direction.x = 1;
                }
                break;
            default:
                break;
        }


        if (direction.x == 0 && direction.y == 0) {
            movestate = MoveState.STOP;
        }
    }

    /**
     * Update method for the sprite.
     */
    @Override
    public void update(World world) {
        super.update(world);
        moveXAxis();
        moveYAxis();
        if (Math.abs(velocity.x) > maxVelocity.x) {
            velocity.x = (velocity.x < 0 ? -1 : 1) * maxVelocity.x;
        }
        if (Math.abs(velocity.y) > maxVelocity.y) {
            velocity.y = (velocity.y < 0 ? -1 : 1) * maxVelocity.y;
        }
    }

    /**
     * Moves the body on the X axis.
     */
    private void moveXAxis() {
        if (direction.x != 0) {
            velocity.x = acceleration.x * direction.x + velocity.x;
        } else if (velocity.x > 0) {
            velocity.x -= deceleration.x;
            if (velocity.x < 0) {
                velocity.x = 0;
            }
        } else if (velocity.x < 0) {
            velocity.x += deceleration.x;
            if (velocity.x > 0) {
                velocity.x = 0;
            }
        }
    }

    /**
     * Moves the body on the Y axis.
     */
    public void moveYAxis() {
        if (direction.y != 0) {
            velocity.y += acceleration.y * direction.y;
        } else if (velocity.y > 0) {
            velocity.y -= deceleration.y;
            if (velocity.y < 0) {
                velocity.y = 0;
            }
        } else if (velocity.y < 0) {
            velocity.y += deceleration.y;
            if (velocity.y > 0) {
                velocity.y = 0;
            }
        }
    }

    /**
     * When the body enters in collision.
     * @param body the body that has been collided.
     */
    @Override
    public void onCollisionEnter(AbstractPhysicBody body) {
        super.onCollisionEnter(body);
        body.effectEnter(this);
    }

    /**
     * When the body exits collision.
     * @param body the body that has been exited.
     */
    @Override
    public void onCollisionExit(AbstractPhysicBody body) {
        super.onCollisionExit(body);
        body.effectExit(this);
    }

    /**
     * Copy constructor.
     *
     * @param runner : already explicit call
     */
    public void copy(Runner runner) {
        super.copy(runner);
        this.movestate = runner.movestate;
        this.acceleration = runner.acceleration;
        this.deceleration = runner.deceleration;
        this.maxVelocity = runner.maxVelocity;
    }

    /**
     * Check si l'objet bouge ou non.
     *
     * @return true si le runner bouge
     */
    public boolean isMoving() {
        return movestate == MoveState.MOVE;
    }
}
