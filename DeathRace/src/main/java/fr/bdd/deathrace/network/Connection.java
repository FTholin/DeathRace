package fr.bdd.deathrace.network;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Manage object stream of a connection.
 */
public class Connection {
    private static final Logger logger = Logger.getLogger(Connection.class);

    // streams
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    // thread which handle input
    private MessageListener messageListener;

    /**
     * Constructor.
     * @param socket connection socket
     * @param ois input stream
     * @param oos output stream
     */
    public Connection(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        this.socket = socket;
        this.ois = ois;
        this.oos = oos;
    }

    /**
     * Try to send object to the connection.
     * @param message object to send
     * @return true if success, false otherwise
     */
    public boolean send(Object message) {
        boolean success = true;
        try {
            oos.writeObject(message);
            oos.flush();
            oos.reset();
        } catch (NullPointerException | IOException e) {
            success = false;
            logger.info("Failed to sent message to " + this.toString());
        }
        return success;
    }

    /**
     * Close all streams.
     */
    public void close() {
        this.stopMessageListener();

        logger.info("Close all streams");
        if (this.ois != null) {
            try {
                this.ois.close();
            } catch (IOException e) {
                logger.warn("Unable to close object input stream : " + e.getMessage());
            }
        }

        if (this.oos != null) {
            try {
                this.oos.close();
            } catch (IOException e) {
                logger.warn("Unable to close object output stream : " + e.getMessage());
            }
        }

        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException e) {
                logger.warn("Unable to close socket : " + e.getMessage());
            }
        }
    }

    /**
     * Stop thread which listen input.
     */
    public void stopMessageListener() {
        if (this.messageListener != null) {
            logger.info("Stop message listener");
            this.messageListener.stop();
        }
    }

    /**
     * Getter socket.
     * @return socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Getter object input stream.
     * @return object input stream
     */
    public ObjectInputStream getObjectInputStream() {
        return ois;
    }

    /**
     * Getter object output stream.
     * @return object output stream
     */
    public ObjectOutputStream getObjectOutputStream() {
        return oos;
    }

    /**
     * Getter message listener.
     * @return message listener
     */
    public MessageListener getMessageListener() {
        return messageListener;
    }

    /**
     * Associate a runnable which listen input to the connection.
     * @param messageListener the runnable
     */
    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @Override
    public String toString() {
        if (socket != null) {
            return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
        }
        return "null";
    }
}
