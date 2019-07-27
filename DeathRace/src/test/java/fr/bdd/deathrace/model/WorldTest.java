package fr.bdd.deathrace.model;

import fr.bdd.deathrace.model.body.AbstractMobileBody;
import fr.bdd.deathrace.model.body.AbstractPhysicBody;
import fr.bdd.deathrace.model.body.Character;
import fr.bdd.deathrace.model.body.Type;
import fr.bdd.deathrace.model.world.World;
import fr.bdd.deathrace.model.world.WorldFactory;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class WorldTest {

    World world;

    @Before
    public void setUp() throws Exception {
        WorldFactory worldFactory = new WorldFactory("map");
        world = worldFactory.getWorld();
        world.setSize(new Point2D.Double(100,100));
    }

    @Test
    public void getSize() {
    }

    @Test
    public void logErrorMap() {
        WorldFactory worldFactory = new WorldFactory("azdazd");
        world = worldFactory.getWorld();
    }

    @Test
    public void setSize() {
        world.setSize(new Point2D.Double(10,10));
        assertEquals(world.getSize(), new Point2D.Double(10, 10));
    }

    @Test
    public void addBody() {
        AbstractPhysicBody object = new Character(Type.PONEY, new Point2D.Double(0, 0));
        world.addBody(object);
        assertEquals(true, world.getObjects().containsValue(object));
    }

    @Test
    public void getObjects() {
        AbstractPhysicBody object = new Character(Type.PONEY, new Point2D.Double(0, 0));
        world.addBody(object);
        assertNotEquals(world.getObjects(), null);
    }

    @Test
    public void thereIsSomething() {
        AbstractMobileBody object = new Character(Type.PONEY, new Point2D.Double(0, 0));
        world.addBody(object);
        world.sortObjects();
        object.setShape(new Rectangle2D.Double(0, 0, 10, 10));

        // Check if there isn't anything
        object.setPosition(new Point2D.Double(50, 50));
        assertEquals(true, world.thereIsNothing(new Point2D.Double(0, -1), object));

        // Check if we get out of the map
        object.setPosition(new Point2D.Double(0, 0));
        assertEquals(false, world.thereIsNothing(new Point2D.Double(0, -1), object));

        //Check if we encounter another object
        /*object.setPosition(new Point2D.Double(50,50));
        object.setSize(new Point2D.Double(10, 10));
        AbstractPhysicBody object1 = new Poney();
        world.addBody(object1);
        object1.setPosition(new Point2D.Double(60, 50));
        object1.setSize(new Point2D.Double(1, 1));
        object1.setIsTraversable(false);
        assertEquals(world.thereIsSomething(new Point2D.Double(5, 0), object), true);*/
    }
}