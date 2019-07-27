package fr.bdd.deathrace.model.body;

import fr.bdd.deathrace.model.world.World;
import fr.bdd.deathrace.model.world.WorldFactory;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

class MobileBody extends AbstractMobileBody {
    MobileBody() {
        super(
                // position
                new Point2D.Double(0,0),
                // size
                new Rectangle2D.Double(0, 0, 20, 20),
                // Type
                Type.PONEY,
                // isTraversable
                false,
                // acceleration
                new Point2D.Double(3, 3),
                2
        );
    }
}

public class AbstractMobileBodyTest {

    MobileBody body1;
    MobileBody body2;
    World world;
    Point2D.Double prev_position;

    @Before
    public void setUp() throws Exception {
        body1 = new MobileBody();
        body1.setShape(new Rectangle2D.Double(0, 0, 10, 10));
        body2 = new MobileBody();
        WorldFactory worldFactory = new WorldFactory("map");
        world = worldFactory.getWorld();
        world.setSize(new Point2D.Double(100, 100));
        body1.setPosition(new Point2D.Double(50, 50));
        world.addBody(body1);
        world.addBody(body2);
        world.sortObjects();
    }

    @Test
    public void update() {
        body1.velocity.y = 1;
        body1.velocity.x = 0;
        prev_position = new Point2D.Double(body1.position.x, body1.position.y);
        body1.update(world);
        assertEquals(body1.position, new Point2D.Double(prev_position.x, prev_position.y + 1));

        body1.velocity.y = -1;
        body1.velocity.x = 0;
        prev_position = new Point2D.Double(body1.position.x, body1.position.y);
        body1.update(world);
        assertEquals(body1.position, new Point2D.Double(prev_position.x, prev_position.y - 1));

        body1.velocity.x = 1;
        body1.velocity.y = 0;
        prev_position = new Point2D.Double(body1.position.x, body1.position.y);
        body1.update(world);
        assertEquals(body1.position, new Point2D.Double(prev_position.x + 1, prev_position.y));

        body1.velocity.x = -1;
        body1.velocity.y = 0;
        prev_position = new Point2D.Double(body1.position.x, body1.position.y);
        body1.update(world);
        assertEquals(body1.position, new Point2D.Double(prev_position.x - 1, prev_position.y));
    }

    @Test
    public void moveXNegative() {
        body1.velocity.x = -1;
        body1.velocity.y = 0;
        prev_position = new Point2D.Double(body1.position.x, body1.position.y);
        body1.moveXNegative(world);
        assertEquals(body1.position, new Point2D.Double(prev_position.x - 1, prev_position.y));
    }

    @Test
    public void moveXPositive() {
        body1.velocity.x = 1;
        body1.velocity.y = 0;
        prev_position = new Point2D.Double(body1.position.x, body1.position.y);
        body1.moveXPositive(world);
        assertEquals(body1.position, new Point2D.Double(prev_position.x + 1, prev_position.y));
    }

    @Test
    public void moveYPositive() {
        body1.velocity.y = 1;
        body1.velocity.x = 0;
        prev_position = new Point2D.Double(body1.position.x, body1.position.y);
        body1.moveYPositive(world);
        assertEquals(body1.position, new Point2D.Double(prev_position.x, prev_position.y + 1));
    }

    @Test
    public void moveYNegative() {
        body1.velocity.y = -1;
        body1.velocity.x = 0;
        prev_position = new Point2D.Double(body1.position.x, body1.position.y);
        body1.moveYNegative(world);
        assertEquals(body1.position, new Point2D.Double(prev_position.x, prev_position.y - 1));
    }

    @Test
    public void onCollisionEnter() {
        body1.onCollisionEnter(body2);
    }

    @Test
    public void onCollisionExit() {
        body1.onCollisionExit(body2);
    }

    @Test
    public void checkMoveState() {
        assertNotEquals(AbstractMobileBody.MoveState.MOVE, null);
        assertNotEquals(AbstractMobileBody.MoveState.STOP, null);

    }

    @Test
    public void copy() {
        body1.copy(body2);
        assertEquals(body1.position, body2.position);
        assertEquals(body1.type, body2.type);
        assertEquals(body1.shape, body2.shape);
        assertEquals(body1.isTraversable, body2.isTraversable);
        assertEquals(body1.velocity, body2.velocity);
        assertEquals(body1.direction, body2.direction);
    }
}