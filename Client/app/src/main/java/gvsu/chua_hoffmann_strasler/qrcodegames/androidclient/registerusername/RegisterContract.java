package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.registerusername;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BasePresenter;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseView;

/**
 * The contract binding the Register View and Presenter using the MVP pattern.
 */
public interface RegisterContract {
    /**
     * * The Register View interface.
     */
    interface View extends BaseView<Presenter> {

        /**
         * Sends username to another activity.
         * @param userName name of the user
         */
        void sendUserName(String userName);
    }
    /**
     * The Register Presenter interface.
     */
    interface Presenter extends BasePresenter {

        /**
         * Handles scanned QR code value.
         * @param barcodeValue value of the scanned QR code
         */
        void handleScan(String barcodeValue);
    }
}
