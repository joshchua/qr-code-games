package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.game;

import java.util.ArrayList;

public class GamePresenter implements GameContract.Presenter{

    private GameContract.View view;

    private ArrayList<String> gameEventList;

    private boolean isGamePlaying;

    public GamePresenter(GameContract.View view) {
        this.view = view;
        this.isGamePlaying = true;
        this.gameEventList = new ArrayList<>();
        view.setPresenter(this);
    }

    @Override
    public void handleScan(String barcodeValue) {
        if (isGamePlaying) {
            view.sendScanRequest(barcodeValue);
        }
    }

    @Override
    public void handleGameEvent(String gameEvent, boolean isGamePlaying) {
        gameEventList.add(0, gameEvent);
        view.showGameEvent();
        this.isGamePlaying = isGamePlaying;

        if (!isGamePlaying) gameOver();
    }

    @Override
    public void gameOver() {
        isGamePlaying = false;
    }

    @Override
    public ArrayList<String> getGameEventList() {
        return gameEventList;
    }

    @Override
    public void start() {

    }
}
