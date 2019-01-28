import com.esotericsoftware.kryonet.*;

import data.*;
import data.Network.RegisterUserName;

import java.io.IOException;

public class QRCodeGameServer {

    Server server;

    public QRCodeGameServer() {
        server = new Server() {
            protected Connection newConnection() {
                return new GameConnection();
            }
        };
        Network.register(server);

        server.addListener(new Listener() {
            public void received(Connection con, Object obj) {
                GameConnection gc = (GameConnection)con;

                if (obj instanceof RegisterUserName) {
                    if (gc.userName != null) return;

                    String name = ((RegisterUserName)obj).userName;

                    if (name == null) return;

                    gc.userName = name;
                    System.out.println(name + " has been registered");
                }
            }

            public void disconnected(Connection con) {

            }
        });

        try {
            server.bind(Network.PORT);
            server.start();
        } catch (IOException ex) {

        }
    }

    public static void main(String[] args) {
        new QRCodeGameServer();
    }
}
