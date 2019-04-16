package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.main.MainContract;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.main.MainPresenter;

public class MainPresenterTest {
    public class MockMainActivity implements MainContract.View {

        private boolean mWasCalled = false;

        @Override
        public void showScanner() {
            mWasCalled = true;
        }

        @Override
        public void setPresenter(MainContract.Presenter presenter) {
            mWasCalled = true;
        }

        public boolean wasCalled() {
            return mWasCalled;
        }
    }

    public MockMainActivity mock;

    public MainPresenter presenter;

    @Before
    public void initialize() {
        mock = new MockMainActivity();
        presenter = new MainPresenter(mock);
        presenter.start();
    }

    @Test
    public void scanUserNameShouldCallView() {
        presenter.scanUserName();
        assertTrue(mock.wasCalled());
    }
}
