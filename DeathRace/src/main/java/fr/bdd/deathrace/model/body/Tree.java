package fr.bdd.deathrace.model.body;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Tree extends AbstractPhysicBody {

    /**
     * Tree's constructor.
     * @param x x position.
     * @param y y position.
     */
    public Tree(Long x, Long y) {
        super(
                // position
                new Point2D.Double(x, y),
                // size
                new Rectangle2D.Double(0, 0, 40, 40),
                // Type
                Type.TREE,
                // isTraversable
                false,
                1
        );
    }
}
