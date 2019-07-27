package fr.bdd.deathrace.view.interfaces;

import fr.bdd.deathrace.controller.Controller;
import fr.bdd.deathrace.model.Player;
import fr.bdd.deathrace.model.Race;
import fr.bdd.deathrace.view.IUpdatable;
import fr.bdd.deathrace.view.RaceCanvasView;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class RaceView extends BorderPane implements IUpdatable {
    private Race race;
    private Canvas starterCanvas;
    private long initTime;

    private RaceCanvasView raceCanvasView;

    /**
     * Race view constructor.
     *
     * @param controller controller
     * @param race       race
     * @param width      width
     * @param height     height
     * @param player     main player
     */
    public RaceView(Controller controller, Race race, double width, double height,
                          Player player) {
        super();
        this.raceCanvasView = new RaceCanvasView(controller, race, width, height, player);

        this.starterCanvas = new Canvas(width, height);
        this.race = race;
        super.getChildren().add(starterCanvas);
        initTime = System.currentTimeMillis();


        //
    }

    public RaceCanvasView getRaceCanvasView() {
        return raceCanvasView;
    }


    @Override
    public void update() {
        if (race.isStarted()) {
            raceCanvasView.update();
        } else {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - initTime;

            if (elapsedTime > 3000) {
                race.setStarted(true);
                super.getChildren().remove(0);
                super.getChildren().add(raceCanvasView);
                update();
            } else {
                GraphicsContext gc = starterCanvas.getGraphicsContext2D();

                // background
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, starterCanvas.getWidth(), starterCanvas.getHeight());

                gc.setFill(Color.WHITE);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.setTextBaseline(VPos.CENTER);
                gc.setFont(new Font("Arial", 100));
                gc.fillText(
                        String.valueOf(3 - (elapsedTime / 1000)),
                        Math.round(starterCanvas.getWidth()  / 2),
                        Math.round(starterCanvas.getHeight() / 2)
                );

                gc.fillText(
                        "La partie commence dans",
                        Math.round(starterCanvas.getWidth()  / 2),
                        Math.round(starterCanvas.getHeight() / 2) - 300
                );
            }
        }

    }
}
