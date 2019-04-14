package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.lobby;

import java.util.ArrayList;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BasePresenter;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseView;

/**
 * The contract binding the Lobby View and Presenter using the MVP pattern.
 */
public interface LobbyContract {
    /**
     * The Lobby View interface.
     */
    interface View extends BaseView<Presenter> {

        /**
         * Tries to connect to the server and start the game.
         *
         * @param userName name of the player switching teams
         * @param gameCode Unique game ID
         */
        void sendSwitchTeamRequest(String userName,
                                    String gameCode);

        /**
         * Updates lists with the players on the screen.
         */
        void updateTeams();

        /**
         * Tries to connect to the server and start the game.
         *
         * @param gameCode Unique game ID
         */
        void startGame(String gameCode);
    }

    /**
     * The Lobby Presenter interface.
     */
    interface Presenter extends BasePresenter {

        /**
         * Switches team for a player.
         *
         * @param userName name of the player that is switching teams
         * @param gameCode ID of the game where the player is switching teams
         */
        void switchTeams(String userName, String gameCode);

        /**
         * Checks if team has players and updates the array.
         *
         * @param team1      array of team 1
         * @param team2      array of team 2
         * @param teamArray1 array list of team 1
         * @param teamArray2 array list of team 2
         */
        void updateTeams(String[] team1, String[] team2, ArrayList<String>
                teamArray1, ArrayList<String> teamArray2);

        /**
         * Starts the game when someone hits the "Start game" button.
         */
        void startGame();

        /**
         * Returns gameCode.
         *
         * @return ID of the game
         */
        String getGameCode();

        /**
         * Returns title text.
         *
         * @return  title text
         */
        String getGameTitle();

        /**
         * Sets the local title and gameCode.
         *
         * @param title title of the game
         * @param gameCode ID of the game
         */
        void setTitleAndCode(String title, String gameCode);
    }


}
