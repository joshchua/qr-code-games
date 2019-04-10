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

/**
 * The type Qr code game server.
 */
public class QRCodeGameServer {
    /**
     * Array list of all the active games
     */
    private ArrayList<Game> games;

    /**
     * Number of the port that is user for the connection
     */
    private int port;

    /**
     * The Server.
     */
    Server server;

    /**
     * Starts the server on a port 54555 if no port given
     */
    public QRCodeGameServer() {
        port = Network.PORT;
        startServer();
    }

    /**
     * Starts the server on a port variable
     *
     * @param port number of the server port
     */
    public QRCodeGameServer(int port) {
        this.port = port;
        startServer();
    }

    /**
     * Server and opens the port on the network
     */
    private void startServer() {
        games = new ArrayList<Game>();
        server = new Server() {
            protected Connection newConnection() {
                return new GameConnection();
            }
        };
        Network.register(server);

        System.out.println("QR Code Games Server was created on port " + port);

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
            server.bind(port);
            server.start();
        } catch (IOException ex) {

        }
    }

    /**
     *  Sends the object to all players connected the specific game
     * @param game name of the game receiving the object
     * @param obj contains information about the event
     */
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

    /**
     *  Tries to find a game with matching gameCode ID
     * @param gameCode ID of the game
     * @return Returns null if no game found, returns Game object of the game if it matches
     */
    private Game findGame(String gameCode) {
        for (Game game : games) {
            if (game.getGameCode().equals(gameCode))
                return game;
        }
        return null;
    }

    /**
     * Send message to client if an error occurs when joining the game
     * @param connectionID client connection
     * @param msg Error message
     */
    private void sendJoinGameError(int connectionID, String msg) {
        JoinGameErrorResult error = new JoinGameErrorResult();
        error.message = msg;
        server.sendToTCP(connectionID, error);
    }

    /**
     *  Creates a new game of a specific game type
     * @param gameNum type of the game
     * @param userName name of the player that created the game
     */

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

    /**
     * Find if there is matching game and joins it
     * @param gameCode game code of the game to be joined
     * @param userName name of player joining the game
     */
    private void joinGame(String gameCode, String userName) {
        Game game = findGame(gameCode);
        game.joinLobby(userName);
        System.out.println(userName + " joined " + game.getGameName() + ". (" + game.getGameCode() + ")");
        sendAllUpdatedLobby(game);
    }

    /**
     * Switched teams of a player
     * @param gameCode ID of the game where the player is switching their team
     * @param userName name of the player switching the teams
     */
    private void switchTeam(String gameCode, String userName) {
        Game game = findGame(gameCode);
        game.switchTeam(userName);
        sendAllUpdatedLobby(game);
    }

    /**
     *  Every time someone joins the game or switches team it sends arrays of the teams to all players
     * @param game name of the game that was updated
     */
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

    /**
     * Starts the game
     * @param gameCode ID of the game to be started
     */
    private void startGame(String gameCode) {
        Game game = findGame(gameCode);

        if (game != null) {
            if (!game.isPlaying()) {
                game.start();

                String msg = String.format("%s (%s) has started!",
                        game.getGameName(), game.getGameCode());
                System.out.println(msg);
                GameEvent ge = new GameEvent();
                ge.message = msg;
                ge.isPlaying = game.isPlaying();
                sendToPlayers(game, ge);
            }
        }
    }

    /**
     * Called when a player had been scanned by another player
     * @param gameCode ID of the game
     * @param userName name of the player who scanned it
     * @param scanned name of the object that was scanned
     */
    private void handleScan(String gameCode, String userName, String scanned) {
        Game game = findGame(gameCode);
        if (game != null) {
            ScanResult scanResult = game.handleScan(userName, scanned);

            if (scanResult != null) {
                GameEvent ge = new GameEvent();
                ge.message = scanResult.getMessage();
                ge.isPlaying = game.isPlaying();
                sendToPlayers(game, ge);
                System.out.println(ge.message);
            }
        }
    }

    /**
     *Main method that starts the server when ran
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        int port = -1;
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                try {
                    if (args[i].equals("-port") || args[i].equals("-p")) {
                        port = Integer.parseInt(args[i + 1]);
                    }
                } catch (Exception ex) {
                    System.out.println("There is an error with the given arguments.");
                }
            }
        }

        if (port == -1)
            new QRCodeGameServer();
        else
            new QRCodeGameServer(port);
    }
}
