package fr.bdd.deathrace.view;

import fr.bdd.deathrace.view.interfaces.MainMenuView;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main window, must be used to show view.
 */
public class MainView {

    private static MainView instance;

    /**
     * Get lone instance of main view.
     * @return lone instance
     */
    public static MainView getInstance() {
        if (instance == null) {
            instance = new MainView();
            instance.show(new MainMenuView()); // default screen
        }
        return instance;
    }

    private Stage stage;

    private MainView() {
        stage = new Stage();
        stage.setTitle("DeathRace");
        stage.setMaximized(true);
    }

    /**
     * Show the interface.
     * @param parent interface
     */
    public void show(Parent parent) {
        Scene scene;
        if (stage.getScene() != null) {
            if (stage.getScene().getRoot() instanceof IUpdatable) {
                UpdateTimer.getInstance().unregister((IUpdatable) stage.getScene().getRoot());
            }
            scene = new Scene(parent, stage.getScene().getWidth(), stage.getScene().getHeight());
        } else {
            scene = new Scene(parent);
        }

        Platform.runLater(() -> {
            stage.setScene(scene);
            stage.show();
        });


        if (parent instanceof IUpdatable) {
            UpdateTimer.getInstance().register((IUpdatable) parent);
        }
    }

    public double getWidth() {
        return stage.getScene().getWidth();
    }

    public double getHeight() {
        return stage.getScene().getHeight();
    }
}
