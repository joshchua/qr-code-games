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
    }

    public MockJoinActivity mock;

    public JoinPresenter presenter;

    @Before
    public void initialize() {

    }
}
