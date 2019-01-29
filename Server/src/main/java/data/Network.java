package data;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
    public static final int PORT = 54555;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(RegisterUserName.class);
        kryo.register(CreateGame.class);
    }

    public static class RegisterUserName {
        public String userName;
    }

    public static class CreateGame {
        public int game;
    }
}