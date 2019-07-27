package fr.bdd.deathrace.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * LobbyView.
 */
public class Lobby implements Serializable {

    private List<Player> players;
    private int maxNbPlayers;
    private String mapName;

    /**
     * Construct a lobby associated with at least one player (the master)
     * and the max number of player allowed.
     * @param master player that own the lobby
     * @param maxNbPlayers max number of player
     */
    public Lobby(Player master, int maxNbPlayers) {
        this.maxNbPlayers = maxNbPlayers;
        this.players = new ArrayList<>();
        this.players.add(master);
    }

    /**
     * Create player if not exceed max number of player.
     * @return player
     */
    public Optional<Player> createPlayer() {
        if (players.size() == maxNbPlayers) {
            return Optional.empty();
        }
        Player player = new Player();
        players.add(player);
        return Optional.of(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public int getMaxNbPlayers() {
        return maxNbPlayers;
    }

    public void addPlayer(Player playerSent) {
        players.add(playerSent);
    }

    public void deletePlayer(Player playerSent) {
        players.remove(playerSent);
    }

    /**
     * Get the player corresponding to the sent one.
     * @param playerSent player sent
     * @return player corresponding to the sent one
     */
    public Player getPlayer(Player playerSent) {
        for (Player player : players) {
            if (player.equals(playerSent)) {
                return player;
            }
        }
        return null;
    }
}
