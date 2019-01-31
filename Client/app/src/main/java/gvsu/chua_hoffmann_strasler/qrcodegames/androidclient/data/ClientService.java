package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.Network.CreateGame;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.Network.JoinGame;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.Network.JoinTeam;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.Network.Lobby;


import java.io.IOException;

public class ClientService extends Service {

    private final IBinder mBinder = new ClientServiceBinder();

    private HandlerThread mHandlerThread;

    private Looper mServiceLooper;

    private ServiceHandler mServiceHandler;

    private Client client;

    private boolean mIsClientConnected = false;

    private boolean mHasJoinedGame = false;

    private Messenger mMsgReceiver;

    public static final int NEW_GAME_CREATED = 1;
    public static final int GAME_JOINED = 2;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
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

        mServiceLooper = mHandlerThread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        client = new Client();
        Network.register(client);
        client.start();

        client.addListener(new Listener() {
            public void received(Connection conn, Object obj) {
                if (obj instanceof Lobby) {
                    Message msg = Message.obtain();
                    msg.what = NEW_GAME_CREATED;
                    sendToReceiver(msg);
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

    public void setReceiver(Messenger messenger) {
        mMsgReceiver = messenger;
    }

    private void sendToReceiver(Message msg) {
        try {
            mMsgReceiver.send(msg);
        } catch (RemoteException ex) {

        }
    }

    public void connectAndCreateGame(String userName, int game, String ip, int port) {
        connect(ip, port);
        if (mIsClientConnected) {
            CreateGame createGame = new CreateGame();
            createGame.game = game;
            createGame.userName = userName;
            Message msg = mServiceHandler.obtainMessage(1);
            msg.obj = createGame;
            mServiceHandler.sendMessage(msg);
        }
    }

    public void connectAndJoinGame(String userName, String gameCode, String ip, int port) {
        connect(ip, port);
        if (mIsClientConnected) {
            JoinGame joinGame = new JoinGame();
            joinGame.gameCode = gameCode;
            joinGame.userName = userName;
            Message msg = mServiceHandler.obtainMessage(1);
            msg.obj = joinGame;
            mServiceHandler.sendMessage(msg);
        }
    }

    public void joinTeam(int team) {
        JoinTeam joinTeam = new JoinTeam();
        joinTeam.team = team;
        Message msg = mServiceHandler.obtainMessage(1);
        msg.obj = joinTeam;
        mServiceHandler.sendMessage(msg);
    }

    private void connect(String ip, int port) {
        Message msg = mServiceHandler.obtainMessage(0);
        msg.obj = new ConnectionInfo(ip, port);
        mServiceHandler.sendMessage(msg);
    }




}
