package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.game;

import java.util.ArrayList;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BasePresenter;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseView;

/**
 * The contract binding the Game View and Presenter using the MVP pattern.
 */
public interface GameContract {
    /**
     * The Game View interface.
     */
    interface View extends BaseView<Presenter> {

        /**
         * Sends the value of the barcode scan.
         *
         * @param barcodeValue value of the scan
         */
        void sendScanRequest(String barcodeValue);

        /**
         * Updates the adapter with a new event in the game.
         */
        void showGameEvent();
    }

    /**
     * The Game Presenter interface.
     */
    interface Presenter extends BasePresenter {

        /**
         * Sends scan request for barcode.
         *
         * @param barcodeValue value of the scanned code
         */
        void handleScan(String barcodeValue);

        /**
         * Called when this activity receives a Local Broadcast from the
         * ClientService.
         *
         * @param gameEvent     name of event the game received
         * @param isGamePlaying determines whether a game is in progress or not
         */
        void handleGameEvent(String gameEvent, boolean isGamePlaying);


        /**
         * Set the game to be over.
         */
        void gameOver();

        /**
         * Gets the array list of game events.
         *
         * @return array list of game events
         */
        ArrayList<String> getGameEventList();
    }
}
