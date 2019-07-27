package fr.bdd.deathrace.model.body;

import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;

class PhysicBody extends AbstractPhysicBody {
    PhysicBody() {
        super(
                // position
                new Point2D.Double(0, 0),
                // size
                new Rectangle2D.Double(0, 0, 15, 15),
                // Type
                Type.TREE,
                // isTraversable
                false,
                0
        );
    }
}

public class AbstractPhysicBodyTest {

    AbstractPhysicBody body1;
    AbstractPhysicBody body2;

    @Before
    public void setUp() throws Exception {
        body1 = new PhysicBody();
        body2 = new PhysicBody();
    }

    @Test
    public void copy() {
        body1.copy(body2);
        assertEquals(body1.position, body2.position);
        assertEquals(body1.type, body2.type);
        assertEquals(body1.shape, body2.shape);
        assertEquals(body1.isTraversable, body2.isTraversable);
    }

    @Test
    public void getTaille() {
        assertEquals(body1.shape, body1.getShape());
    }

    @Test
    public void getPosition() {
        assertEquals(body1.position, body1.getPosition());
    }

    @Test
    public void getType() {
        assertEquals(body1.type, body1.getType());
    }

    @Test
    public void setPosition() {
        Point2D.Double position = new Point2D.Double(4,4);
        body1.setPosition(position);
        assertEquals(body1.position, position);
    }

    @Test
    public void setTaille() {
        Rectangle2D.Double taille = new Rectangle2D.Double(0, 0, 5,5);
        body1.setShape(taille);
        assertEquals(body1.getShape(), taille);
    }

    @Test
    public void setType() {
        Type type = Type.CROCODILE;
        body1.setType(type);
        assertEquals(body1.getType(), type);
    }

    @Test
    public void getIsTraversable() {
        assertEquals(body1.getIsTraversable(), body1.isTraversable);
    }

    @Test
    public void setIsTraversable() {
        body1.setIsTraversable(true);
        assertEquals(true, body1.getIsTraversable());
    }
}