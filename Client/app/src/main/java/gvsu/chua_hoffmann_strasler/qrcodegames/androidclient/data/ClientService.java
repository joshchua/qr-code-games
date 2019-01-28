package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import com.esotericsoftware.kryonet.Client;


import java.io.IOException;

public class ClientService extends Service {

    private final IBinder mBinder = new ClientServiceBinder();

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private Client client;



    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    client = new Client();
                    Network.register(client);
                    client.start();
                    try {
                        client.connect(5000, "ip address here", Network.PORT);
                    } catch (IOException ex) {

                    }
                    break;
                case 1:
                    Network.RegisterUserName name = new Network.RegisterUserName();
                    name.userName = "Josh";
                    client.sendTCP(name);
                    break;
            }

        }
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("QRGamesClientService",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        Message connectMessage = mServiceHandler.obtainMessage(0);
        mServiceHandler.sendMessage(connectMessage);

//        client = new Client();
//        Network.register(client);
//        client.start();
//
//        try {
//            client.connect(5000, "127.0.0.1", Network.PORT);
//        } catch (IOException ex) {
//
//        }
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
    public void onDestroy() {}

    public void test() {
        Message msg = mServiceHandler.obtainMessage(1);
        mServiceHandler.sendMessage(msg);
    }


}
