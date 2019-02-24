package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.registerusername;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BasePresenter;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseView;

/**
 * The contract binding the Register View and Presenter using the MVP pattern
 */
public interface RegisterContract {
    /**
     * * The Register View interface
     */
    interface View extends BaseView<Presenter> {
        void sendUserName(String userName);
    }
    /**
     * The Register Presenter interface
     */
    interface Presenter extends BasePresenter {
        void handleScan(String barcodeValue);
    }
}
