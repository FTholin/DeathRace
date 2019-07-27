package fr.bdd.deathrace.network;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Charge to listen incoming messages from a connection and transmits them to a IMessageHandler.
 */
public class MessageListener implements Runnable {
    private static final Logger logger = Logger.getLogger(MessageListener.class);

    private IMessageHandler messageHandler; // which to notify when a new message is received
    private Connection connection;          // which connection to listen
    private boolean run;
    // allows to know if the thread is reading stream (which is a blocking method)
    private boolean isReading;

    /**
     * Constructor.
     * Listen message from a connection and transmits them to another class.
     * @param messageHandler transmits message to this class.
     * @param connection connection to listen
     */
    public MessageListener(IMessageHandler messageHandler, Connection connection) {
        this.messageHandler = messageHandler;
        this.connection = connection;
        this.run = true;
        this.isReading = false;
    }


    @Override
    public void run() {
        logger.info("Start listening messages for " + connection.toString());
        ObjectInputStream ois = this.connection.getObjectInputStream();
        Object message;
        while (this.run) {
            message = null;
            try {
                this.isReading = true;
                message = ois.readObject(); // blocking method
            } catch (IOException e) {
                logger.error("IOException while reading messages from "
                        + connection.toString() + " -> " + e);
                this.run = false;
                messageHandler.handleError(connection);
            } catch (ClassNotFoundException e) {
                logger.error("ClassNotFoundException while reading messages from "
                        + connection.toString() + " -> " + e);
                this.run = false;
                messageHandler.handleError(connection);
            }
            this.isReading = false;

            if (message != null) {
                messageHandler.handle(message, connection);
            }
        }
        logger.info("Stop listening messages for " + connection.toString());
    }

    /**
     * Stop the runnable.
     * The thread will stop at next iteration.
     */
    public void stop() {
        this.run = false;
    }

    /**
     * Reading state of runnable.
     * If it is in reading state, the thread is blocked in the blocking method.
     * @return reading state
     */
    public boolean isReading() {
        return this.isReading;
    }
}
