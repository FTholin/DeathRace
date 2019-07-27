package fr.bdd.deathrace.network;

import fr.bdd.deathrace.network.client.AbstractClient;

public class AbstractClientTemp extends AbstractClient {
    private boolean messageHandled = false;

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
