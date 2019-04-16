package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.lobby.LobbyContract;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.lobby.LobbyPresenter;

public class LobbyPresenterTest {
    public static class MockLobbyActivity implements LobbyContract.View {

        private boolean mWasCalled = false;

        @Override
        public void sendSwitchTeamRequest(String userName, String gameCode) {
            mWasCalled = true;
        }

        @Override
        public void updateTeams() {
            mWasCalled = true;
        }

        @Override
        public void startGame(String gameCode) {
            mWasCalled = true;
        }

        @Override
        public void setPresenter(LobbyContract.Presenter presenter) {
            mWasCalled = true;
        }

        public boolean wasCalled() {
            return mWasCalled;
        }
    }

    public MockLobbyActivity mock;

    public LobbyPresenter presenter;

    @Test
    public void switchTeamsShouldCallView() {
        mock = new MockLobbyActivity();
        presenter = new LobbyPresenter(mock);
        presenter.start();

        presenter.switchTeams("userName", "12345");
        assertTrue(mock.wasCalled());
    }

    @Test
    public void updateTeamsShouldCallView() {
        mock = new MockLobbyActivity();
        presenter = new LobbyPresenter(mock);
        presenter.start();

        presenter.updateTeams(new String[1], new String[1],
                new ArrayList<String>(), new ArrayList<String>());
        assertTrue(mock.wasCalled());
    }

    @Test
    public void startGameShouldCallView() {
        mock = new MockLobbyActivity();
        presenter = new LobbyPresenter(mock);
        presenter.start();

        presenter.startGame();
        assertTrue(mock.wasCalled());
    }

    @Test
    public void getGameCodeShouldGetCorrectGameCode() {
        mock = new MockLobbyActivity();
        presenter = new LobbyPresenter(mock);
        presenter.start();

        String expected = "TEST";
        presenter.setTitleAndCode(expected);
        assertEquals(expected, presenter.getGameCode());
    }

}
