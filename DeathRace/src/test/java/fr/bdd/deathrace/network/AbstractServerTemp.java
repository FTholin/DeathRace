package fr.bdd.deathrace.network;

import fr.bdd.deathrace.network.server.AbstractServer;

public class AbstractServerTemp extends AbstractServer {
    private boolean messageHandled = false;

    /**
     * Constructor.
     * Create a server at the specified port.
     *
     * @param port port
     */
    AbstractServerTemp(int port) {
        super(port);
    }

    @Override
    public void handle(Object message, Connection connection) {
        messageHandled = true;
    }

    @Override
    public void handleError(Connection connection) {

    }

    boolean isMessageHandled() {
        return messageHandled;
    }

}
