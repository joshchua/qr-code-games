package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.connect;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BasePresenter;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseView;

/**
 * The contract binding the Connect View and Presenter using the MVP pattern
 */
public interface ConnectContract {
    /**
     * The Connect View interface
     */
    interface View extends BaseView<Presenter> {
        void sendCreateGameRequest(String ip, int port, String userName, int game);
        void sendJoinGameRequest(String ip, int port, String userName, String gameCode);
        void showError(String name);
        void showScanner();
        String getIpAddress();
        void showUserName(String userName);
        void setConnectBtnEnabled(boolean isEnabled);
    }

    /**
     * The Connect Presenter interface
     */
    interface Presenter extends BasePresenter {
        boolean isValidIPAddress(String ip);
        boolean isValidPort(String port);
        boolean isValidGame(String game);
        void joinGame(String ip, String port, String gameCode);
        void createGame(String ip, String port, String game);
        void scanUserName();
        void setUserName(String userName);
        String getUserName();
        boolean hasUserNameSet();
    }
}
