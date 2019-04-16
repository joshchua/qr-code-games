package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.create;


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
         * Attempts to connect to the game server, and if successful, will
         * create a new game.
         *
         * @param ip       The IP address of the server
         * @param port     The server's port reserved for this game
         * @param userName The user's username
         * @param game     The game the user wishes to play
         */
        void sendCreateGameRequest(String ip, int port, String userName,
                                   int game);

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
         * to the server and create a new game.
         *
         * @param ip   IP address
         * @param port Port
         * @param game The type of game to be created
         * @param count number of treasures
         */
        void createGame(String ip, String port, String game, String count);

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
