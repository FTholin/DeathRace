package fr.bdd.deathrace.network.client;

import fr.bdd.deathrace.network.Connection;
import fr.bdd.deathrace.network.IMessageHandler;
import fr.bdd.deathrace.network.MessageListener;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Client that handle one connection.
 */
public abstract class AbstractClient implements IClient, IMessageHandler {
    private static final Logger logger = Logger.getLogger(AbstractClient.class);

    private Connection connection;

    /**
     * Constructor.
     */
    public AbstractClient() {
        this.connection = null;
    }

    @Override
    public synchronized boolean connect(String ip, int port) {
        if (connection != null) {
            connection.close();
        }

        boolean success = false;
        try {
            Socket socket = new Socket();
            logger.info(String.format("Try connect to %s:%d", ip, port));
            socket.connect(new InetSocketAddress(ip, port), 2000);
            logger.info("Ok connect");
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            logger.info("Ok streams");
            this.connection = new Connection(socket, ois, oos);

            logger.info(String.format("Successful connection to %s:%d", ip, port));

            // create thread that listen messages
            MessageListener listener = new MessageListener(this, this.connection);
            this.connection.setMessageListener(listener);
            new Thread(listener).start();

            success = true;
        } catch (IOException e) {
            logger.error(String.format("Failure connection to %s:%d", ip, port));
        }
        return success;
    }

    @Override
    public boolean isConnected() {
        return this.connection != null && this.connection.getSocket().isConnected();
    }

    @Override
    public void close() {
        if (this.connection != null) {
            this.connection.close();
            this.connection = null;
        }
    }

    @Override
    public boolean send(Object message) {
        boolean succes = false;
        if (this.connection != null) {
            succes = this.connection.send(message);
        }
        return succes;
    }

    public Connection getConnection() {
        return connection;
    }
}
