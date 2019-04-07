package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.lobby;

import java.util.ArrayList;

/**
 * Presenter for Lobby activity
 */
public class LobbyPresenter implements LobbyContract.Presenter {

    /**
     * Lobby view
     */
    private LobbyContract.View mLobbyView;

    /**
     * Unique ID of the game
     */
    private String gameCode;

    /**
     * Initializes the connection between the Lobby View and this presenter
     *
     * @param LobbyView The Connect View to be bound to this presenter
     */
    public LobbyPresenter(LobbyContract.View LobbyView) {
        mLobbyView = LobbyView;
        mLobbyView.setPresenter(this);
    }

    /**
     * Switches team for a player
     *
     * @param userName name of the player that is switching teams
     * @param gameCode ID of the game where the player is switching teams
     */
    @Override
    public void switchTeams(String userName, String gameCode) {
        mLobbyView.sendSwitchTeamRequest(userName, gameCode);
    }

    /**
     * Checks if team has players and updates the array
     *
     * @param team1      array of team 1
     * @param team2      array of team 2
     * @param teamArray1 array list of team 1
     * @param teamArray2 array list of team 2
     */
    @Override
    public void updateTeams(String[] team1, String[] team2, ArrayList<String> teamArray1, ArrayList<String> teamArray2) {
        teamArray1.clear();
        teamArray2.clear();
        if (team1 != null) {
            for (String user : team1) teamArray1.add(user);
        }
        if (team2 != null) {
            for (String user : team2) teamArray2.add(user);
        }
        mLobbyView.updateTeams();
    }

    /**
     * Starts the game when someone hits the "Start game" button
     */
    @Override
    public void startGame() {
        mLobbyView.startGame(gameCode);
    }

    /**
     * Returns gameCode
     *
     * @return ID of the game
     */
    @Override
    public String getGameCode() {
        return gameCode;
    }

    /**
     * Sets the local gameCode
     *
     * @param gameCode ID of the game
     */
    @Override
    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    /**
     * Start from the base presenter
     */
    @Override
    public void start() {
    }

}
