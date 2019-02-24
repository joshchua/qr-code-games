package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.game;

import java.util.ArrayList;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BasePresenter;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseView;

/**
 * The contract binding the Game View and Presenter using the MVP pattern
 */
public interface GameContract {
    /**
     * The Game View interface
     */
    interface View extends BaseView<Presenter> {
        void sendScanRequest(String barcodeValue);
        void showGameEvent();
    }
    /**
     * The Game Presenter interface
     */
    interface Presenter extends BasePresenter {
        void handleScan(String barcodeValue);
        void handleGameEvent(String gameEvent, boolean isPlaying);
        void gameOver();
        ArrayList<String> getGameEventList();
    }
}
