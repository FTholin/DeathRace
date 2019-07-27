package fr.bdd.deathrace.network;

/**
 * Interface which handle messages from connections.
 */
public interface IMessageHandler {

    /**
     * Handle the new message.
     * @param message message
     * @param connection transmitter
     */
    void handle(Object message, Connection connection);

    /**
     * Handle error while reading messages from a connection.
     * @param connection connection
     */
    void handleError(Connection connection);
}
