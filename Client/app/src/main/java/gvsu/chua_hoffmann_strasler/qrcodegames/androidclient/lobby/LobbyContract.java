package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.lobby;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BasePresenter;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseView;

/**
 * The contract binding the Lobby View and Presenter using the MVP pattern
 */
public interface LobbyContract {
    /**
     * The Lobby View interface
     */
    interface View extends BaseView<Presenter> {
        void sendSwitchTeamRequest(final String userName, final String gameCode);
        void updateTeams();
        void startGame(String gameCode);
    }

    /**
     * The Lobby Presenter interface
     */
    interface Presenter extends BasePresenter {
        void switchTeams(String userName, String gameCode);
        void updateTeams(String[] team1, String[] team2, ArrayList<String> teamArray1,  ArrayList<String> teamArray2);
        void startGame();
        void setGameCode(String gameCode);
        String getGameCode();
    }


}
