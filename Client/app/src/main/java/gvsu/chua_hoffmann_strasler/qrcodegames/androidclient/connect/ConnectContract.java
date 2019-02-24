package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.connect;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BasePresenter;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseView;

/**
 * The contract binding the Connect View and Presenter using the MVP pattern.
 */
public interface ConnectContract {
    /**
     * The Connect View interface.
     */
    interface View extends BaseView<Presenter> {
        /**
         * Attempts to connect to the game server, and if successful, will
         * create a new game.
         *
         * @param ip The IP address of the server
         * @param port The server's port reserved for this game
         * @param userName The user's username
         * @param game The game the user wishes to play
         */
        void sendCreateGameRequest(String ip, int port, String userName,
                                   int game);

        /**
         * Attempts to connect to the game server, and if successful, will join
         * an existing game.
         *
         * @param ip The IP address of the server
         * @param port The server's port reserved for this game
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
         * Starts the RegisterActivity, so a user can scan their username.
         */
        void showScanner();

        /**
         * Get's the the text from the IP address text box.
         *
         * @return IP address string
         */
        String getIpAddress();

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
         * Checks if the given game string is a valid game that can be played
         * via this app.
         *
         * @param game The string to be checked
         * @return If the given game is a valid game
         */
        boolean isValidGame(String game);

        /**
         * Checks if the given inputs are valid, and if so, attempts to connect
         * to the server and join an existing session.
         *
         * @param ip IP address
         * @param port Port
         * @param gameCode Game code of the existing session
         */
        void joinGame(String ip, String port, String gameCode);

        /**
         * Checks if the given inputs are valid, and if so, attempts to connect
         * to the server and create a new game.
         *
         * @param ip IP address
         * @param port Port
         * @param game The type of game to be created
         */
        void createGame(String ip, String port, String game);

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
