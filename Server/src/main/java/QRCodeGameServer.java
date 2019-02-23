import com.esotericsoftware.kryonet.*;

import data.*;
import data.Network.CreateGame;
import data.Network.JoinGame;
import data.Network.Lobby;
import data.Network.SwitchTeam;
import data.Network.JoinGameErrorResult;
import data.Network.StartGame;
import data.Network.GameEvent;
import data.Network.Scan;
import games.CaptureTheFlag;
import games.Game;
import models.ScanResult;

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
                    createGame(cg.game, cg.userName);
                    gc.gameCode = games.get(games.size() - 1).getGameCode();
                }

                if (obj instanceof JoinGame) {
                    JoinGame jg = (JoinGame)obj;
                    Game game = findGame(jg.gameCode);
                    if (game == null) {
                        String msg = "There is no game with the provided game code.";
                        sendJoinGameError(gc.getID(), msg);
                        return;
                    }

                    if (game.getPlayers().contains(jg.userName)) {
                        String msg = "Someone with that user name is already in this game.";
                        sendJoinGameError(gc.getID(), msg);
                        return;
                    }

                    gc.gameCode = jg.gameCode;
                    gc.userName = jg.userName;
                    joinGame(jg.gameCode, jg.userName);
                }

                if (obj instanceof SwitchTeam) {
                    SwitchTeam ct = (SwitchTeam)obj;
                    System.out.printf("%s is switching teams in game %s.%n", ct.userName, ct.gameCode);
                    switchTeam(ct.gameCode, ct.userName);
                }

                if (obj instanceof StartGame) {
                    StartGame sg = (StartGame)obj;
                    startGame(sg.gameCode);
                }

                if (obj instanceof Scan) {
                    Scan scan = (Scan)obj;
                    handleScan(gc.gameCode, gc.userName, scan.scanned);
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

    private void sendToPlayers(Game game, Object obj) {
        ArrayList<String> temp = new ArrayList<String>(game.getPlayers());
        for (Connection connection : server.getConnections()) {
            GameConnection gc = (GameConnection)connection;
            for (String userName : temp) {
                if (userName.equals(gc.userName)) {
                    server.sendToTCP(gc.getID(), obj);
                    temp.remove(userName);
                    break;
                }
            }
        }
    }

    private Game findGame(String gameCode) {
        for (Game game : games) {
            if (game.getGameCode().equals(gameCode))
                return game;
        }
        return null;
    }

    private void sendJoinGameError(int connectionID, String msg) {
        JoinGameErrorResult error = new JoinGameErrorResult();
        error.message = msg;
        server.sendToTCP(connectionID, msg);
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
        game.joinLobby(userName);
        System.out.println(userName + " joined " + game.getGameName() + ". (" + game.getGameCode() + ")");
        sendAllUpdatedLobby(game);
    }

    private void switchTeam(String gameCode, String userName) {
        Game game = findGame(gameCode);
        game.switchTeam(userName);
        sendAllUpdatedLobby(game);
    }

    private void sendAllUpdatedLobby(Game game) {
        Lobby lobby = new Lobby();
        lobby.gameCode = game.getGameCode();
        lobby.gameName = game.getGameName();

        ArrayList<String> team1 = new ArrayList<String>();
        ArrayList<String> team2 = new ArrayList<String>();

        for (String userName : game.getPlayers()) {
            if (game.findTeam(userName) == 1) {
                team1.add(userName);
            } else if (game.findTeam(userName) == 2) {
                team2.add(userName);
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

        sendToPlayers(game, lobby);
    }

    private void startGame(String gameCode) {
        Game game = findGame(gameCode);

        if (game != null) {
            if (!game.isPlaying()) {
                String msg = String.format("%s (%s) has started!",
                        game.getGameName(), game.getGameCode());
                System.out.println(msg);
                GameEvent ge = new GameEvent();
                ge.message = msg;
                sendToPlayers(game, ge);
            }
        }
    }

    private void handleScan(String gameCode, String userName, String scanned) {
        Game game = findGame(gameCode);
        if (game != null) {
            ScanResult scanResult = game.handleScan(userName, scanned);

            if (scanResult != null) {
                GameEvent ge = new GameEvent();
                ge.message = scanResult.getMessage();
                sendToPlayers(game, ge);
            }
        }
    }

    public static void main(String[] args) {
        new QRCodeGameServer();
    }
}
