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
import android.widget.Toast;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.ClientService;

/**
 * Base activity that is implemented by all other activities.
 */
public abstract class BaseActivity extends AppCompatActivity {
    /**
     * The service bound to this activity.
     */
    private ClientService gameService;

    /**
     * If the service is bound to this activity.
     */
    private boolean isServiceBound = false;

    /**
     * The LocalBroadcastManager used for connection.
     */
    private LocalBroadcastManager lbm;

    /**
     * Basic function for back button.
     */
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You can't use the back button here.",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Reconnects to the server on resume of the app.
     */
    @Override
    protected void onResume() {
        super.onResume();

        lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(mGameEventReceiver, new IntentFilter(
                "game_event_received"));

        Intent intent = new Intent(this, ClientService.class);
        bindService(intent, mConnection, BIND_NOT_FOREGROUND);
    }

    /**
     * Unbinds connection on pause of the app.
     */
    @Override
    protected void onPause() {
        super.onPause();

        unbindService(mConnection);
        lbm.unregisterReceiver(mGameEventReceiver);
    }

    /**
     * Connect to the server and binds the connection.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName className,
                                       final IBinder service) {
            ClientService.ClientServiceBinder binder =
                    (ClientService.ClientServiceBinder) service;
            gameService = binder.getService();
            isServiceBound = true;
        }

        /**
         * @param name
         */
        @Override
        public void onServiceDisconnected(final ComponentName name) {
            isServiceBound = false;
        }
    };

    /**
     * Handles game event recieved from the client.
     *
     * @param bundle Contains additional infomation from the event
     */
    protected abstract void handleGameEvent(Bundle bundle);

    /**
     *  Recives broadcast from the server and if there is event to be handled,
     *  it sends it to the client.
     */
    private BroadcastReceiver mGameEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent != null) {
                handleGameEvent(intent.getExtras());
            }
        }
    };
}
