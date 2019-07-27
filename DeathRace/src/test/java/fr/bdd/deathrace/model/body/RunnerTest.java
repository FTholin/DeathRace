package fr.bdd.deathrace.model.body;

import fr.bdd.deathrace.controller.InputEvent;
import fr.bdd.deathrace.model.world.World;
import fr.bdd.deathrace.model.world.WorldFactory;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RunnerTest {

    Character character1;
    Character character2;

    @Before
    public void setUp() throws Exception {
        character1 = new Character(Type.PONEY, new Point2D.Double(0, 0));
        character2 = new Character(Type.PONEY, new Point2D.Double(0, 0));
    }

    @Test
    public void keysTest() {
        character1.keyPressedEvent(InputEvent.UP);
        character1.keyPressedEvent(InputEvent.LEFT);
        character1.keyPressedEvent(InputEvent.RIGHT);
        character1.keyPressedEvent(InputEvent.DOWN);

        character1.direction.y = -1;
        character1.keyReleasedEvent(InputEvent.UP);
        character1.direction.y = 1;
        character1.keyReleasedEvent(InputEvent.DOWN);
        character1.direction.x = 1;
        character1.keyReleasedEvent(InputEvent.RIGHT);
        character1.direction.x = -1;
        character1.keyReleasedEvent(InputEvent.LEFT);
    }

    @Test
    public void getUid() {
        assertNotEquals(character1.getUid(), character2.getUid());
    }

    @Test
    public void copy() {
        character1.copy(character2);
        assertEquals(character1.position, character2.position);
        assertEquals(character1.type, character2.type);
        assertEquals(character1.shape, character2.shape);
        assertEquals(character1.isTraversable, character2.isTraversable);
        assertEquals(character1.velocity, character2.velocity);
        assertEquals(character1.direction, character2.direction);
        assertEquals(character1.deceleration, character2.deceleration);
        assertEquals(character1.maxVelocity, character2.maxVelocity);
        assertEquals(character1.acceleration, character2.acceleration);
        assertEquals(character1.movestate, character2.movestate);
    }

    @Test
    public void getTaille() {
        assertEquals(character1.shape, character1.getShape());
    }

    @Test
    public void getPosition() {
        assertEquals(character1.getPosition(), character1.position);
    }

    @Test
    public void getType() {
        assertEquals(character1.type, character1.getType());
    }

    @Test
    public void setPosition() {
        Point2D.Double position = new Point2D.Double(4,4);
        character1.setPosition(position);
        assertEquals(character1.position, position);
    }

    @Test
    public void setTaille() {
        Rectangle2D.Double taille = new Rectangle2D.Double(0, 0, 5,5);
        character1.setShape(taille);
        assertEquals(character1.getShape(), taille);
    }

    @Test
    public void setType() {
        Type type = Type.CROCODILE;
        character1.setType(type);
        assertEquals(character1.getType(), type);
    }

    @Test
    public void getIsTraversable() {
        assertEquals(character1.getIsTraversable(), character1.isTraversable);
    }

    @Test
    public void update() {
        WorldFactory worldFactory = new WorldFactory("map");
        World world = worldFactory.getWorld();
        character1.velocity.x = 0;
        character1.velocity.y = 0;
        character1.direction = new Point2D.Double(1,0);
        character1.update(world);

        character1.velocity.x = -1;
        character1.velocity.y = 0;
        character1.update(world);

        character1.velocity.x = 1;
        character1.velocity.y = 0;
        character1.update(world);

        character1.velocity.x = 0;
        character1.velocity.y = 0;
        character1.direction = new Point2D.Double(0,1);
        character1.update(world);

        character1.velocity.x = 0;
        character1.velocity.y = -1;
        character1.update(world);

        character1.velocity.x = 0;
        character1.velocity.y = 1;
        character1.update(world);

        character1.maxVelocity = new Point2D.Double(1,1);
        character1.velocity.x = 2;
        character1.velocity.y = 2;
        character1.update();
        character1.velocity.x = -2;
        character1.velocity.y = -2;
        character1.update();


    }
}