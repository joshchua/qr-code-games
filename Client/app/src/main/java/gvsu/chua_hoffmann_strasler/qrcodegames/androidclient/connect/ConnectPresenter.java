package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.connect;

public class ConnectPresenter implements ConnectContract.Presenter {

    private ConnectContract.View mConnectView;

    public ConnectPresenter(ConnectContract.View connectView) {
        mConnectView = connectView;
        mConnectView.setPresenter(this);
    }

    @Override
    public boolean isValidIPAddress(String ip) {
        return true; // TODO
    }

    @Override
    public boolean isValidPort(String port) {
        return true; // TODO
    }

    @Override
    public boolean isValidGame(String game) {
        try {
            boolean isValid = Integer.parseInt(game) == 0;

            return isValid;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    @Override
    public void joinGame(String ip, String port, String userName, String gameCode) {
        //mConnectView.sendJoinGameRequest(ip, port, userName, gameCode);
    }

    @Override
    public void createGame(String ip, String port, String userName, String game) {
        if (!isValidIPAddress(ip)) {
            mConnectView.showError("The provided IP address is not valid");
            return;
        }


        if (!isValidGame(game)) {
            mConnectView.showError("The game option provided is not valid.");
            return;
        }


        if (!isValidPort(port)) {
            mConnectView.showError("The port provided is not valid");
            return;
        }

        mConnectView.sendCreateGameRequest(ip, Integer.parseInt(port), userName, Integer.parseInt(game));
    }

    @Override
    public void start() {

    }
}
