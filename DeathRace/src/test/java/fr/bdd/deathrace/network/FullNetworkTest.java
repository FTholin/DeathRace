package fr.bdd.deathrace.network;

import fr.bdd.deathrace.network.protocol.KeyEvent;
import fr.bdd.deathrace.network.server.ConnectionListener;
import javafx.scene.input.KeyCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FullNetworkTest {
    static private AbstractServerTemp server;
    static private AbstractClientTemp client;

    @Before
    public void setup() {
        int port = 8083;
        server = new AbstractServerTemp(port);
        assertNotNull(server.getServerSocket());
        assertFalse(server.getServerSocket().isClosed());
        client = new AbstractClientTemp();
        if ((!client.connect("localhost", port))) throw new AssertionError();
    }

    @After
    public void closeAll() {
        server.closeAllConnections();
        if (client != null) {
            client.close();
        }
        server.stopConnectionListener();
        if (server != null) {
            server.stopServer();
        }
    }

    @Test
    public void sendTest() {
        assertNotNull(server);
        client.send(new KeyEvent(true, KeyCode.UP));
        await().atMost(5, TimeUnit.SECONDS).until(() ->
                server.isMessageHandled());
        server.sendToAll(new KeyEvent(false, KeyCode.UP));
        await().atMost(5, TimeUnit.SECONDS).until(() ->
                client.isMessageHandled());
    }

    @Test
    public void closeOneConnectionServerSideTest() {
        server.close(client.getConnection());
        await().atMost(5, TimeUnit.SECONDS).until(() ->
                !server.getConnections().contains(client.getConnection()));
    }

    @Test
    public void closeOneConnectionClientSideTest() {
        client.close();
        await().atMost(5, TimeUnit.SECONDS).until(() ->
                !server.getConnections().contains(client.getConnection()));
        assertFalse(client.isConnected());
        assertNull(client.getConnection());
    }

    @Test
    public void DoubleCloseOneConnectionTest() {
        server.close(client.getConnection());
        server.close(client.getConnection());
        await().atMost(5, TimeUnit.SECONDS).until(() ->
                !server.getConnections().contains(client.getConnection()));
    }

    @Test
    public void DoubleConnectOneConnectionTest() {
        client.connect("localhost", 1111);
        client.connect("localhost", 1111);
        await().atMost(5, TimeUnit.SECONDS).until(() ->
                !server.getConnections().contains(client.getConnection()));
    }

    @Test
    public void getObjectOutputStreamTest() {
        assertNotNull(client.getConnection().getObjectOutputStream());
    }

    @Test
    public void getMessageListenerTest() {
        assertNotNull(client.getConnection().getMessageListener());
    }

    @Test
    public void getSocketTest() {
        assertNotNull(client.getConnection().getSocket());
    }

    @Test
    public void connectionListenerRunTest() {
        ConnectionListener connectionListener = new ConnectionListener(null);
        assert(connectionListener.isRunning());
    }
}