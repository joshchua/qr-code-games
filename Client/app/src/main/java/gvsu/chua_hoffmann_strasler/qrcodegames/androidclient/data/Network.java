package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
    public static final int PORT = 54555;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(CreateGame.class);
        kryo.register(JoinGame.class);
    }

    public static class CreateGame {
        public int game;
        public String userName;
    }

    public static class JoinGame {
        public String gameCode;
        public String userName;
    }

    public static class JoinTeam {
        public int team;
    }

    public static class Tag {
        public String tagged;
        public String tagger;
    }
}
