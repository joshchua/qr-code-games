package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;



import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.game.GameContract;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.game.GamePresenter;

public class GamePresenterTest {
    public class MockGameActivity implements GameContract.View {

        private boolean mWasCalled = false;

        private boolean mSendScanRequestWasCalled = false;

        @Override
        public void sendScanRequest(String barcodeValue) {
            mWasCalled = true;
            mSendScanRequestWasCalled = true;
        }

        @Override
        public void showGameEvent() {
            mWasCalled = true;
        }

        @Override
        public void setPresenter(GameContract.Presenter presenter) {
            mWasCalled = true;
        }

        public boolean wasCalled() {
            return mWasCalled;
        }

        public boolean sendScanRequestWasCalled() {
            return mSendScanRequestWasCalled;
        }
    }

    public MockGameActivity mock;
    public GamePresenter presenter;

    @Test
    public void handleScanShouldCallView() {
        mock = new MockGameActivity();
        presenter = new GamePresenter(mock);
        presenter.start();

        presenter.handleScan("barcode");
        assertTrue(mock.wasCalled());
    }

    @Test
    public void handleGameEventShouldCallView() {
        mock = new MockGameActivity();
        presenter = new GamePresenter(mock);
        presenter.start();

        presenter.handleGameEvent("gameEvent", true);
        assertTrue(mock.wasCalled());
    }

    @Test
    public void getGameEventListShouldReturnCorrectList() {
        mock = new MockGameActivity();
        presenter = new GamePresenter(mock);
        presenter.start();

        String testEvent1 = "test 1";
        String testEvent2 = "test 2";

        ArrayList<String> expected = new ArrayList<>();
        expected.add(testEvent2);
        expected.add(testEvent1);

        presenter.handleGameEvent(testEvent1, true);
        presenter.handleGameEvent(testEvent2, true);

        assertEquals(expected, presenter.getGameEventList());
    }

    @Test
    public void gameOverShouldNotCallViewOnHandleScanWhenGameOver() {
        mock = new MockGameActivity();
        presenter = new GamePresenter(mock);
        presenter.start();

        String testEvent1 = "test 1";

        presenter.handleGameEvent(testEvent1, false);
        presenter.handleScan("test");
        assertFalse(mock.sendScanRequestWasCalled());
    }

}
