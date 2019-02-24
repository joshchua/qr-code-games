package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient;

import org.junit.Test;
import static org.junit.Assert.*;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.registerusername.RegisterContract;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.registerusername.RegisterPresenter;

public class RegisterPresenterTest {
    public static class MockRegisterActivity implements RegisterContract.View {
        private boolean mWasCalled = false;

        @Override
        public void sendUserName(String userName) {
            mWasCalled = true;
        }

        @Override
        public void setPresenter(RegisterContract.Presenter presenter) {
            mWasCalled = true;
        }

        public boolean wasCalled() {
            return mWasCalled;
        }
    }

    public MockRegisterActivity mock;

    public RegisterPresenter presenter;

    @Test
    public void handleScanShouldCallView() {
        mock = new MockRegisterActivity();
        presenter = new RegisterPresenter(mock);
        presenter.start();

        presenter.handleScan("test");
        assertTrue(mock.wasCalled());
    }

}
