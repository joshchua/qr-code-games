package games;

import models.PlayerState;
import models.ScanResult;

import java.util.ArrayList;
import java.util.Random;

public abstract class Game {

    private String mGameCode;
    protected String mGameName;
    protected ArrayList<String> mPlayers;
    protected ArrayList<Integer> mPlayerState;
    protected ArrayList<Integer> mPlayerTeam;
    protected boolean mIsStarted;

    public Game() {
        mPlayers = new ArrayList<String>();
        mPlayerState = new ArrayList<Integer>();
        mPlayerTeam = new ArrayList<Integer>();

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

    public String getGameName() {
        return mGameName;
    }

    public String getGameCode() {
        return mGameCode;
    }

    public void chooseTeam(String userName, int team) {
        int index = mPlayers.indexOf(userName);
        if (team == 1 || team == 2)
            mPlayerTeam.set(index, team);
    }

    public void joinLobby(String userName) {
        mPlayers.add(userName);
        mPlayerTeam.add(0);
        mPlayerState.add(PlayerState.NORMAL);
    }

    public ArrayList<String> getPlayers() {
        return mPlayers;
    }

    public int findTeam(String userName) {
        int index = mPlayers.indexOf(userName);
        if (index > -1) {
            return mPlayerTeam.get(index);
        }
        return -1;
    }

    // This should be overridden by children, but also use super.start()
    public void start() {
        mIsStarted = true;
    }

    public boolean isPlaying() {
        return mIsStarted;
    }

    // This should be implemented by classes that inherit from Game
    public abstract ScanResult handleScan(String userName, String scanned);
}