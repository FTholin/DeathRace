package fr.bdd.deathrace.network.client;

/**
 * Necessary methods for a client.
 */

public interface IClient {

    /**
     * Try to connect server with given info.
     * @param ip server IP
     * @param port server port
     * @return true if success, false otherwise
     */
    public boolean connect(String ip, int port);

    /**
     * Allows to know if connection is established.
     * @return true if yes, false otherwise
     */
    public boolean isConnected();

    /**
     * Close connection.
     */
    public void close();

    /**
     * Try to send message to conneted server.
     * @param messaege message to send
     * @return true if success, false otherwise
     */
    public boolean send(Object messaege);

}
