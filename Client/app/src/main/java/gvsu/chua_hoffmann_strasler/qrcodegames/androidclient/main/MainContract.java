package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.main;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BasePresenter;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseView;

/**
 * The contract binding the Connect View and Presenter using the MVP pattern.
 */
public interface MainContract {
    /**
     * The Connect View interface.
     */
    interface View extends BaseView<Presenter> {

        /**
         * Starts the RegisterActivity, so a user can scan their username.
         */
        void showScanner();

    }

    /**
     * The Connect Presenter interface.
     */
    interface Presenter extends BasePresenter {

        /**
         * Launches the scanner for a user to register their username.
         */
        void scanUserName();



    }
}
