package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.join;


import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BasePresenter;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseView;

/**
 * The contract binding the Connect View and Presenter using the MVP pattern.
 */
public interface JoinContract {
    /**
     * The Connect View interface.
     */
    interface View extends BaseView<Presenter> {


        /**
         * Attempts to connect to the game server, and if successful, will join
         * an existing game.
         *
         * @param ip       The IP address of the server
         * @param port     The server's port reserved for this game
         * @param userName The user's username
         * @param gameCode The game code of the existing session on the server
         */
        void sendJoinGameRequest(String ip, int port, String userName,
                                 String gameCode);

        /**
         * Displays a Toast with given error message.
         *
         * @param error The error message
         */
        void showError(String error);

        /**
         * Returns to previous activity on back button press.
         */
        void getBack();
    }

    /**
     * The Connect Presenter interface.
     */
    interface Presenter extends BasePresenter {
        /**
         * Checks if the given string is a valid IP address.
         *
         * @param ip The IP address to be checked
         * @return If the given string is a valid IP address
         */
        boolean isValidIPAddress(String ip);

        /**
         * Checks if the given port is a valid port.
         *
         * @param port The integer to be checked
         * @return If the given integer is a valid port
         */
        boolean isValidPort(String port);

        /**
         * Checks if the given inputs are valid, and if so, attempts to connect
         * to the server and join an existing session.
         *
         * @param ip       IP address
         * @param port     Port
         * @param gameCode Game code of the existing session
         */
        void joinGame(String ip, String port, String gameCode);

        /**
         * Gets the user's username.
         *
         * @return The user's username
         */
        String getUserName();

        /**
         * Sets the given username.
         *
         * @param userName The user's username
         */
        void setUserName(String userName);

        /**
         * Returns to previous activity on back button press.
         */
        void getBack();
    }


}
