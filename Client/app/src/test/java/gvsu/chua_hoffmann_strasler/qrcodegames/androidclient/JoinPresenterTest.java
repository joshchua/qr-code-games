package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient;

import org.junit.Before;
import org.junit.Test;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.join.JoinContract;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.join.JoinPresenter;

import static org.junit.Assert.*;


public class JoinPresenterTest {
    public class MockJoinActivity implements JoinContract.View {
        public boolean mWasCalled = false;

        @Override
        public void sendJoinGameRequest(String ip, int port, String userName, String gameCode) {
            mWasCalled = true;
        }

        @Override
        public void showError(String error) {
            mWasCalled = true;
        }

        @Override
        public void getBack() {
            mWasCalled = true;
        }

        @Override
        public void setPresenter(JoinContract.Presenter presenter) {
            mWasCalled = true;
        }

        public boolean wasCalled() {
            return mWasCalled;
        }
    }

    public MockJoinActivity mock;

    public JoinPresenter presenter;

    @Before
    public void initialize() {
        mock = new MockJoinActivity();
        presenter = new JoinPresenter(mock);
        presenter.start();
    }

    @Test
    public void isVaildIPAddressShouldPassValidIP() {
        String ip = "1.160.10.240";
        assertTrue(presenter.isValidIPAddress(ip));
    }

    @Test
    public void isValidIPAddressShouldFailInvalidIP() {
        String ip = "bad input";
        assertFalse(presenter.isValidIPAddress(ip));
    }

    @Test
    public void isValidPortShouldPassValidPort() {
        String port = "12345";
        assertTrue(presenter.isValidPort(port));
    }

    @Test
    public void isValidPortShouldFailInvalidPort() {
        String port = "bad input";
        assertFalse(presenter.isValidPort(port));
    }

    @Test
    public void joinGameShouldCallView() {
        String ip = "1.160.10.240";
        String port = "12345";
        String gameCode = "ABC123";
        presenter.joinGame(ip, port, gameCode);
        assertTrue(mock.wasCalled());
    }

    @Test
    public void setAndGetUserNameShould() {
        String expected = "username1";
        presenter.setUserName(expected);
        assertEquals(expected, presenter.getUserName());
    }

    @Test
    public void getBackShouldCallView() {
        presenter.getBack();
        assertTrue(mock.wasCalled());
    }
}
