package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.create;


/**
 * The presenter for the Connect Activity holding all presentation logic.
 */
public class CreatePresenter implements CreateContract.Presenter {

    /**
     * The Connect View.
     */
    private CreateContract.View mCreateView;

    /**
     * The user's username.
     */
    private String mUserName;

    /**
     * Initializes the connection between the Connect View and this presenter.
     *
     * @param createView The Connect View to be bound to this presenter
     */
    public CreatePresenter(final CreateContract.View createView) {
        mCreateView = createView;
        mCreateView.setPresenter(this);
    }


    /**
     * Checks if the given string is a valid IP address.
     *
     * @param ip The IP address to be checked
     * @return If the given string is a valid IP address
     */
    @Override
    public boolean isValidIPAddress(final String ip) {
        String strPattern = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        return ip.matches(strPattern);
    }

    /**
     * Checks if the given port is a valid port.
     *
     * @param port The integer to be checked
     * @return If the given integer is a valid port
     */
    @Override
    public boolean isValidPort(final String port) {
        return port.matches("\\d+");
    }


    /**
     * Checks if the given inputs are valid, and if so, attempts to connect to
     * the server and create a new game.
     *
     * @param ip   IP address
     * @param port Port
     * @param game The type of game to be created + number of items
     */
    @Override
    public void createGame(final String ip, final String port,
                           final String game, final String count) {

        String gameNum = "0";

        if (game.equals("Treasure Hunt")) {
            gameNum = "1";
        }

        if (!isValidIPAddress(ip)) {
            mCreateView.showError("The provided IP address is not valid");
            return;
        }

        if (!isValidPort(port)) {
            mCreateView.showError("The port provided is not valid");
            return;
        }

        mCreateView.sendCreateGameRequest(ip, Integer.parseInt(port),
                mUserName, Integer.parseInt(gameNum), Integer.parseInt(count));
    }

    /**
     * Gets the user's username.
     *
     * @return The user's username
     */
    @Override
    public String getUserName() {
        return mUserName;
    }

    /**
     * Sets the given username.
     *
     * @param userName The user's username
     */
    @Override
    public void setUserName(final String userName) {
        mUserName = userName;
    }

    /**
     * Used to initialize the presenter in testing.
     */
    @Override
    public void start() {

    }

    /**
     * Returns to previous activity on back button press.
     */
    @Override
    public void getBack() {
        mCreateView.getBack();
    }

}
