package fr.bdd.deathrace.controller;

import fr.bdd.deathrace.model.Player;
import fr.bdd.deathrace.network.Connection;
import fr.bdd.deathrace.network.protocol.AnswerRequest;
import fr.bdd.deathrace.network.protocol.KeyEvent;
import fr.bdd.deathrace.network.protocol.LobbyOperation;
import fr.bdd.deathrace.network.protocol.LobbyUpdate;
import fr.bdd.deathrace.network.protocol.RaceUpdate;
import fr.bdd.deathrace.network.protocol.Request;
import fr.bdd.deathrace.network.protocol.StatusEvent;
import fr.bdd.deathrace.network.server.AbstractServer;
import fr.bdd.deathrace.view.IUpdatable;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static fr.bdd.deathrace.network.protocol.LobbyOperation.UPDATE_PLAYER;

public class ControllerServer extends AbstractServer implements IUpdatable {
    private static final Logger logger = Logger.getLogger(ControllerServer.class);

    private Controller controller;
    private ControllerModel controllerModel;
    private Map<Connection, Player> associateConnectionToPlayer;
    private int nbPlayers;
    private boolean sendRaceUpdate;

    /**
     * Constructor from main controller.
     *
     * @param port       default can be 9001
     * @param controller main controller
     */
    public ControllerServer(int port, Controller controller, int nbPlayers) {
        super(port);
        this.controller = controller;
        this.controllerModel = controller.getControllerModel();
        this.associateConnectionToPlayer = new HashMap<>();
        this.nbPlayers = nbPlayers;
        this.sendRaceUpdate = false;
    }

    @Override
    public synchronized void addConnection(Connection connection) {
        Optional<Player> player = controllerModel.getLobby().createPlayer();
        if (!player.isPresent()) {
            super.stopConnectionListener();
        } else {
            super.addConnection(connection);
            // send to all other player the new player
            sendToAllExceptProvider(new LobbyUpdate(player.get(), LobbyOperation.ADD), connection);
            this.associateConnectionToPlayer.put(connection, player.get());

            // send created player too match id
            connection.send(new LobbyUpdate(player.get(), LobbyOperation.INIT_PLAYER));
            // ask pseudo to player
            connection.send(Request.PSEUDO);
            // send current lobby to player
            connection.send(new LobbyUpdate(controllerModel.getLobby()));
        }
    }

    @Override
    public synchronized void close(Connection connection) {
        super.close(connection);
        if (associateConnectionToPlayer.containsKey(connection)) {
            Player playerRemoved = associateConnectionToPlayer.remove(connection);
            // TODO authorize new connection only in lobby
            if (associateConnectionToPlayer.size() < nbPlayers) {
                createConnectionListener();
            }

            controllerModel.deletePlayer(playerRemoved);
            sendToAll(new LobbyUpdate(playerRemoved, LobbyOperation.DELETE));
        }
    }

    @Override
    public void handle(Object message, Connection connection) {
        if (message instanceof AnswerRequest) {
            handleAnswerRequest((AnswerRequest) message, connection);
        } else if (message instanceof KeyEvent) {
            handleKeyEvent((KeyEvent) message, connection);
        } else if (message instanceof LobbyUpdate) {
            handleLobbyUpdate((LobbyUpdate) message, connection);
        } else if (message instanceof StatusEvent) {
            StatusEvent statusEvent = (StatusEvent) message;
            logger.debug("Status event received : " + statusEvent);

            if (statusEvent == StatusEvent.DISCONNECTION) {
                close(connection);
            } else {
                logger.warn("Status event not handled : " + statusEvent);
            }
        }
    }

    /**
     * Handle key event.
     *
     * @param keyEvent   key event
     * @param connection connection that sent it
     */
    private void handleKeyEvent(KeyEvent keyEvent, Connection connection) {
        logger.debug("Key event received : " + (keyEvent.isPressed() ? "pressed " : "released ")
                + keyEvent.getKeyCode());
        if (keyEvent.isPressed()) {
            controller.keyPressedEvent(keyEvent.getKeyCode(),
                    associateConnectionToPlayer.get(connection).getRunnerUid());
        } else {
            controller.keyReleasedEvent(keyEvent.getKeyCode(),
                    associateConnectionToPlayer.get(connection).getRunnerUid());
        }
    }

    /**
     * Handle a lobby update.
     *
     * @param lobbyUpdate lobby update
     * @param connection  connection that sent it
     */
    private void handleLobbyUpdate(LobbyUpdate lobbyUpdate, Connection connection) {
        logger.debug("Lobby update received : " + lobbyUpdate.getLobbyOperation());
        if (UPDATE_PLAYER == lobbyUpdate.getLobbyOperation()) {
            // client update itself
            Player playerToUpdate = associateConnectionToPlayer.get(connection);
            playerToUpdate.setPseudo(lobbyUpdate.getPlayer().getPseudo());
            playerToUpdate.setType(lobbyUpdate.getPlayer().getType());
            playerToUpdate.setReady(lobbyUpdate.getPlayer().isReady());
            // send update to all other player
            sendToAllExceptProvider(new LobbyUpdate(playerToUpdate,
                    LobbyOperation.UPDATE_PLAYER), connection);
        } else {
            logger.warn("Ignore lobby update : " + lobbyUpdate.getLobbyOperation());
        }
    }

    /**
     * Handle answer to server request.
     *
     * @param answerRequest answer to server request
     * @param connection    connection that answer
     */
    private void handleAnswerRequest(AnswerRequest answerRequest, Connection connection) {
        logger.debug("Answer request received : " + answerRequest.getRequest());
        switch (answerRequest.getRequest()) {
            case PSEUDO:
                String pseudo = answerRequest.getResponse().toString();
                Player playerToUpdate = associateConnectionToPlayer.get(connection);
                playerToUpdate.setPseudo(pseudo);
                sendToAllExceptProvider(new LobbyUpdate(playerToUpdate,
                        LobbyOperation.UPDATE_PLAYER), connection);
                break;
            default:
                logger.warn("Ignore answer request : " + answerRequest.getRequest());
                break;
        }
    }

    @Override
    public void handleError(Connection connection) {
        super.close(connection);
    }

    @Override
    public synchronized void update() {
        if (sendRaceUpdate && controllerModel.getRace() != null) {
            sendToAll(new RaceUpdate(controllerModel.getRace().getMobileBodies()));
        }
    }

    /**
     * Remove player and close associated connexion.
     * @param player player to remove
     */
    public synchronized void removePlayer(Player player) {
        for (Map.Entry<Connection, Player> association : associateConnectionToPlayer.entrySet()) {
            if (association.getValue().equals(player)) {
                close(association.getKey());
                break;
            }
        }
    }

    public void sendRaceAndFuturUpdates() {
        sendToAll(new RaceUpdate(controllerModel.getRace()));
        this.sendRaceUpdate = true;
    }
}
