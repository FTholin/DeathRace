package fr.bdd.deathrace.network.protocol;

public enum LobbyOperation {
    ADD, // add new player
    UPDATE_MAP, // update map
    UPDATE_PLAYER, // update player
    DELETE, // delete player
    INIT_PLAYER, //
    LOBBY_COPY,
}
