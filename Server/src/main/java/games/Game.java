package games;

import models.PlayerState;
import models.ScanResult;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class defining a basic structure for a game.
 */
public abstract class Game {

    /**
     * Holds the ID of the game.
     */
    private String mGameCode;

    /**
     * Holds the type of the game.
     */
    private String mGameName;

    /**
     * Holds an array list with all players.
     */
    private ArrayList<String> mPlayers;

    /**
     * Holds an array list with player state.
     */
    private ArrayList<Integer> mPlayerState;


    /**
     * Holds an array list with player team.
     */
    private ArrayList<Integer> mPlayerTeam;


    /**
     * Tells if the game has started.
     */
    private boolean mIsStarted;

    /**
     * Initializes game.
     */
    public Game() {
        mPlayers = new ArrayList<String>();
        mPlayerState = new ArrayList<Integer>();
        mPlayerTeam = new ArrayList<Integer>();

        final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final int n = alphabet.length();

        // Randomly generates a new game code
        Random r = new Random();
        mGameCode = "";
        for (int i = 0; i < 5; i++) {
            mGameCode += alphabet.charAt(r.nextInt(n));
        }

        mIsStarted = false;
    }

    /**
     * Returns the type of the game.
     * @return type of the
     */
    public String getGameName() {
        return mGameName;
    }

    /**
     * Returns game ID.
     * @return ID of the game
     */
    public String getGameCode() {
        return mGameCode;
    }

    /**
     * Switches teams for players.
     * @param userName name of the player switching teams
     */
    public void switchTeam(final String userName) {
        int index = mPlayers.indexOf(userName);
        int team = mPlayerTeam.get(index);
        if (team == 1) {
            team = 2;
        } else {
            team = 1;
        }

        mPlayerTeam.set(index, team);
    }

    /**
     * Joins a player in the lobby and adds him to a team.
     * @param userName name of the player joining the game
     */
    public void joinLobby(final String userName) {
        mPlayers.add(userName);
        mPlayerTeam.add(1);
        mPlayerState.add(PlayerState.NORMAL);
    }

    /**
     * Returns a array list of all players.
     * @return list of players
     */
    public ArrayList<String> getPlayers() {
        return mPlayers;
    }

    /**
     * Find a team of a player.
     * @param userName name of the player
     * @return team number of the player
     */
    public int findTeam(final String userName) {
        int index = mPlayers.indexOf(userName);
        if (index > -1) {
            return mPlayerTeam.get(index);
        }
        return -1;
    }

    /**
     * Starts the game.
     * This should be overridden by children, but also use super.start().
     */
    public void start() {
        mIsStarted = true;
    }

    /**
     * Ends the game.
     */
    public void gameOver() {
        mIsStarted = false;
    }

    /**
     * Check if the game has started.
     * @return True = game has started, False = the game has not started
     */
    public boolean isPlaying() {
        return mIsStarted;
    }

    /**
     * Handles a scan from a player.
     * This should be implemented by classes that inherit from Game.
     *
     * @param userName name of the player that scanned the object
     * @param scanned name of the scanned object
     * @return returns the message for players
     */
    public abstract ScanResult handleScan(String userName, String scanned);
}
