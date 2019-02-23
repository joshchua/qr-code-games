package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.registerusername;

public class RegisterPresenter implements RegisterContract.Presenter {
    private String userName;

    private RegisterContract.View view;

    public RegisterPresenter(RegisterContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void handleScan(String barcodeValue) {
        view.sendUserName(barcodeValue);
    }

    @Override
    public void start() { }
}
