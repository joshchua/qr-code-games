package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.game;

public class GamePresenter implements GameContract.Presenter{

    private GameContract.View view;

    private String userName;

    public GamePresenter(GameContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void handleScan(String barcodeValue) {
        view.sendScanRequest(barcodeValue);
    }

    @Override
    public void start() {

    }
}
