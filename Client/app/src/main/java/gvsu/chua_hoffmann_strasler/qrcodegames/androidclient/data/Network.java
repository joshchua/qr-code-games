package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/**
 * A class holding Kryo serializable objects for Kryonet socket communication.
 */
public final class Network {

    /**
     * Default port number.
     */
    public static final int PORT = 54555;

    /**
     * Registers relevant objects to be Kryo serialized for transport.
     *
     * @param endPoint The endPoint receive the registrations.
     */
    public static void register(final EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(CreateGame.class);
        kryo.register(JoinGame.class);
        kryo.register(Lobby.class);
        kryo.register(String[].class);
        kryo.register(JoinGameErrorResult.class);
        kryo.register(SwitchTeam.class);
        kryo.register(StartGame.class);
        kryo.register(GameEvent.class);
        kryo.register(Scan.class);
    }

    /**
     * Object to be sent over the network telling the server to create a new
     * game.
     */
    public static class CreateGame {
        /**
         * The type of game to be created.
         */
        public int game;

        /**
         * The user's username.
         */
        public String userName;
    }

    /**
     * Object to be sent over the network telling the server to add the user to
     * an existing game.
     */
    public static class JoinGame {
        /**
         * The gameCode.
         */
        public String gameCode;

        /**
         * The user name.
         */
        public String userName;
    }

    /**
     * Object to be sent over the network holding the state of the lobby.
     */
    public static class Lobby {
        /**
         * The name of the game.
         */
        public String gameName;

        /**
         * The code for the game.
         */
        public String gameCode;

        /**
         * The user names of the players on team 1.
         */
        public String[] team1;

        /**
         * The user names of the players on team 2.
         */
        public String[] team2;
    }

    /**
     * Object to be sent over the network telling the server to switch the
     * user's teams.
     */
    public static class SwitchTeam {

        /**
         * The game code of the session.
         */
        public String gameCode;

        /**
         * The user name of the user.
         */
        public String userName;
    }

    /**
     * Sent to the client if there is an error with joining a game.
     */
    public static class JoinGameErrorResult {

        /**
         * The error message.
         */
        public String message;
    }

    /**
     * Object sent to the server, telling the server to start the game.
     */
    public static class StartGame {
        /**
         * The game code.
         */
        public String gameCode;
    }

    /**
     * Object sent to clients, telling the clients the game events.
     */
    public static class GameEvent {
        /**
         * The message.
         */
        public String message;
        /**
         * Is game playing.
         */
        public boolean isPlaying;
    }

    /**
     * Object sent to the server, telling the server what has been scanned.
     */
    public static class Scan {
        /**
         * The scanned raw text.
         */
        public String scanned;
    }
}
