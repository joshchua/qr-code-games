package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.game;

import java.util.ArrayList;

/**
 * Presenter for Game activity.
 */
public class GamePresenter implements GameContract.Presenter {

    /**
     * Game View.
     */
    private GameContract.View view;

    /**
     * Array list of game events.
     */
    private ArrayList<String> gameEventList;

    /**
     * Determines whether a game is in progress or not.
     */
    private boolean isGamePlaying;

    /**
     * Initializes the connection between the Game View and this presenter.
     *
     * @param view The Game View to be bound to this presenter
     */
    public GamePresenter(final GameContract.View view) {
        this.view = view;
        this.isGamePlaying = true;
        this.gameEventList = new ArrayList<>();
        view.setPresenter(this);
    }

    /**
     * Sends scan request for barcode.
     *
     * @param barcodeValue value of the scanned code
     */
    @Override
    public void handleScan(final String barcodeValue) {
        if (isGamePlaying) {
            view.sendScanRequest(barcodeValue);
        }
    }

    /**
     * Called when this activity receives a Local Broadcast from the
     * ClientService.
     *
     * @param gameEvent     name of event the game received
     * @param isGamePlaying determines whether a game is in progress or not
     */
    @Override
    public void handleGameEvent(final String gameEvent,
                                final boolean isGamePlaying) {
        gameEventList.add(0, gameEvent);
        view.showGameEvent();
        this.isGamePlaying = isGamePlaying;

        if (!isGamePlaying) {
            gameOver();
        }
    }

    /**
     * Set the game to be over.
     */
    @Override
    public void gameOver() {
        isGamePlaying = false;
    }

    /**
     * Gets the array list of game events.
     *
     * @return array list of game events
     */
    @Override
    public ArrayList<String> getGameEventList() {
        return gameEventList;
    }

    /**
     * Start from the base presenter.
     */
    @Override
    public void start() {

    }
}
