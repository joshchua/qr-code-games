package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.registerusername;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BasePresenter;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseView;

public interface RegisterContract {
    interface View extends BaseView<Presenter> {
        void sendUserName(String userName);
    }

    interface Presenter extends BasePresenter {
        void handleScan(String barcodeValue);
    }
}
