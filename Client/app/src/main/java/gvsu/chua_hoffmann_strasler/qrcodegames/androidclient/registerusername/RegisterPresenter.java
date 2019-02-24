package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.registerusername;

/**
 * Presenter for register activity
 */
public class RegisterPresenter implements RegisterContract.Presenter {
    private String userName;

    private RegisterContract.View view;

    /**
     * Initializes the connection between the Register View and this presenter
     *
     * @param view The Register View to be bound to this presenter
     */
    public RegisterPresenter(RegisterContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    /**
     * Receives the string from the QR code and registers it as a user name
     * @param barcodeValue value of the scanned code
     */
    @Override
    public void handleScan(String barcodeValue) {
        view.sendUserName(barcodeValue);
    }

    /**
     * Start from the base presenter
     */
    @Override
    public void start() { }
}
