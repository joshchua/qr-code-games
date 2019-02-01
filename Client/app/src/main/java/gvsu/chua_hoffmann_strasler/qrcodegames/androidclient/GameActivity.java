package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.ClientService;

public abstract class GameActivity extends AppCompatActivity {
    protected ClientService gameService;
    protected boolean isServiceBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(mGameEventReceiver, new IntentFilter("game_event_received"));
    }

    protected ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            ClientService.ClientServiceBinder binder = (ClientService.ClientServiceBinder) service;
            gameService = binder.getService();
            isServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
        }
    };

    protected abstract void handleGameEvent(Bundle bundle);

    private BroadcastReceiver mGameEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                handleGameEvent(intent.getExtras());
            }
        }
    };
}
