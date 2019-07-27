package fr.bdd.deathrace.controller;

import fr.bdd.deathrace.model.Lobby;
import fr.bdd.deathrace.model.Player;
import fr.bdd.deathrace.model.Race;
import fr.bdd.deathrace.model.body.AbstractMobileBody;
import fr.bdd.deathrace.network.Connection;
import fr.bdd.deathrace.network.client.AbstractClient;
import fr.bdd.deathrace.network.protocol.AnswerRequest;
import fr.bdd.deathrace.network.protocol.LobbyUpdate;
import fr.bdd.deathrace.network.protocol.RaceUpdate;
import fr.bdd.deathrace.network.protocol.Request;
import fr.bdd.deathrace.network.protocol.StatusEvent;
import org.apache.log4j.Logger;

import java.util.Map;

public class ControllerClient extends AbstractClient {
    private static final Logger logger = Logger.getLogger(ControllerClient.class);

    private Controller controller;
    private ControllerModel controllerModel;

    public ControllerClient(Controller controller) {
        this.controller = controller;
        this.controllerModel = controller.getControllerModel();
    }

    @Override
    public void handle(Object message, Connection connection) {
        if (message instanceof Request) {
            handleRequest((Request) message, connection);
        } else if (message instanceof RaceUpdate) {
            handleRaceUpdate((RaceUpdate) message);
        } else if (message instanceof LobbyUpdate) {
            handleLobbyUpdate((LobbyUpdate) message);
        } else if (message instanceof StatusEvent) {
            StatusEvent statusEvent = (StatusEvent) message;
            logger.debug("Status event received : " + statusEvent);

            if (statusEvent == StatusEvent.DISCONNECTION) {
                controller.goToMainView();
            } else {
                logger.warn("Status event not handled : " + statusEvent);
            }
        }
    }

    /**
     * Handle race update.
     *
     * @param raceUpdate race update
     */
    private void handleRaceUpdate(RaceUpdate raceUpdate) {
        Race race = raceUpdate.getRace();
        Map<Integer, AbstractMobileBody> positions = raceUpdate.getPositions();

        logger.debug("Race update received : " + raceUpdate.getOperation());

        switch (raceUpdate.getOperation()) {
            case INIT_RACE:
                controller.showRace(race, controller.getPlayer());
                break;
            case UPDATE_POSITION:
                controllerModel.getRace().updateMobileBodies(positions);
                break;
            default:
                logger.warn("Ignore race update : " + raceUpdate.getOperation());
                break;
        }
    }

    /**
     * Handle lobby update.
     *
     * @param lobbyUpdate lobby update
     */
    private void handleLobbyUpdate(LobbyUpdate lobbyUpdate) {
        Player playerSent = lobbyUpdate.getPlayer();
        String map = lobbyUpdate.getMap();
        Lobby lobby = lobbyUpdate.getLobby();

        logger.debug("Lobby update received : " + lobbyUpdate.getLobbyOperation());

        switch (lobbyUpdate.getLobbyOperation()) {
            case ADD:
                // new player to add to lobby
                controllerModel.getLobby().addPlayer(playerSent);
                break;
            case DELETE:
                // delete a player in lobby
                controllerModel.deletePlayer(playerSent);
                // you have been kick
                if (playerSent.equals(controller.getPlayer())) {
                    controller.goToMainView();
                }
                break;
            case UPDATE_PLAYER:
                // update your content
                Player playerToUpdate = controllerModel.getLobby().getPlayer(playerSent);
                if (playerToUpdate != null) {
                    if (playerToUpdate.equals(controller.getPlayer())) {
                        playerToUpdate.updateExceptPseudo(playerSent);
                    } else {
                        playerToUpdate.updateExceptPseudo(playerSent);
                        playerToUpdate.setPseudo(playerSent.getPseudo());
                    }
                } else {
                    logger.warn("Try to update player that doesn't exists.");
                }
                break;
            case UPDATE_MAP:
                controllerModel.getLobby().setMapName(map);
                break;
            case INIT_PLAYER:
                playerSent.setPseudo(Controller.getInstance().getPlayer().getPseudo());
                controller.setPlayer(playerSent);
                break;
            case LOBBY_COPY:
                controllerModel.setLobby(lobby);
                Player current = Controller.getInstance().getPlayer();
                for (Player player : lobby.getPlayers()) {
                    if (player.equals(current)) {
                        player.setPseudo(current.getPseudo());
                        Controller.getInstance().setPlayer(player);
                        break;
                    }
                }
                break;
            default:
                logger.warn("Ignore lobby update : " + lobbyUpdate.getLobbyOperation());
                break;
        }
    }

    /**
     * Handle request.
     *
     * @param request    request
     * @param connection connection
     */
    private void handleRequest(Request request, Connection connection) {
        logger.debug("Request received : " + request);
        switch (request) {
            case PSEUDO:
                connection.send(new AnswerRequest(request, controller.getPlayer().getPseudo()));
                break;
            default:
                logger.warn("Request not handled : " + request);
                break;
        }
    }

    @Override
    public void handleError(Connection connection) {
        logger.debug("Error with connection. Close socket and go to main view.");
        super.close();
        controller.goToMainView();
    }
}
