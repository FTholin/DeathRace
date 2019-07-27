package fr.bdd.deathrace.controller;

import fr.bdd.deathrace.model.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class ControllerTest {

    @Before
    public void setup() {
        Controller.getInstance().clearController();
    }

    @After
    public void cleanup() {
        Controller.getInstance().clearController();
    }

    @Test
    public void getInstance() {
        assertNotNull(Controller.getInstance());
    }

    @Test
    public void keyPressedEvent() {
    }

    @Test
    public void keyReleasedEvent() {
    }

    @Test
    public void setControllerClient() {
        Controller controller = Controller.getInstance();
        ControllerClient controllerClient = new ControllerClient(controller);
        controller.setControllerClient(controllerClient);

        assertEquals(controllerClient, controller.getControllerClient());

        ControllerClient controllerClient2 = new ControllerClient(controller);
        controller.setControllerClient(controllerClient2);
        assertEquals(controllerClient2, controller.getControllerClient());


        ControllerServer controllerServer = new ControllerServer(8000, controller, 2);
        controller.setControllerServer(controllerServer);
        assertNull(controller.getControllerClient());

        ControllerClient controllerClient3 = new ControllerClient(controller);
        controller.setControllerClient(controllerClient3);
        assertEquals(controllerClient3, controller.getControllerClient());

        controller.disconnectClient();
        controller.disconnectServer();
    }

    @Test
    public void setControllerServer() {
        Controller controller = Controller.getInstance();
        ControllerServer controllerServer = new ControllerServer(8000, controller, 2);
        controller.setControllerServer(controllerServer);

        assertEquals(controllerServer, controller.getControllerServer());

        ControllerServer controllerServer2 = new ControllerServer(8000, controller, 2);
        controller.setControllerServer(controllerServer2);
        assertEquals(controllerServer2, controller.getControllerServer());


        ControllerClient controllerClient = new ControllerClient(controller);
        controller.setControllerClient(controllerClient);
        assertNull(controller.getControllerServer());

        ControllerServer controllerServer3 = new ControllerServer(8000, controller, 2);
        controller.setControllerServer(controllerServer3);
        assertEquals(controllerServer3, controller.getControllerServer());

        controller.disconnectClient();
        controller.disconnectServer();
    }

    @Test
    public void setControllerModel() {
        Controller controller = Controller.getInstance();
        controller.setControllerModel(new ControllerModel());
    }

    @Test
    public void disconnectClient() {
        Controller controller = Controller.getInstance();
        ControllerClient controllerClient = new ControllerClient(controller);
        controller.setControllerClient(controllerClient);
        assertEquals(controllerClient, controller.getControllerClient());

        controller.disconnectClient();

        assertNull(controller.getControllerClient());

    }

    @Test
    public void disconnectServer() {
        Controller controller = Controller.getInstance();
        ControllerServer controllerServer = new ControllerServer(8000, controller, 2);
        controller.setControllerServer(controllerServer);
        assertEquals(controllerServer, controller.getControllerServer());

        controller.disconnectServer();

        assertNull(controller.getControllerServer());
    }

    @Test
    public void hostRace() {
        Controller.getInstance().hostRace(5);
        assertNotNull(Controller.getInstance().getControllerServer());
        assertNotNull(Controller.getInstance().getControllerModel().getLobby());

        disconnectServer();
    }

    @Test
    public void joinRace() {
        assertFalse(Controller.getInstance().joinRace("0", 0));
    }

    @Test
    public void clearController() {
        hostRace();

        assertNotNull(Controller.getInstance().getControllerModel().getLobby());
        Controller.getInstance().clearController();
        assertNull(Controller.getInstance().getControllerModel().getLobby());
    }

    @Test
    public void createRaceLobbyNull() {
        assertNull(Controller.getInstance().getControllerModel().getLobby());
        Controller.getInstance().createRace();
        assertNull(Controller.getInstance().getControllerModel().getLobby());
        assertNull(Controller.getInstance().getControllerModel().getRace());
    }

    @Test
    public void createRaceLobbyNotNull() {
        Controller.getInstance().createLobby(5);
        assertNotNull(Controller.getInstance().getControllerModel().getLobby());
        try {
            Controller.getInstance().createRace();
        } catch (ExceptionInInitializerError | NoClassDefFoundError e) {
            // javafx null
        }
        assertNotNull(Controller.getInstance().getControllerModel().getLobby());
        assertNotNull(Controller.getInstance().getControllerModel().getRace());
    }

    @Test
    public void deletePlayer() {
        Controller.getInstance().deletePlayer(null);
    }

    @Test
    public void goToMainView() {
        try {
            Controller.getInstance().goToMainView();
        } catch (ExceptionInInitializerError | NoClassDefFoundError e) {
            // javafx null
        }
    }

    @Test
    public void player() {
        Player player1 = Controller.getInstance().getPlayer();
        assertNotNull(player1);
        Controller.getInstance().setPlayer(new Player());
        Player player2 = Controller.getInstance().getPlayer();
        assertNotNull(player2);
        assertNotEquals(player1, player2);
    }
}