package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.Network.CreateGame;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.Network.JoinGame;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.Network.SwitchTeam;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.Network.Lobby;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.Network.JoinGameErrorResult;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.Network.StartGame;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.Network.GameEvent;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.Network.Scan;


import java.io.IOException;

/**
 * The service used to facilitate communication between this client app and
 * the game server.
 */
public class ClientService extends Service {

    /**
     * The binder to that binds Activities to this service for activity to
     * service communication.
     */
    private final IBinder mBinder = new ClientServiceBinder();

    /**
     * The second thread used for outgoing network calls.
     */
    private HandlerThread mHandlerThread;

    /**
     * The looper keeping the second thread alive.
     */
    private Looper mOutgoingNetworkLooper;

    /**
     * The handler for the HandlerThread used for outgoing network calls.
     */
    private OutgoingNetworkHandler mOutNetHandler;

    /**
     * The Kryonet client that communicates with the game server.
     */
    private Client client;

    /**
     * If the client is connected to the game server.
     */
    private boolean mIsClientConnected = false;

    /**
     * If the user is in a lobby/game.
     */
    private boolean mIsInGame = false;

    /**
     * A custom handler for a HandlerThread used for outgoing network calls.
     */
    private final class OutgoingNetworkHandler extends Handler {
        /**
         * Creates an OutgoingNetworkHandler.
         *
         * @param looper The looper to keep the message loop alive
         */
        OutgoingNetworkHandler(final Looper looper) {
            super(looper);
        }

