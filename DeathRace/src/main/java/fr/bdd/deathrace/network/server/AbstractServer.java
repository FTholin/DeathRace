package fr.bdd.deathrace.network.server;

import fr.bdd.deathrace.network.Connection;
import fr.bdd.deathrace.network.IMessageHandler;
import fr.bdd.deathrace.network.MessageListener;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Server.
 */
public abstract class AbstractServer implements IMessageHandler, IServer {
    private static final Logger logger = Logger.getLogger(AbstractServer.class);

    protected ServerSocket serverSocket;                  // server socket
    protected final ConcurrentLinkedQueue<Connection> connections;         // connected machines
    protected ConnectionListener connectionListener;      // thread which listen for new connection
    protected boolean listenForNewConnection;

    /**
     * Constructor.
     * Create a server at the specified port.
     * @param port port
     */
    public AbstractServer(int port) {
        this.connections = new ConcurrentLinkedQueue<>();
        this.listenForNewConnection = false;

        try {
            // create the server at the specified port
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            logger.error("Failed to create server socket : " + e.getMessage());
        }

        if (this.serverSocket != null) {
            logger.info("Server listen to " + getHostAddress() + ":" + getPort());
            createConnectionListener();
        }
    }

    /**
     * Create a thread which will handle new connections and start it.
     */
    public void createConnectionListener() {
        if (!this.listenForNewConnection) {
            this.listenForNewConnection = true;
            this.connectionListener = new ConnectionListener(this);
            new Thread(this.connectionListener).start();
        }
    }

    /**
     * Stop connection listener.
     */
    public void stopConnectionListener() {
        if (this.connectionListener != null) {
            this.connectionListener.stop();
            this.listenForNewConnection = false;
        }
    }

    @Override
    public synchronized void close(Connection connection) {
        if (connections.contains(connection)) {
            connection.close();
            connections.remove(connection);
            logger.info(String.format("Connection %s have been closed", connection.toString()));
        } else {
            logger.info(String.format("Connection %s is already closed", connection.toString()));
        }
    }

    @Override
    public synchronized void closeAllConnections() {
        Iterator<Connection> iterator = connections.iterator();
        Connection connection;
        while (iterator.hasNext()) {
            connection = iterator.next();
            iterator.remove();
            connection.close();
        }
    }

    @Override
    public void stopServer() {
        if (this.connectionListener != null) {
            this.connectionListener.stop();
        }
        closeAllConnections();
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            logger.error("Failed to close server socket : " + e.getMessage());
        } catch (NullPointerException e) {
            logger.warn("Try to close a server that is not setup : " + e.getMessage());
        }
    }

    @Override
    public String getHostAddress() {
        if (this.serverSocket != null) {
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                logger.error("Failed to get host address : " + e.getMessage());
            }
        }
        return "0.0.0.0";
    }

    @Override
    public int getPort() {
        if (this.serverSocket != null) {
            return this.serverSocket.getLocalPort();
        }
        return -1;
    }

    @Override
    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    @Override
    public synchronized void addConnection(Connection connection) {
        if (this.listenForNewConnection) {
            this.connections.add(connection);
            // creer un thread qui va se charger d'ecouter les messages provenant de cette connexion
            MessageListener messageListener = new MessageListener(this, connection);
            connection.setMessageListener(messageListener);
            new Thread(messageListener).start();
        } else {
            connection.close();
        }
    }

    /**
     * Send message to all connected machines.
     * @param message message
     */
    public void sendToAll(Object message) {
        for (Connection connection : connections) {
            connection.send(message);
        }
    }

    /**
     * Send message to all connected machines except provider.
     * @param message message
     * @param provider provider
     */
    public void sendToAllExceptProvider(Object message, Connection provider) {
        for (Connection connection : connections) {
            if (provider != connection) {
                connection.send(message);
            }
        }
    }

    public Queue<Connection> getConnections() {
        return connections;
    }
}
