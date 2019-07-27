package fr.bdd.deathrace.network;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class ConnectionTest {

    @Test
    public void sendToNullConnectionTest() {
        Connection connection = new Connection(null, null, null);

        assertFalse(connection.send(null));
    }

    @Test
    public void closeAlreadyClosedConnectionTest() {
        AbstractServerTemp server = new AbstractServerTemp(9001);
        AbstractClientTemp client = new AbstractClientTemp();

        client.connect("127.0.0.1", 9001);

        Connection connection = client.getConnection();
        connection.close();
        connection.close();
    }

}