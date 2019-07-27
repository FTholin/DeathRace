package fr.bdd.deathrace.model.body;

import fr.bdd.deathrace.controller.Controller;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class FinishingLine extends AbstractPhysicBody {

    /**
     * FinishingLine's constructor.
     *
     * @param x x position.
     * @param y y position.
     */
    public FinishingLine(Long x, Long y) {
        super(
                // position
                new Point2D.Double(x, y),
                // size
                new Rectangle2D.Double(0, 0, 10, 600),
                // Type
                Type.FINISHINGLINE,
                // isTraversable
                true,
                0
        );
    }

    @Override
    public void effectEnter(Runner runner) {
        super.effectEnter(runner);
        Controller.getInstance().getControllerModel().getRace().addToArrivals(runner.getUid());
    }

    @Override
    public void effectExit(Runner runner) {
        super.effectEnter(runner);
    }
}
