package fr.bdd.deathrace.model.body;

import org.apache.log4j.Logger;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Character extends Runner {

    private static final Logger logger = Logger.getLogger(Character.class);

    /**
     * Constructor of sprites.
     */
    public Character(Type type, Point2D.Double position) {
        super(
                // position
                position,
                // shape
                new Rectangle2D.Double(),
                // Type
                type,
                // isTraversable
                false,
                // acceleration
                new Point2D.Double(),
                // deceleration
                new Point2D.Double(),
                // maxVelocity
                new Point2D.Double(),
                2
        );
        switch (type) {
            case PONEY:
                shape.width = 30;
                shape.height = 30;
                isTraversable = false;
                acceleration.setLocation(1,1);
                deceleration.setLocation(1, 1);
                maxVelocity.setLocation(5, 5);
                break;
            case CROCODILE:
                shape.width = 25;
                shape.height = 25;
                isTraversable = false;
                acceleration.setLocation(2,2);
                deceleration.setLocation(2, 2);
                maxVelocity.setLocation(6, 6);
                break;
            default:
                logger.debug("There's no type associated.");
                break;
        }
    }

    @Override
    public void update() {
        // Does nothing because there isn't anything to specify here.
    }
}
