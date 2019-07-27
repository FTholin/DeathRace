package fr.bdd.deathrace.network.server;

import fr.bdd.deathrace.network.Connection;

import java.net.ServerSocket;

/**
 * Necessary methods for a server.
 */
public interface IServer {

    /**
     * The active server socket.
     * @return server socket
     */
    ServerSocket getServerSocket();

    /**
     * Add a new connection to know all connected machine.
     * @param connection new connection
     */
    void addConnection(Connection connection);

    /**
     * Close the given connection if belongs to server.
     * @param connection connection
     */
    void close(Connection connection);

    /**
     * Close all connections.
     */
    void closeAllConnections();

    /**
     * Stop the server.
     */
    void stopServer();

    /**
     * If the server isn't set, return -1.
     * @return port
     */
    String getHostAddress();

    /**
     * If the server isn't set, return 0.0.0.0.
     * @return IP address
     */
    int getPort();
}
