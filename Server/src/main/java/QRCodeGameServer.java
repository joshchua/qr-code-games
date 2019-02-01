import com.esotericsoftware.kryonet.*;

import data.*;
import data.Network.CreateGame;
import data.Network.JoinGame;
import data.Network.Lobby;
import data.Network.ChooseTeam;
import games.CaptureTheFlag;
import games.Game;

import java.io.IOException;
import java.util.ArrayList;

public class QRCodeGameServer {

    private ArrayList<Game> games;

    Server server;

    public QRCodeGameServer() {
        games = new ArrayList<Game>();
        server = new Server() {
            protected Connection newConnection() {
                return new GameConnection();
            }
        };
        Network.register(server);

        server.addListener(new Listener() {
            public void received(Connection con, Object obj) {
                GameConnection gc = (GameConnection)con;

                if (obj instanceof CreateGame) {
                    CreateGame cg = (CreateGame)obj;
                    gc.userName = cg.userName;
                    gc.gameIndex = games.size();
                    createGame(cg.game, cg.userName);
                }

                if (obj instanceof JoinGame) {
                    JoinGame jg = (JoinGame)obj;
                    gc.userName = jg.userName;
                    joinGame(jg.gameCode, jg.userName);
                    //gc.gameIndex = findGame(jg.gameCode);
                }

                if (obj instanceof ChooseTeam) {
                    ChooseTeam ct = (ChooseTeam)obj;
                    chooseTeam(ct.gameCode, gc.userName, ct.team);
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

    private Game findGame(String gameCode) {
        for (Game game : games) {
            if (game.getGameCode().equals(gameCode))
                return game;
        }
        return null;
    }

    private void createGame(int gameNum, String userName) {
        Game game;
        if (gameNum == 0) {
            game = new CaptureTheFlag();
        } else
            return; // better handling probably
        game.joinLobby(userName);

        System.out.println(userName + " has created a new game of " + game.getGameName() + ". (" + game.getGameCode() + ")");
        games.add(game);
        sendAllUpdatedLobby(game);
    }

    private void joinGame(String gameCode, String userName) {
        Game game = findGame(gameCode);
        // If no game exists with this game code
        if (game == null) {
            return;
        }

        // If someone with this userName is already in this game
        if (game.getPlayers().contains(userName)) {
            return;
        }

        game.joinLobby(userName);
        System.out.println(userName + " joined " + game.getGameName() + ". (" + game.getGameCode() + ")");
        sendAllUpdatedLobby(game);
    }

    private void chooseTeam(String gameCode, String userName, int team) {
        Game game = findGame(gameCode);
        game.chooseTeam(userName, team);
        sendAllUpdatedLobby(game);
    }

    private void sendAllUpdatedLobby(Game game) {
        Lobby lobby = new Lobby();
        lobby.gameName = game.getGameName();

        ArrayList<String> team1 = new ArrayList<String>();
        ArrayList<String> team2 = new ArrayList<String>();
        ArrayList<String> noTeam = new ArrayList<String>();
        for (String userName : game.getPlayers()) {
            if (game.findTeam(userName) == 1) {
                team1.add(userName);
            } else if (game.findTeam(userName) == 2) {
                team2.add(userName);
            } else {
                noTeam.add(userName);
            }
        }
        if (team1.size() > 0) {
            lobby.team1 = new String[team1.size()];
            lobby.team1 = team1.toArray(lobby.team1);
        }

        if (team2.size() > 0) {
            lobby.team2 = new String[team2.size()];
            lobby.team2 = team2.toArray(lobby.team2);
        }

        if (noTeam.size() > 0) {
            lobby.noTeam = new String[noTeam.size()];
            lobby.noTeam = noTeam.toArray(lobby.noTeam);
        }

        ArrayList<String> temp = new ArrayList<String>(game.getPlayers());
        for (Connection connection : server.getConnections()) {
            GameConnection gc = (GameConnection)connection;
            for (String userName : temp) {
                if (userName.equals(gc.userName)) {
                    server.sendToTCP(gc.getID(), lobby);
                    temp.remove(userName);
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        new QRCodeGameServer();
    }
}
