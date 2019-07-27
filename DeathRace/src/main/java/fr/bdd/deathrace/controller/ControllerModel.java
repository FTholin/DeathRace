package fr.bdd.deathrace.controller;

import fr.bdd.deathrace.model.Lobby;
import fr.bdd.deathrace.model.ModelTimer;
import fr.bdd.deathrace.model.Player;
import fr.bdd.deathrace.model.Race;
import fr.bdd.deathrace.view.RaceCanvasView;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

public class ControllerModel {
    private Race race;
    private Lobby lobby;
    private RaceCanvasView raceCanvasView;
    private List<RunnerBot> listBot;

    /**
     * Constructor.
     */
    public ControllerModel() {
        this.race = null;
        this.lobby = null;
        this.raceCanvasView = null;
        this.listBot = new ArrayList<>();
    }

    public void createLobby(Player player, int nbPlayer) {
        this.setLobby(new Lobby(player, nbPlayer));
    }

    /**
     * Delete player in lobby and race if exist.
     *
     * @param player player to delete
     */
    public void deletePlayer(Player player) {
        if (lobby != null) {
            lobby.deletePlayer(player);
        }
        if (race != null) {
            race.removeObject(player.getRunnerUid());
        }
        if (raceCanvasView != null) {
            raceCanvasView.delete(player.getRunnerUid());
        }
    }

    /**
     * Create a race if lobby is set.
     *
     * @return true if race created
     */
    public boolean createRace() {
        if (this.lobby == null) {
            return false;
        }
        Race newRace = new Race(lobby);
        for (Player player : lobby.getPlayers()) {
            if (player.isBot()) {
                RunnerBot runnerBot = new RunnerBot(newRace.getWorld(), player, 10);
                listBot.add(runnerBot);
                ModelTimer.register(runnerBot);
            }
        }
        this.setRace(newRace);
        return true;
    }

    /**
     * Keys pressed event handler.
     *
     * @param keyCode key pressed
     * @param uid     UID of the runner that we want to move
     */
    public void keyPressedEvent(KeyCode keyCode, int uid) {
        switch (keyCode) {
            case RIGHT:
                race.getRunner(uid).keyPressedEvent(InputEvent.RIGHT);
                break;
            case LEFT:
                race.getRunner(uid).keyPressedEvent(InputEvent.LEFT);
                break;
            case UP:
                race.getRunner(uid).keyPressedEvent(InputEvent.UP);
                break;
            case DOWN:
                race.getRunner(uid).keyPressedEvent(InputEvent.DOWN);
                break;
            default:
                break;
        }
    }

    /**
     * Keys released event handler.
     *
     * @param keyCode key released
     * @param uid     UID of the runner that we want to move
     */
    public void keyReleasedEvent(KeyCode keyCode, int uid) {
        switch (keyCode) {
            case RIGHT:
                race.getRunner(uid).keyReleasedEvent(InputEvent.RIGHT);
                break;
            case LEFT:
                race.getRunner(uid).keyReleasedEvent(InputEvent.LEFT);
                break;
            case UP:
                race.getRunner(uid).keyReleasedEvent(InputEvent.UP);
                break;
            case DOWN:
                race.getRunner(uid).keyReleasedEvent(InputEvent.DOWN);
                break;
            default:
                break;
        }
    }

    /**
     * Set the new race, unregister the former one if exists.
     *
     * @param race new race
     */
    public void setRace(Race race) {
        if (this.race != null) {
            ModelTimer.unregister(this.race);
            this.race = null;
        }
        this.race = race;
    }

    public Race getRace() {
        return race;
    }

    /**
     * Set the lobby, destroy current one if exists.
     *
     * @param lobby lobby
     */
    public void setLobby(Lobby lobby) {
        if (this.lobby != null) {
            this.lobby = null;
        }
        this.lobby = lobby;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setRaceCanvasView(RaceCanvasView raceCanvasView) {
        this.raceCanvasView = raceCanvasView;
    }

    public RaceCanvasView getRaceCanvasView() {
        return raceCanvasView;
    }
}
