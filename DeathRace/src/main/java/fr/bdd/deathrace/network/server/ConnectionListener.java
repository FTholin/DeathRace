package fr.bdd.deathrace.network.server;

import fr.bdd.deathrace.network.Connection;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Handle new connection for a server.
 */
public class ConnectionListener implements Runnable {
    private static final Logger logger = Logger.getLogger(ConnectionListener.class);

    private boolean run;
    private IServer server;

    /**
     * Handle new connection for the server.
     * @param server server
     */
    public ConnectionListener(IServer server) {
        this.server = server;
        this.run = true;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = this.server.getServerSocket();
        Socket socket;
        ObjectInputStream ois;  // input stream
        ObjectOutputStream oos; // output stream

        logger.info(String.format("Start listening for incoming connection for server %s:%d",
                server.getHostAddress(), server.getPort()));

        while (this.run) {
            socket = null;
            ois = null;
            oos = null;

            try {
                socket = serverSocket.accept(); // blocking method
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
            } catch (SocketException e) {
                logger.error(
                        String.format("SocketException for server %s:%d -> %s",
                        server.getHostAddress(), server.getPort(), e));
                if (serverSocket.isClosed()) {
                    this.run = false;
                }

            } catch (IOException e) {
                logger.error("IOException while trying to accept connection -> " + e);
                if (serverSocket.isClosed()) {
                    this.run = false;
                }
            }

            if (socket != null && oos != null && ois != null && this.run) {
                logger.info("A new client is connected");
                this.server.addConnection(new Connection(socket, ois, oos));
            }
        }
        logger.info("Stop listening for incoming connection");
    }

    public void stop() {
        logger.info("Request to stop listening for incoming connection");
        this.run = false;
    }

    public boolean isRunning() {
        return run;
    }
}
