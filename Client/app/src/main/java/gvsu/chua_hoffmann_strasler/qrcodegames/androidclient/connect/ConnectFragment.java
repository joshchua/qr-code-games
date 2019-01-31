package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.connect;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.R;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.ClientService;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.ClientService.ClientServiceBinder;

public class ConnectFragment extends Fragment implements ConnectContract.View {

    private ConnectContract.Presenter mPresenter;
    private ClientService mService;
    private boolean mBound = false;

    public ConnectFragment() {
        // Required empty public constructor
    }

    private class ResponseHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ClientService.NEW_GAME_CREATED:
                    break;
                case ClientService.GAME_JOINED:
                    break;
            }
        }
    }

    public static ConnectFragment newInstance() {
        return new ConnectFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), ClientService.class);
        //getActivity().startService(intent);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void setPresenter(ConnectContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            ClientServiceBinder binder = (ClientServiceBinder) service;
            mService = binder.getService();
            mService.setReceiver(new Messenger(new ResponseHandler()));
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    public void sendCreateGameRequest(String ip, int port, String userName, int game) {
        mService.connectAndCreateGame(userName, game, ip, port);
    }

    @Override
    public void sendJoinGameRequest(String ip, int port, String userName, String gameCode) {
        mService.connectAndJoinGame(userName, gameCode, ip, port);
    }

    @Override
    public void showError(String name) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_connect, container, false);

        Button btnCreateGame = root.findViewById(R.id.btn_createGame);

        btnCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtIp = root.findViewById(R.id.editText_ipAddress);
                String ip = txtIp.getText().toString();

                EditText txtPort = root.findViewById(R.id.editText_port);
                String port = txtPort.getText().toString();

                EditText txtUserName = root.findViewById(R.id.editText_userName);
                String userName = txtUserName.getText().toString();

                String game = "0";

                mPresenter.createGame(ip, port, userName, game);
            }
        });

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getActivity().unbindService(mConnection);
    }
}
