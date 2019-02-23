package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.game;

import java.util.ArrayList;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BasePresenter;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseView;

public interface GameContract {
    interface View extends BaseView<Presenter> {
        void sendScanRequest(String barcodeValue);
        void showGameEvent();
    }

    interface Presenter extends BasePresenter {
        void handleScan(String barcodeValue);
        void handleGameEvent(String gameEvent);
        ArrayList<String> getGameEventList();
    }
}
