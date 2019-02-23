package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient;

import org.junit.Test;
import static org.junit.Assert.*;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.connect.ConnectActivity;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.connect.ConnectContract;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.connect.ConnectPresenter;


public class ConnectPresenterTest {
    public static class MockConnectActivity implements ConnectContract.View {

        public MockConnectActivity() {

        }

        @Override
        public void sendCreateGameRequest(String ip, int port, String userName, int game) {

        }

        @Override
        public void sendJoinGameRequest(String ip, int port, String userName, String gameCode) {

        }

        @Override
        public void showError(String name) {

        }

        @Override
        public void showScanner() {

        }

        @Override
        public String getIpAddress() {
            return null;
        }

        @Override
        public void showUserName(String userName) {

        }

        @Override
        public void setConnectBtnEnabled(boolean isEnabled) {

        }

        @Override
        public void setPresenter(ConnectContract.Presenter presenter) {

        }
    }

    public ConnectPresenter presenter;

    public MockConnectActivity mock;

    @Test
    public void shouldPassCorrectIp() {
        mock = new MockConnectActivity();
        presenter = new ConnectPresenter(mock);
        presenter.start();
    }

    @Test
    public void isValidIPAddressShouldFailWrongIp() {

    }

    @Test
    public void isValidIPAddressShouldPassCorrectPort() {

    }

    @Test
    public void isValidPortShouldFailWrongPort() {

    }

    @Test
    public void isValidPortShouldPassCorrectPort() {

    }

    @Test
    public void isValidGameShouldPassCorrectGame() {

    }

    @Test
    public void joinGameShouldCallView() {

    }

    @Test
    public void createGameShouldCallView() {

    }

    @Test
    public void scanUserNameShouldCallView() {

    }

    @Test
    public void getUserNameShouldGetCorrectUserName() {

    }

    @Test
    public void hasUserNameSetShouldTellIfUserNameSet() {

    }


}
