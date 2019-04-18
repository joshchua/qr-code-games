package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient;

import org.junit.Before;
import org.junit.Test;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.create.CreateContract;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.create.CreatePresenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CreatePresenterTest {
    public class MockCreateActivity implements CreateContract.View {

        private boolean mWasCalled = false;

        @Override
        public void sendCreateGameRequest(String ip, int port, String userName, int game, int options) {
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
        public void setPresenter(CreateContract.Presenter presenter) {
            mWasCalled = true;
        }

        public boolean wasCalled() {
            return mWasCalled;
        }
    }

    public MockCreateActivity mock;

    public CreatePresenter presenter;

    @Before
    public void init() {
        mock = new MockCreateActivity();
        presenter = new CreatePresenter(mock);
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
    public void createGameShouldCallView() {
        String ip = "1.160.10.240";
        String port = "12345";
        String gameCode = "ABC123";
        String count = "0";
        presenter.createGame(ip, port, gameCode, count);
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
