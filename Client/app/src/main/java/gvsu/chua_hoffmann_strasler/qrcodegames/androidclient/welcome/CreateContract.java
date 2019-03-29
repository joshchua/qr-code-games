package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.welcome;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BasePresenter;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseView;

/**
 * The contract binding the Connect View and Presenter using the MVP pattern.
 */
public interface CreateContract {
    /**
     * The Connect View interface.
     */
    interface View extends BaseView<Presenter> {

        /**
         * Displays a Toast with given error message.
         *
         * @param error The error message
         */
        void showError(String error);

        /**
         * Starts the RegisterActivity, so a user can scan their username.
         */
        void showScanner();

        /**
         * Displays the given username by changing the TextView.
         *
         * @param userName The user's username
         */
        void showUserName(String userName);

        /**
         * Enables/Disables the connect/join buttons.
         *
         * @param isEnabled If true, the buttons will be enabled. If false, the
         *                 buttons will be disabled.
         */
        void setConnectBtnEnabled(boolean isEnabled);
    }

    /**
     * The Connect Presenter interface.
     */
    interface Presenter extends BasePresenter {





        /**
         * Launches the scanner for a user to register their username.
         */
        void scanUserName();

        /**
         * Sets the given username.
         *
         * @param userName The user's username
         */
        void setUserName(String userName);

        /**
         * Gets the user's username.
         *
         * @return The user's username
         */
        String getUserName();

        /**
         * Checks if there is a username set.
         *
         * @return If there is a username set
         */
        boolean hasUserNameSet();
    }
}
