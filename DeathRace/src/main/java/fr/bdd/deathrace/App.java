package fr.bdd.deathrace;

import fr.bdd.deathrace.controller.Controller;
import fr.bdd.deathrace.model.ModelTimer;
import fr.bdd.deathrace.view.MainView;
import fr.bdd.deathrace.view.UpdateTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public final class App extends Application {

    /**
     * Start main menu.
     * @param stage stage
     * @throws Exception e
     */
    @Override
    public void start(Stage stage) throws Exception {
        System.setProperty("sun.java2d.opengl", "true");
        MainView.getInstance();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        ModelTimer.stop();
        UpdateTimer.getInstance().stop();
        Controller.getInstance().disconnectServer();
        Controller.getInstance().disconnectClient();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
