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
import android.support.v4.content.LocalBroadcastManager;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.Network.CreateGame;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.Network.JoinGame;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.Network.SwitchTeam;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.Network.Lobby;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.Network.JoinGameErrorResult;


import java.io.IOException;

public class ClientService extends Service {

    private final IBinder mBinder = new ClientServiceBinder();
    private HandlerThread mHandlerThread;
    private Looper mOutgoingNetworkLooper;
    private OutgoingNetworkHandler mOutNetHandler;
    private Client client;
    private boolean mIsClientConnected = false;
    private boolean mIsInGame = false;

    // For outgoing network requests, as network calls cannot be on the main thread
    private final class OutgoingNetworkHandler extends Handler {
        public OutgoingNetworkHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: // Connect to Server
                    ConnectionInfo info = (ConnectionInfo)msg.obj;
                    try {
                        client.connect(5000, info.ip, Network.PORT);
                        if (client.isConnected())
                            mIsClientConnected = true;
                    } catch (IOException ex) {

                    }
                    break;
                case 1: // Send an object to the server
                    client.sendTCP(msg.obj);
                    break;
            }
        }
    }

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
            public void received(Connection conn, Object obj) {
                if (obj instanceof Lobby) {
                    mIsInGame = true;
                    Lobby lobby = (Lobby)obj;
                    String eventName = "lobby_received";
                    Bundle bundle = new Bundle();
                    bundle.putString("gameCode", lobby.gameCode);
                    bundle.putString("gameName", lobby.gameName);
                    bundle.putStringArray("team1", lobby.team1);
                    bundle.putStringArray("team2", lobby.team2);
                    sendToActivity(eventName, bundle);
                }

                if (obj instanceof JoinGameErrorResult) {
                    JoinGameErrorResult error = (JoinGameErrorResult)obj;
                    String eventName = "join_game_error";
                    Bundle bundle = new Bundle();
                    bundle.putString("message", error.message);
                    sendToActivity(eventName, bundle);
                }
            }

            public void disconnected(Connection connection) {
                mIsClientConnected = false;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public class ClientServiceBinder extends Binder {
        public ClientService getService() {
            return ClientService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        mHandlerThread.quit();
    }

    private void sendToActivity(String messageName, Bundle extras) {
        Intent intent = new Intent("game_event_received");
        intent.putExtra("key", messageName);
        intent.putExtras(extras);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void connectAndCreateGame(String ip, int port, final String userName, final int game) {
        connectToServer(ip, port);
        // Wait a second to ensure that client is connected to server
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mIsClientConnected && !mIsInGame) {
                    CreateGame createGame = new CreateGame();
                    createGame.game = game;
                    createGame.userName = userName;
                    Message msg = mOutNetHandler.obtainMessage(1);
                    msg.obj = createGame;
                    mOutNetHandler.sendMessage(msg);
                }
            }
        }, 1000);

    }

    public void connectAndJoinGame(String ip, int port, final String userName, final String gameCode) {
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

    public void switchTeam(final String userName, final String gameCode) {
                    SwitchTeam switchTeam = new SwitchTeam();
                    switchTeam.gameCode = gameCode;
                    switchTeam.userName = userName;
                    Message msg = mOutNetHandler.obtainMessage(1);
                    msg.obj = switchTeam;
                    mOutNetHandler.sendMessage(msg);
    }



    private void connectToServer(String ip, int port) {
        Message msg = mOutNetHandler.obtainMessage(0);
        msg.obj = new ConnectionInfo(ip, port);
        mOutNetHandler.sendMessage(msg);
    }
}
