package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.join;


/**
 * The presenter for the Join Activity holding all presentation logic.
 */
public class JoinPresenter implements JoinContract.Presenter {

    /**
     * The Join View.
     */
    private JoinContract.View mJoinView;

    /**
     * The user's username.
     */
    private String userName;

    /**
     * Initializes the Connection between the Join View and this presenter.
     *
     * @param JoinView The Join View to be bound to this presenter
     */
    public JoinPresenter(final JoinContract.View JoinView) {
        mJoinView = JoinView;
        mJoinView.setPresenter(this);
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
     * Checks if the given inputs are valid, and if so, attempts to Join to
     * the server and join an existing session.
     *
     * @param ip       IP address
     * @param port     Port
     * @param gameCode Game code of the existing session
     */
    @Override
    public void joinGame(final String ip, final String port,
                         final String gameCode) {
        if (!isValidIPAddress(ip)) {
            mJoinView.showError("The provided IP address is not valid");
            return;
        }


        if (!isValidPort(port)) {
            mJoinView.showError("The port provided is not valid");
            return;
        }
        try {
            mJoinView.sendJoinGameRequest(ip, Integer.parseInt(port),
                    userName, gameCode);
        } catch (InterruptedException e) {
            System.out.println(e);
        }

    }

    /**
     * Gets the user's username.
     *
     * @return The user's username
     */
    @Override
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the given username.
     *
     * @param userName The user's username
     */
    @Override
    public void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * Used to initialize the presenter in testing.
     */
    @Override
    public void start() {

    }

    /**
     * Returns to previous activity on back button press
     */
    @Override
    public void getBack() {
        mJoinView.getBack();
    }


}