        /**
         * Handles messages sent to this worker thread.
         *
         * @param msg The message delegating work for this thread
         */
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case 0: // Connect to Server
                    ConnectionInfo info = (ConnectionInfo) msg.obj;
                    try {
                        client.connect(5000, info.getIp(), Network.PORT);
                        if (client.isConnected()) {
                            mIsClientConnected = true;
                        }
                    } catch (IOException ex) {

                    }
                    break;
                case 1: // Send an object to the server
                    client.sendTCP(msg.obj);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Called when this service is created.
     */
    @Override
    public void onCreate() {
        mHandlerThread = new HandlerThread("QRGamesClientService",
                Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();

        mOutgoingNetworkLooper = mHandlerThread.getLooper();
        mOutNetHandler = new OutgoingNetworkHandler(mOutgoingNetworkLooper);

        client = new Client();
        Network.register(client);
        client.start();

        client.addListener(new Listener() {
            public void received(final Connection conn, final Object obj) {
                if (obj instanceof Lobby) {
                    mIsInGame = true;
                    Lobby lobby = (Lobby) obj;
                    String eventName = "lobby_received";
                    Bundle bundle = new Bundle();
                    bundle.putString("gameCode", lobby.gameCode);
                    bundle.putString("gameName", lobby.gameName);
                    bundle.putStringArray("team1", lobby.team1);
                    bundle.putStringArray("team2", lobby.team2);
                    sendToActivity(eventName, bundle);
                }

                if (obj instanceof JoinGameErrorResult) {
                    JoinGameErrorResult error = (JoinGameErrorResult) obj;
                    String eventName = "join_game_error";
                    Bundle bundle = new Bundle();
                    bundle.putString("message", error.message);
                    sendToActivity(eventName, bundle);
                }

                if (obj instanceof GameEvent) {
                    GameEvent ge = (GameEvent) obj;
                    String eventName = "game_event";
                    Bundle bundle = new Bundle();
                    bundle.putString("message", ge.message);
                    bundle.putBoolean("is_playing", ge.isPlaying);
                    sendToActivity(eventName, bundle);
                }
            }

            public void disconnected(final Connection connection) {
                mIsClientConnected = false;
            }
        });
    }

    /**
     * Called when this service is called to be a started service.
     *
     * @param intent The intent calling this service
     * @param flags Service flags
     * @param startId The start id
     * @return Specifies the process for this service
     */
    @Override
    public int onStartCommand(final Intent intent, final int flags,
                              final int startId) {
        return START_STICKY;
    }

    /**
     * A custom binder used to bind this service on an activity.
     */
    public class ClientServiceBinder extends Binder {
        /**
         * Gets the service.
         *
         * @return This ClientService
         */
        public ClientService getService() {
            return ClientService.this;
        }
    }

    /**
     * Called when the activity is bound to this service.
     *
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(final Intent intent) {
        return mBinder;
    }

    /**
     * Called when this service is destroyed.
     */
    @Override
    public void onDestroy() {
        mHandlerThread.quit();
    }

    /**
     * Sends a Local Broadcast to listening activities.
     *
     * @param messageName The key message
     * @param extras The bundle holding relevant extras
     */
    private void sendToActivity(final String messageName, final Bundle extras) {
        Intent intent = new Intent("game_event_received");
        intent.putExtra("key", messageName);
        intent.putExtras(extras);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * Connects to the game server.
     *
     * @param ip IP Address
     * @param port Port
     */
    private void connectToServer(final String ip, final int port) {
        Message msg = mOutNetHandler.obtainMessage(0);
        msg.obj = new ConnectionInfo(ip, port);
        mOutNetHandler.sendMessage(msg);
    }

    /**
     * Attempts to connect to the server, and if successful, will create a new
     * game.
     *
     * @param ip IP address
     * @param port Port
     * @param userName Username
     * @param game Type of game to be created
     * @param options Extra options like number of treasures
     */
    public void connectAndCreateGame(final String ip, final int port,
                                     final String userName, final int game,
                                     final int options) {
        connectToServer(ip, port);
        // Wait a second to ensure that client is connected to server
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mIsClientConnected && !mIsInGame) {
                    CreateGame createGame = new CreateGame();
                    createGame.game = game;
                    createGame.userName = userName;
                    createGame.options = options;
                    Message msg = mOutNetHandler.obtainMessage(1);
                    msg.obj = createGame;
                    mOutNetHandler.sendMessage(msg);
                }
            }
        }, 1000);

    }

    /**
     * Attempts to connect to the server, and if successful, will join an
     * existing session.
     *
     * @param ip IP address
     * @param port Port
     * @param userName The user's username
     * @param gameCode The game code of the existing session
     */
    public void connectAndJoinGame(final String ip, final int port,
                                   final String userName,
                                   final String gameCode) {
        connectToServer(ip, port);
        // Wait a second to ensure that client is connected to the server
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mIsClientConnected && !mIsInGame) {
                    JoinGame joinGame = new JoinGame();
                    joinGame.gameCode = gameCode;
                    joinGame.userName = userName;
                    Message msg = mOutNetHandler.obtainMessage(1);
                    msg.obj = joinGame;
                    mOutNetHandler.sendMessage(msg);
                }
            }
        }, 1000);
    }

    /**
     * Switches the user's team.
     *
     * @param userName The user's username
     * @param gameCode The session that the user is in
     */
    public void switchTeam(final String userName, final String gameCode) {
        SwitchTeam switchTeam = new SwitchTeam();
        switchTeam.gameCode = gameCode;
        switchTeam.userName = userName;
        Message msg = mOutNetHandler.obtainMessage(1);
        msg.obj = switchTeam;
        mOutNetHandler.sendMessage(msg);
    }

    /**
     * Starts the game.
     *
     * @param gameCode The game code of the session
     */
    public void startGame(final String gameCode) {
        Message msg = mOutNetHandler.obtainMessage(1);
        StartGame startGame = new StartGame();
        startGame.gameCode = gameCode;
        msg.obj = startGame;
        mOutNetHandler.sendMessage(msg);
    }

    /**
     * Tells the server that this user has scanned something.
     *
     * @param scanned The value of the QR code the user scanned
     */
    public void sendScan(final String scanned) {
        Message msg = mOutNetHandler.obtainMessage(1);
        Scan scan = new Scan();
        scan.scanned = scanned;
        msg.obj = scan;
        mOutNetHandler.sendMessage(msg);
    }
}
