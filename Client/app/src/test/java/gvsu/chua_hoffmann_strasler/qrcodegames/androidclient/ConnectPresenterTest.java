package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient;

import org.junit.Test;
import static org.junit.Assert.*;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.connect.ConnectContract;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.connect.ConnectPresenter;


public class ConnectPresenterTest {
    public static class MockConnectActivity implements ConnectContract.View {

        public MockConnectActivity() {

        }

        @Override
        public void sendCreateGameRequest(String ip, int port, String userName, int game) {
            mockWasCalled();
        }

        @Override
        public void sendJoinGameRequest(String ip, int port, String userName, String gameCode) {
            mockWasCalled();
        }

        @Override
        public void showError(String name) {
            mockWasCalled();

        }

        @Override
        public void showScanner() {
            mockWasCalled();
        }

        @Override
        public String getIpAddress() {
            return null;
        }

        @Override
        public void showUserName(String userName) {
            mockWasCalled();
        }

        @Override
        public void setConnectBtnEnabled(boolean isEnabled) {
            mockWasCalled();
        }

        @Override
        public void setPresenter(ConnectContract.Presenter presenter) {
            mockWasCalled();
        }

        public boolean mockWasCalled() {
            return true;
        }
    }

    public ConnectPresenter presenter;

    public MockConnectActivity mock;

    @Test
    public void shouldPassCorrectIp() {
        mock = new MockConnectActivity();
        presenter = new ConnectPresenter(mock);
        presenter.start();

        String testIp = "123.123.123.123";
        assertTrue(presenter.isValidIPAddress(testIp));
    }

    @Test
    public void isValidIPAddressShouldFailWrongIp() {
        mock = new MockConnectActivity();
        presenter = new ConnectPresenter(mock);
        presenter.start();

        String testIp = "NOT AN IP ADDRESS";
        assertFalse(presenter.isValidIPAddress(testIp));
    }

    @Test
    public void isValidPortShouldPassCorrectPort() {
        mock = new MockConnectActivity();
        presenter = new ConnectPresenter(mock);
        presenter.start();

        String testPort = "12345";
        assertTrue(presenter.isValidPort(testPort));

    }

    @Test
    public void isValidPortShouldFailWrongPort() {
        mock = new MockConnectActivity();
        presenter = new ConnectPresenter(mock);
        presenter.start();

        String testPort = "NOT A VALID PORT";
        assertFalse(presenter.isValidPort(testPort));
    }

    @Test
    public void isValidGameShouldPassCorrectGame() {
        mock = new MockConnectActivity();
        presenter = new ConnectPresenter(mock);
        presenter.start();

        String testGame = "0";
        assertTrue(presenter.isValidGame(testGame));
    }

    @Test
    public void isValidGameShouldFailWrongGame() {
        mock = new MockConnectActivity();
        presenter = new ConnectPresenter(mock);
        presenter.start();

        String testGame = "APPLE";
        assertFalse(presenter.isValidGame(testGame));
    }

    @Test
    public void createGameShouldCallView() {
        mock = new MockConnectActivity();
        presenter = new ConnectPresenter(mock);
        presenter.start();

        presenter.createGame("123.123.123.123", "12345", "0");
        assertTrue(mock.mockWasCalled());
    }

    @Test
    public void joinGameShouldCallView() {
        mock = new MockConnectActivity();
        presenter = new ConnectPresenter(mock);
        presenter.start();

        presenter.joinGame("123.123.123.123", "12345","ABC");
        assertTrue(mock.mockWasCalled());
    }

    @Test
    public void scanUserNameShouldCallView() {
        mock = new MockConnectActivity();
        presenter = new ConnectPresenter(mock);
        presenter.start();

        presenter.scanUserName();
        assertTrue(mock.mockWasCalled());
    }

    @Test
    public void getUserNameShouldGetCorrectUserName() {
        mock = new MockConnectActivity();
        presenter = new ConnectPresenter(mock);
        presenter.start();

        String testUserName = "apple";
        presenter.setUserName(testUserName);
        assertEquals(testUserName, presenter.getUserName());
    }

    @Test
    public void hasUserNameSetShouldTellIfUserNameSet() {
        mock = new MockConnectActivity();
        presenter = new ConnectPresenter(mock);
        presenter.start();

        String testUserName = "banana";
        presenter.setUserName(testUserName);
        assertTrue(presenter.hasUserNameSet());
    }

    @Test
    public void hasUserNameSetShouldBeFalseWhenNoUserName() {
        mock = new MockConnectActivity();
        presenter = new ConnectPresenter(mock);
        presenter.start();

        assertFalse(presenter.hasUserNameSet());
    }
}
