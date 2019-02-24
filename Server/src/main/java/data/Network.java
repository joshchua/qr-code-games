package data;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/**
 * This class handles the server connection from the kryo plugin
 */
public class Network {
    /**
     * Number of the port for the server
     */
    public static final int PORT = 54555;

    /**
     * Registers all classes on the server
     * @param endPoint name of the endpoint
     */
    public static void register(EndPoint endPoint) {
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
     * Class defining variables needed to create a game
     */
    public static class CreateGame {
        public int game;
        public String userName;
    }

    /**
     * Class defining variables needed to join a game
     */
    public static class JoinGame {
        public String gameCode;
        public String userName;
    }

    /**
     * Class defining variables needed to create a lobby
     */
    public static class Lobby {
        public String gameName;
        public String gameCode;
        public String[] team1;
        public String[] team2;
    }

    /**
     * Class defining variables needed to switch a team
     */
    public static class SwitchTeam {
        public String gameCode;
        public String userName;
    }

    /**
     * Class defining the result of the Join Game error
     */
    public static class JoinGameErrorResult {
        public String message;
    }

    /**
     * Class defining variables needed to start game
     */
    public static class StartGame {
        public String gameCode;
    }

    /*
     *Class defining variables needed for a game event
     */
    public static class GameEvent {
        public String message;
        public boolean isPlaying;
    }

    /**
     Class defining variables needed for a scan
     */
    public static class Scan {
        public String scanned;
    }
}
