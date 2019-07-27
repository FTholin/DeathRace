package fr.bdd.deathrace.controller;

import fr.bdd.deathrace.model.Lobby;
import fr.bdd.deathrace.model.Player;
import fr.bdd.deathrace.model.Race;
import fr.bdd.deathrace.view.RaceCanvasView;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class ControllerModelTest {
    private ControllerModel controllerModel;

    @Before
    public void setup() {
        controllerModel = new ControllerModel();
    }

    @Test
    public void createLobby() {
        int maxNbPlayers = 5;
        Player player = new Player();
        controllerModel.createLobby(player, maxNbPlayers);

        Lobby lobby = controllerModel.getLobby();
        assertNotNull(lobby);

        assertEquals(maxNbPlayers, lobby.getMaxNbPlayers());

        List<Player> playerList = lobby.getPlayers();

        assertEquals(1, playerList.size());
        assertEquals(player, playerList.get(0));
    }

    @Test
    public void deletePlayer() {
        Player player = new Player();
        controllerModel.createLobby(player, 5);
        controllerModel.createRace();
        List<Player> playerList = controllerModel.getLobby().getPlayers();

        assertEquals(1, playerList.size());
        assertEquals(player, playerList.get(0));

        controllerModel.deletePlayer(player);

        assertTrue(playerList.isEmpty());
    }

    @Test
    public void createRaceLobbyNull() {
        assertFalse(controllerModel.createRace());
        assertNull(controllerModel.getRace());
    }

    @Test
    public void createRaceLobbyNotNull() {
        Player player = new Player();
        controllerModel.createLobby(player, 5);
        assertTrue(controllerModel.createRace());

        Race race = controllerModel.getRace();
        assertNotNull(race);
        assertEquals(1, race.getRunners().size());
        assertEquals(player.getRunnerUid(), race.getRunners().keySet().iterator().next(), 0);
    }

    @Test
    public void setRace() {
        Player player = new Player();
        controllerModel.createLobby(player, 5);
        assertTrue(controllerModel.createRace());

        Race race = controllerModel.getRace();
        assertNotNull(race);

        controllerModel.setRace(null);
        assertNull(controllerModel.getRace());

        assertNotEquals(race, controllerModel.getRace());
    }


    @Test
    public void keyPressedEvent() {
    }

    @Test
    public void keyReleasedEvent() {
    }


    @Test
    public void setLobby() {
        Player player = new Player();
        controllerModel.createLobby(player, 5);

        Lobby lobby = controllerModel.getLobby();
        assertNotNull(lobby);

        controllerModel.setLobby(null);
        assertNull(controllerModel.getLobby());

        assertNotEquals(lobby, controllerModel.getLobby());
    }

    @Test
    public void setRaceCanvasView() {

        RaceCanvasView raceCanvasView = controllerModel.getRaceCanvasView();
        assertNull(raceCanvasView);

        controllerModel.setRaceCanvasView(null);
        assertNull(controllerModel.getRaceCanvasView());

        assertEquals(raceCanvasView, controllerModel.getRaceCanvasView());
    }
}