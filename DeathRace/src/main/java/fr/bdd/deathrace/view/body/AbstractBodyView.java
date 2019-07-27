package fr.bdd.deathrace.view.body;

import fr.bdd.deathrace.model.body.AbstractPhysicBody;
import fr.bdd.deathrace.view.IDrawable;
import javafx.scene.canvas.GraphicsContext;

public abstract class AbstractBodyView implements IDrawable {
    protected GraphicsContext gc;
    
    public abstract AbstractPhysicBody getAbstractPhysicObject();

    /**
     * Gets the body's UID.
     * @return uid.
     */
    public abstract int getUid();
}
