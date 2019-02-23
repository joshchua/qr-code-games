package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.lobby;

import java.util.ArrayList;

public class LobbyPresenter implements LobbyContract.Presenter {

    private LobbyContract.View mLobbyView;

    private String gameCode;

    public LobbyPresenter(LobbyContract.View connectView) {
        mLobbyView = connectView;
        mLobbyView.setPresenter(this);
    }

    @Override
    public void switchTeams(String userName, String gameCode) {
        mLobbyView.sendSwitchTeamRequest(userName, gameCode);
    }

    @Override
    public void updateTeams(String[] team1, String[] team2, ArrayList<String> teamArray1, ArrayList<String> teamArray2) {
        teamArray1.clear();
        teamArray2.clear();
        if(team1 != null) {for (String user: team1) teamArray1.add(user);}
        if(team2 != null) {for (String user: team2) teamArray2.add(user);}
        mLobbyView.updateTeams();
    }

    @Override
    public void startGame() {
        mLobbyView.startGame(gameCode);
    }

    @Override
    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    @Override
    public String getGameCode() {
        return gameCode;
    }

    @Override
    public void start() {

    }

}
