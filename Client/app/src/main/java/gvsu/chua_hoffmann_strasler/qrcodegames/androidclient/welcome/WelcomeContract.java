package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.welcome;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BasePresenter;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseView;

/**
 * The contract binding the Connect View and Presenter using the MVP pattern.
 */
public interface WelcomeContract {
    /**
     * The Connect View interface.
     */
    interface View extends BaseView<Presenter> {


        /**
         * Displays the given username by changing the TextView.
         *
         * @param userName The user's username
         */
        void showUserName(String userName);

        void createGame();

        void joinGame();

    }

    /**
     * The Connect Presenter interface.
     */
    interface Presenter extends BasePresenter {


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

        void createGame();

        void joinGame();
    }
}
