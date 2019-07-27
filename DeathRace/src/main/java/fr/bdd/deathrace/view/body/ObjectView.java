package fr.bdd.deathrace.view.body;

import fr.bdd.deathrace.model.body.AbstractPhysicBody;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.log4j.Logger;

import java.awt.geom.Point2D;

public class ObjectView extends AbstractBodyView {

    private static final Logger logger = Logger.getLogger(ObjectView.class);

    private AbstractPhysicBody object;
    private Color color;

    /**
     * PoneyView's constructor.
     * @param object the poney.
     * @param gc the graphic context
     */
    public ObjectView(AbstractPhysicBody object, GraphicsContext gc) {
        this.object = object;
        this.gc = gc;
        switch (object.getType()) {
            case CROCODILE:
                color = Color.YELLOW;
                break;
            case PONEY:
                color = Color.DARKRED;
                break;
            case DIRT:
                color = Color.SANDYBROWN;
                break;
            case TREE:
                color = Color.GREEN;
                break;
            case BOOST:
                color = Color.LIGHTBLUE;
                break;
            case FINISHINGLINE:
                color = Color.DARKBLUE;
                break;
            case MUD:
                color = Color.BROWN;
                break;
            default:
                logger.debug("No associated.");
                break;
        }
    }

    /**
     * Draws the object.
     * @param position the object's position
     */
    @Override
    public void draw(Point2D.Double position) {
        gc.setFill(color);
        gc.fillRect(
                position.x,
                position.y,
                object.getShape().width,
                object.getShape().height);
    }

    /**
     * Returns the AbstractPhysicBody of the poney.
     * @return poney the wanted poney's body
     */
    @Override
    public AbstractPhysicBody getAbstractPhysicObject() {
        return object;
    }

    @Override
    public int getUid() {
        return this.object.getUid();
    }
}
