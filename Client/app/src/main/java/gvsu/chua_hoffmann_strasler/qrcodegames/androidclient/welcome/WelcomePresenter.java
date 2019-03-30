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
     * Sets the given username.
     *
     * @param userName The user's username
     */
    public void setUserName(final String userName) {
        mUserName = userName;
        mWelcomeView.showUserName(userName);
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
     * Checks if there is a username set.
     *
     * @return If there is a username set
     */
    public boolean hasUserNameSet() {
        return mUserName != null;
    }


    @Override
    public void createGame() {
        mWelcomeView.createGame();
    }
    @Override
    public void joinGame() {
      mWelcomeView.joinGame();
    }


    @Override
    public void start(){ }


}

