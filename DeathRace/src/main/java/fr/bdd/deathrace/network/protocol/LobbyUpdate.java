package fr.bdd.deathrace.network.protocol;

import fr.bdd.deathrace.model.Lobby;
import fr.bdd.deathrace.model.Player;

import java.io.Serializable;

public class LobbyUpdate implements Serializable {
    private LobbyOperation lobbyOperation;
    private Player player;
    private String map;
    private Lobby lobby;

    public LobbyUpdate(Player player, LobbyOperation lobbyOperation) {
        this.player = player;
        this.lobbyOperation = lobbyOperation;
    }

    public LobbyUpdate(String map) {
        this.map = map;
        this.lobbyOperation = LobbyOperation.UPDATE_MAP;
    }

    public LobbyUpdate(Lobby lobby) {
        this.lobby = lobby;
        this.lobbyOperation = LobbyOperation.LOBBY_COPY;
    }

    public Player getPlayer() {
        return player;
    }

    public String getMap() {
        return map;
    }

    public LobbyOperation getLobbyOperation() {
        return lobbyOperation;
    }

    public Lobby getLobby() {
        return lobby;
    }
}
