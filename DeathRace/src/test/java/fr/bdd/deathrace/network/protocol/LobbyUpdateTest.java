package fr.bdd.deathrace.network.protocol;

import fr.bdd.deathrace.model.Lobby;
import fr.bdd.deathrace.model.Player;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LobbyUpdateTest {

    @Parameterized.Parameters
    public static Iterable<LobbyOperation> data() {
        return Arrays.asList(LobbyOperation.ADD, LobbyOperation.UPDATE_PLAYER, LobbyOperation.DELETE);
    }

    @Test
    public void getPlayer() {
        data().forEach(lobbyOperation -> {
            Player player = new Player();
            LobbyUpdate lobbyUpdate = new LobbyUpdate(player, lobbyOperation);

            assertEquals(player, lobbyUpdate.getPlayer());
            assertNull(lobbyUpdate.getLobby());
            assertNull(lobbyUpdate.getMap());

            assertEquals(lobbyOperation, lobbyUpdate.getLobbyOperation());
        });

    }

    @Test
    public void getMap() {
        String mapName = "map";
        LobbyUpdate lobbyUpdate = new LobbyUpdate(mapName);

        assertEquals(mapName, lobbyUpdate.getMap());
        assertNull(lobbyUpdate.getLobby());
        assertNull(lobbyUpdate.getPlayer());

        assertEquals(LobbyOperation.UPDATE_MAP, lobbyUpdate.getLobbyOperation());
    }

    @Test
    public void getLobby() {
        Lobby lobby = new Lobby(new Player(), 5);
        LobbyUpdate lobbyUpdate = new LobbyUpdate(lobby);

        assertEquals(lobby, lobbyUpdate.getLobby());
        assertNull(lobbyUpdate.getMap());
        assertNull(lobbyUpdate.getPlayer());

        assertEquals(LobbyOperation.LOBBY_COPY, lobbyUpdate.getLobbyOperation());
    }
}