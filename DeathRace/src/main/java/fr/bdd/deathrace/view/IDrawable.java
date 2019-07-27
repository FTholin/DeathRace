package fr.bdd.deathrace.view;

import java.awt.geom.Point2D;

public interface IDrawable {
    /**
     * Draw itself at given position.
     */
    public void draw(Point2D.Double position);
}
