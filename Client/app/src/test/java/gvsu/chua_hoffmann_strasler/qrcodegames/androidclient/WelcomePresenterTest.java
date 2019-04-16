package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.welcome.WelcomeContract;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.welcome.WelcomePresenter;

public class WelcomePresenterTest {
    public class MockWelcomeActivity implements WelcomeContract.View {

        private boolean mWasCalled = false;

        @Override
        public void showUserName(String userName) {
            mWasCalled = true;
        }

        @Override
        public void createGame() {
            mWasCalled = true;
        }

        @Override
        public void joinGame() {
            mWasCalled = true;
        }

        @Override
        public void setPresenter(WelcomeContract.Presenter presenter) {
            mWasCalled = true;
        }

        public boolean wasCalled() {
            return mWasCalled;
        }
    }

    public MockWelcomeActivity mock;

    public WelcomePresenter presenter;

    @Before
    public void initialize() {
        mock = new MockWelcomeActivity();
        presenter = new WelcomePresenter(mock);
        presenter.start();
    }

    @Test
    public void createGameShouldCallView() {
        presenter.createGame();
        assertTrue(mock.wasCalled());
    }

    @Test
    public void joinGameShouldCallView() {
        presenter.joinGame();
        assertTrue(mock.wasCalled());
    }

    @Test
    public void userNameShouldBeCorrect() {
        String expected = "Test123";
        presenter.setUserName(expected);
        assertEquals(expected, presenter.getUserName());
    }
}
