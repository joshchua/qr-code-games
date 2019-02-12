package data;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
    public static final int PORT = 54555;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(CreateGame.class);
        kryo.register(JoinGame.class);
        kryo.register(Lobby.class);
        kryo.register(String[].class);
        kryo.register(JoinGameErrorResult.class);
        kryo.register(SwitchTeam.class);
    }

    public static class CreateGame {
        public int game;
        public String userName;
    }

    public static class JoinGame {
        public String gameCode;
        public String userName;
    }

    public static class Lobby {
        public String gameName;
        public String gameCode;
        public String[] team1;
        public String[] team2;
    }

    public static class SwitchTeam {
        public String gameCode;
        public String userName;
    }

    public static class Tag {
        public String tagged;
    }

    public static class JoinGameErrorResult {
        public String message;
    }
}
