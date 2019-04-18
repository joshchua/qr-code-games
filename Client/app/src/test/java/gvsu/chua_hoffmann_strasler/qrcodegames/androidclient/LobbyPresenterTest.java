package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient;
import org.junit.Before;
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

    @Before
    public void init() {
        mock = new MockLobbyActivity();
        presenter = new LobbyPresenter(mock);
        presenter.start();

    }

    @Test
    public void switchTeamsShouldCallView() {
        presenter.switchTeams("userName", "12345");
        assertTrue(mock.wasCalled());
    }

    @Test
    public void updateTeamsShouldCallView() {
        presenter.updateTeams(new String[1], new String[1],
                new ArrayList<String>(), new ArrayList<String>());
        assertTrue(mock.wasCalled());
    }

    @Test
    public void startGameShouldCallView() {
        presenter.startGame();
        assertTrue(mock.wasCalled());
    }

    @Test
    public void setTitleAndCodeShouldSetTitle() {
        String expected = "This is a title";
        presenter.setTitleAndCode(expected, "a game code");
        assertEquals(expected, presenter.getGameTitle());
    }

    @Test
    public void setTitleAndCodeShouldSetGameCode() {
        String expected = "GameC0de";
        presenter.setTitleAndCode("title", expected);
        assertEquals(expected, presenter.getGameCode());
    }

}