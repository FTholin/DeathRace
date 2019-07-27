package fr.bdd.deathrace.view;

import fr.bdd.deathrace.controller.Controller;
import fr.bdd.deathrace.controller.ControllerModel;
import fr.bdd.deathrace.model.Player;
import fr.bdd.deathrace.model.Race;
import fr.bdd.deathrace.view.body.AbstractBodyView;
import fr.bdd.deathrace.view.body.ObjectView;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Race view, display race.
 */
public class RaceCanvasView extends Canvas implements IUpdatable {
    
    private GraphicsContext gc;
    private Controller controller;
    private Map<Integer, AbstractBodyView> bodyViews;
    private List<AbstractBodyView> bodyViewsSorted;
    private Point2D.Double sizeMap;
    private double width;
    private double height;
    private Player player;

    /**
     * Race canvas constructor.
     *
     * @param controller controller
     * @param race       race
     * @param width      width
     * @param height     height
     * @param player     main player
     */
    public RaceCanvasView(Controller controller, Race race, double width, double height,
                          Player player) {
        super(width, height);
        this.controller = controller;
        this.gc = this.getGraphicsContext2D();
        this.width = width;
        this.height = height;
        this.player = player;
        this.sizeMap = race.getWorld().getSize();
        bodyViews = new HashMap<>();

        setKeyEvent();

        race.getWorld().getObjects().forEach((key, value) ->
            // Avoid displaying the same poney twice.
            bodyViews.put(key, new ObjectView(value, gc))
        );
        bodyViewsSorted =
                new ArrayList<>(bodyViews.values());

        bodyViewsSorted.sort(Comparator.comparingInt(integerAbstractBodyViewEntry ->
                integerAbstractBodyViewEntry.getAbstractPhysicObject().getLayer()));
    }

    @Override
    public void update() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        AbstractBodyView main = bodyViews.get(player.getShowRunnerUid());

        Rectangle rect = new Rectangle(
                0 - (main.getAbstractPhysicObject().getPosition().x - width / 2),
                0 - (main.getAbstractPhysicObject().getPosition().y - height / 2),
                sizeMap.x,
                sizeMap.y);
        gc.setFill(Color.GREY);
        gc.fillRect(
                rect.getX(),
                rect.getY(),
                rect.getWidth(),
                rect.getHeight());

        bodyViewsSorted.forEach((value) ->
                value.draw(new Point2D.Double(
                        value.getAbstractPhysicObject().getPosition().x
                                - (main.getAbstractPhysicObject().getPosition().x - width / 2),
                        value.getAbstractPhysicObject().getPosition().y
                                - (main.getAbstractPhysicObject().getPosition().y - height / 2)
                ))
        );

        ControllerModel controllerModel = Controller.getInstance().getControllerModel();

        List<Integer> arrivals = controllerModel.getRace().getArrivals();
        List<Player> players = controllerModel.getLobby().getPlayers();

        for (int i = 0; i < arrivals.size(); i++) {
            for (Player player1 : players) {
                if (player1.getRunnerUid() == arrivals.get(i)) {
                    gc.setFill(Color.WHITE);
                    gc.fillText((i + 1) + " : " + player1.getPseudo(), 100.0 * i, 20);
                    break;
                }
            }
        }

        main.draw(new Point2D.Double(gc.getCanvas().getWidth() / 2,
                gc.getCanvas().getHeight() / 2));
    }

    private void setKeyEvent() {
        this.setFocusTraversable(true);
        this.setOnKeyPressed(event -> controller.keyPressedEvent(event.getCode(),
                player.getRunnerUid()));
        this.setOnKeyReleased(event -> controller.keyReleasedEvent(event.getCode(),
                player.getRunnerUid()));
        this.requestFocus();
    }

    /**
     * Delete an object that doesn't need to be draw anymore.
     * @param uid uid of object
     */
    public void delete(Integer uid) {
        AbstractBodyView removed = bodyViews.remove(uid);
        if (removed != null) {
            bodyViewsSorted.remove(removed);
        }
    }
}
