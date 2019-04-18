package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.welcome;

/**
 * The presenter for the Connect Activity holding all presentation logic.
 */
public class WelcomePresenter implements WelcomeContract.Presenter {

    /**
     * The Connect View.
     */
    private WelcomeContract.View mWelcomeView;

    /**
     * The user's username.
     */
    private String mUserName;

    /**
     * Initializes the connection between the Connect View and this presenter.
     *
     * @param welcomeView The Connect View to be bound to this presenter
     */
    public WelcomePresenter(final WelcomeContract.View welcomeView) {
        mWelcomeView = welcomeView;
        mWelcomeView.setPresenter(this);
    }

    /**
     * Gets the user's username.
     *
     * @return The user's username
     */
    public String getUserName() {
        return mUserName;
    }

    /**
     * Sets the given username.
     *
     * @param userName The user's username
     */
    public void setUserName(final String userName) {
        mUserName = userName;
        mWelcomeView.showUserName(userName);
    }

    /**
     * Opens Create new game activity.
     */
    @Override
    public void createGame() {
        mWelcomeView.createGame();
    }

    /**
     * Opens Join game activity.
     */
    @Override
    public void joinGame() {
        mWelcomeView.joinGame();
    }

    /**
     * Basic presenter method.
     */
    @Override
    public void start() {
    }


}

