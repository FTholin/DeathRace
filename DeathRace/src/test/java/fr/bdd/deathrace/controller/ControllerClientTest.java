package fr.bdd.deathrace.controller;

import fr.bdd.deathrace.model.Lobby;
import fr.bdd.deathrace.model.Race;
import fr.bdd.deathrace.model.body.AbstractMobileBody;
import fr.bdd.deathrace.network.Connection;
import fr.bdd.deathrace.network.protocol.AnswerRequest;
import fr.bdd.deathrace.network.protocol.RaceUpdate;
import fr.bdd.deathrace.network.protocol.Request;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

class ConnectionMoke extends Connection {

    private Object sended;

    /**
     * Constructor.
     */
    public ConnectionMoke() {
        super(null, null, null);
        sended = null;
    }

    @Override
    public boolean send(Object object) {
        sended = object;
        return true;
    }

    public Object getSended() {
        return sended;
    }
}

class RaceMoke extends Race {
    private boolean updated;

    /**
     * Construct a race base on the lobby.
     *
     * @param lobby lobby
     */
    public RaceMoke(Lobby lobby) {
        super(lobby);
        updated = false;
    }

    @Override
    public void updateMobileBodies(Map<Integer, AbstractMobileBody> newMobileBodies) {
        updated = true;
    }

    public boolean isUpdated() {
        return updated;
    }
}

public class ControllerClientTest {
    private ControllerClient controllerClient;
    private ConnectionMoke connection;

    private static final int PORT = 9001;

    @Before
    public void setup() {
        controllerClient = new ControllerClient(Controller.getInstance());
        connection = new ConnectionMoke();
    }

    @After
    public void clean() {
    }

    @Test(expected = ExceptionInInitializerError.class)
    public void handleInitRaceRaceUpdateTest() {
        Controller.getInstance().createLobby(1);
        Controller.getInstance().createRace();
        controllerClient.handle(
                new RaceUpdate(Controller.getInstance().getControllerModel().getRace()),
                null);
        // try to show race but java fx not initialized
    }

    @Test
    public void handleUpdatePositionsRaceUpdateTest() {
        Controller controller = Controller.getInstance();
        controller.createLobby(1);
        RaceMoke race = new RaceMoke(controller.getControllerModel().getLobby());
        controller.getControllerModel().setRace(
                race);

        Map<Integer, AbstractMobileBody> positions = new HashMap<>();

        controllerClient.handle(new RaceUpdate(positions),null);

        assertTrue(race.isUpdated());

    }

    @Test
    public void handlePseudoRequestTest() {
        Controller.getInstance().getPlayer().setPseudo("test");
        controllerClient.handle(Request.PSEUDO, connection);

        Object sended = connection.getSended();
        assertNotNull(sended);

        assertTrue(sended instanceof AnswerRequest);
        AnswerRequest answerRequest = (AnswerRequest) sended;

        assertEquals(Request.PSEUDO, answerRequest.getRequest());
        assertEquals("test", answerRequest.getResponse());
    }

    @Test
    public void handleNotYetImplementedRequestTest() {
        Controller.getInstance().getPlayer().setPseudo("test");
        controllerClient.handle(Request.PASSWORD, connection);

        Object sended = connection.getSended();
        assertNull(sended);
    }

    @Test
    public void handleError() {
        try {
            controllerClient.handleError(null);
            assert(false);
        } catch (ExceptionInInitializerError | NoClassDefFoundError e) {
            // going to main view but javafx not initialized
            assert(true);
        }

    }
}