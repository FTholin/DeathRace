package fr.bdd.deathrace.model.body;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Dirt extends AbstractPhysicBody {

    /**
     * Dirt's constructor.
     * @param x x position.
     * @param y y position.
     */
    public Dirt(Long x, Long y) {
        super(
                // position
                new Point2D.Double(x, y),
                // size
                new Rectangle2D.Double(0, 0, 100, 100),
                // Type
                Type.DIRT,
                // isTraversable
                true,
                0
        );
    }

    @Override
    public void effectEnter(Runner runner) {
        super.effectEnter(runner);
        runner.maxVelocity = new Point2D.Double(runner.maxVelocity.x / 2, runner.maxVelocity.y / 2);
    }

    @Override
    public void effectExit(Runner runner) {
        super.effectEnter(runner);
        runner.maxVelocity = new Point2D.Double(runner.maxVelocity.x * 2, runner.maxVelocity.y * 2);
    }
}
