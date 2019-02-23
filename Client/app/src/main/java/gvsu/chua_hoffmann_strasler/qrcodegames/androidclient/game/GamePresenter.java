package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.game;

import java.util.ArrayList;

public class GamePresenter implements GameContract.Presenter{

    private GameContract.View view;

    private String userName;

    private ArrayList<String> gameEventList;

    public GamePresenter(GameContract.View view) {
        this.view = view;
        this.gameEventList = new ArrayList<>();
        view.setPresenter(this);
    }

    @Override
    public void handleScan(String barcodeValue) {
        view.sendScanRequest(barcodeValue);
    }

    @Override
    public void handleGameEvent(String gameEvent) {
        gameEventList.add(0, gameEvent);
        view.showGameEvent();
    }

    @Override
    public ArrayList<String> getGameEventList() {
        return gameEventList;
    }

    @Override
    public void start() {

    }
}
