package fr.bdd.deathrace.controller;

import fr.bdd.deathrace.model.ModelTimer;
import fr.bdd.deathrace.model.Player;
import fr.bdd.deathrace.model.Race;
import fr.bdd.deathrace.network.protocol.KeyEvent;
import fr.bdd.deathrace.network.protocol.LobbyUpdate;
import fr.bdd.deathrace.network.protocol.StatusEvent;
import fr.bdd.deathrace.view.MainView;
import fr.bdd.deathrace.view.interfaces.MainMenuView;
import fr.bdd.deathrace.view.interfaces.RaceView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;

import java.util.Optional;

/**
 * Lone instance of controller.
 */
public class Controller {

    private static Controller instance;

    /**
     * Get lone instance of controller. Create if first call.
     *
     * @return controller
     */
    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    private ControllerClient controllerClient;
    private ControllerServer controllerServer;
    private ControllerModel controllerModel;
    private Player player;

    private Controller() {
        this.controllerClient = null;
        this.controllerServer = null;
        this.controllerModel = new ControllerModel();
        this.player = new Player();
    }

    /**
     * Keys pressed event handler.
     *
     * @param keyCode key pressed
     * @param i       ID of the runner that we want to move
     */
    public void keyPressedEvent(KeyCode keyCode, int i) {
        if (keyCode == KeyCode.ESCAPE) {
            showDialogueEchap();
        } else {
            if (controllerClient != null) {
                controllerClient.send(new KeyEvent(true, keyCode));
            } else {
                controllerModel.keyPressedEvent(keyCode, i);
            }
        }
    }

    /**
     * Keys released event handler.
     *
     * @param keyCode key pressed
     * @param i       ID of the runner that we want to move
     */
    public void keyReleasedEvent(KeyCode keyCode, int i) {
        if (controllerClient != null) {
            controllerClient.send(new KeyEvent(false, keyCode));
        } else {
            controllerModel.keyReleasedEvent(keyCode, i);
        }
    }

    /**
     * Set new client controller, removePlayer former network controller if exists.
     *
     * @param controllerClient new client controller
     */
    public void setControllerClient(ControllerClient controllerClient) {
        disconnectClient();
        disconnectServer();

        this.controllerClient = controllerClient;
    }

    public ControllerClient getControllerClient() {
        return controllerClient;
    }

    /**
     * Set new server controller, removePlayer former network controller if exists.
     *
     * @param controllerServer new server controller
     */
    public void setControllerServer(ControllerServer controllerServer) {
        disconnectClient();
        disconnectServer();

        this.controllerServer = controllerServer;
    }

    public ControllerServer getControllerServer() {
        return controllerServer;
    }

    public ControllerModel getControllerModel() {
        return controllerModel;
    }

    public void setControllerModel(ControllerModel controllerModel) {
        this.controllerModel = controllerModel;
    }

    /**
     * Close client controller if exists.
     */
    public void disconnectClient() {
        if (this.controllerClient != null) {
            this.controllerClient.send(StatusEvent.DISCONNECTION);
            this.controllerClient.close();
            this.controllerClient = null;
        }
    }

    /**
     * Close server controller if exists.
     */
    public void disconnectServer() {
        if (this.controllerServer != null) {
            this.controllerServer.sendToAll(StatusEvent.DISCONNECTION);
            this.controllerServer.stopServer();
            this.controllerServer = null;
        }
    }

    /**
     * Host a multiplayer race.
     */
    public void hostRace(int nbPlayers) {
        ControllerServer server = new ControllerServer(9001, this, nbPlayers);
        this.setControllerServer(server);
        ModelTimer.register(server);
        createLobby(nbPlayers);
    }

    /**
     * Try join a race.
     *
     * @param ip   ip
     * @param port port
     * @return true if succeed
     */
    public boolean joinRace(String ip, int port) {
        ControllerClient newControllerClient = new ControllerClient(this);
        this.setControllerClient(newControllerClient);
        return controllerClient.connect(ip, port);
    }

    /**
     * Create a lobby.
     */
    public void createLobby(int nbPlayer) {
        this.controllerModel.createLobby(player, nbPlayer);
    }

    /**
     * Create a race base on lobby and show it.
     * Send to all client if this is the host.
     */
    public void createRace() {
        if (this.controllerModel.createRace()) {
            this.showRace(this.controllerModel.getRace(), this.player);

            if (this.controllerServer != null) {
                // re send lobby because createRace update players in lobby
                this.controllerServer.sendToAll(new LobbyUpdate(this.controllerModel.getLobby()));
                this.controllerServer.sendRaceAndFuturUpdates();
            }
        }
    }

    /**
     * Delete player.
     * @param player player to delete
     */
    public void deletePlayer(Player player) {
        if (controllerModel != null) {
            controllerModel.deletePlayer(player);
        }
    }

    /**
     * Show the race center on the player.
     *
     * @param race   race
     * @param player player
     */
    public void showRace(Race race, Player player) {
        this.controllerModel.setRace(race);

        MainView mainView = MainView.getInstance();

        RaceView raceView = new RaceView(this, race, mainView.getWidth(),
                mainView.getHeight(), player);
        this.controllerModel.setRaceCanvasView(raceView.getRaceCanvasView());

        mainView.show(raceView);

        ModelTimer.register(race);
    }

    /**
     * Disconnect all networks, clear all datas and show main menu.
     */
    public void goToMainView() {
        clearController();

        MainView.getInstance().show(new MainMenuView());
    }

    /**
     * Clear the controller.
     * Disconnect client and server.
     * Set to null race, lobby and race canvas view.
     */
    public void clearController() {
        disconnectServer();
        disconnectClient();

        controllerModel.setRace(null);
        controllerModel.setLobby(null);
        controllerModel.setRaceCanvasView(null);
    }

    /**
     * Show the echap dialog.
     */
    public void showDialogueEchap() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quitter");
        alert.setHeaderText("Êtes vous sûre de vouloir nous quitter ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Controller.getInstance().goToMainView();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
