package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data;

/**
 * Holds information regarding the connection to the game server.
 */
public class ConnectionInfo {

    /**
     * The server's IP address.
     */
    public String ip;

    /**
     * The server's port
     */
    public int port;

    /**
     * Creates a Connection Info object.
     *
     * @param ip IP Address
     * @param port Port
     */
    public ConnectionInfo(final String ip, final int port) {
        this.ip = ip;
        this.port = port;
    }
}
