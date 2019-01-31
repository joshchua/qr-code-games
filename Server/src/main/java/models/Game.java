package models;

import java.util.ArrayList;
import java.util.Random;

public class Game {

    private String mGameCode;

    protected ArrayList<Player> team1;

    protected ArrayList<Player> team2;

    private ArrayList<String> lobby;

    protected boolean mIsStarted;

    public Game() {
        final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final int N = alphabet.length();

        // Randomly generates a new game code
        Random r = new Random();
        mGameCode = "";
        for (int i = 0; i < 5; i++) {
            mGameCode += alphabet.charAt(r.nextInt(N));
        }

        mIsStarted = false;
    }

    public String getGameCode() {
        return mGameCode;
    }

    public void start() {
        mIsStarted = true;
    }

    protected Player findPlayer(String userName) {
        return null; //TODO: Write a quick search to find a player
    }

    protected Player findPlayer(String userName, int team) {
        return null;
    }

    public void addNewPlayer(int team, String userName) {
        Player player = new Player(userName, team);
        if (team == 1) {
            team1.add(player);
        } else if (team == 2) {
            team2.add(player);
        }
    }

    public void joinLobby(String userName) {
        lobby.add(userName);
    }

    // This should be overridden by classes that inherit from Game
    public void handleScan(String userName, String scanned) {
        System.out.println(userName + " scanned " + scanned);
    }
}
