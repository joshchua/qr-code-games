package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.main;

/**
 * The presenter for the Connect Activity holding all presentation logic.
 */
public class MainPresenter implements MainContract.Presenter {

    /**
     * The Connect View.
     */
    private MainContract.View mMainView;

    /**
     * Initializes the connection between the Connect View and this presenter.
     *
     * @param mainView The Connect View to be bound to this presenter
     */
    protected MainPresenter(final MainContract.View mainView) {
        mMainView = mainView;
        mMainView.setPresenter(this);
    }

    /**
     * Opens QR scanner in register activity.
     */
    @Override
    public void scanUserName() {
        mMainView.showScanner();

    }

    /**
     * Used to initialize the presenter in testing.
     */
    @Override
    public void start() {

    }
}
