package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.connect;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.GameActivity;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.R;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.ClientService;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.lobby.LobbyActivity;

public class ConnectActivity extends GameActivity implements ConnectContract.View {

    private ConnectContract.Presenter mPresenter;

    private EditText txtIp;
    private EditText txtPort;
    private EditText txtUserName;
    private EditText txtGameCode;

    private Button btnCreateGame;
    private Button btnJoinGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        Intent intent = new Intent(this, ClientService.class);
        startService(intent);

        mPresenter = new ConnectPresenter(this);

        btnCreateGame = findViewById(R.id.btn_createGame);
        btnJoinGame = findViewById(R.id.btn_joinGame);

        txtIp = findViewById(R.id.editText_ipAddress);
        txtPort = findViewById(R.id.editText_port);
        txtUserName = findViewById(R.id.editText_userName);
        txtGameCode = findViewById(R.id.editText_gameCode);

        btnCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String game = "0";
                String ip = txtIp.getText().toString();
                String port = txtPort.getText().toString();
                String userName = txtUserName.getText().toString();
                mPresenter.createGame(ip, port, userName, game);
            }
        });

        btnJoinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = txtIp.getText().toString();
                String port = txtPort.getText().toString();
                String userName = txtUserName.getText().toString();
                String gameCode = txtGameCode.getText().toString();
                mPresenter.joinGame(ip, port, userName, gameCode);
            }
        });
    }


    @Override
    protected void handleGameEvent(Bundle bundle) {
        String key = (String)bundle.get("key");
        if(key.equals("lobby_received")) {
            Intent intent = new Intent(this, LobbyActivity.class);
            intent.removeExtra("key");
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void setPresenter(ConnectContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void sendCreateGameRequest(String ip, int port, String userName, int game) {
        // Disable buttons for 5 seconds to wait for connection attempt
        btnCreateGame.setEnabled(false);
        btnJoinGame.setEnabled(false);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                btnCreateGame.setEnabled(true);
                btnJoinGame.setEnabled(true);
            }
        }, 5000);

        gameService.connectAndCreateGame(ip, port, userName, game);
    }

    @Override
    public void sendJoinGameRequest(String ip, int port, String userName, String gameCode) {
        // Disable buttons for 5 seconds to wait for connection attempt
        btnCreateGame.setEnabled(false);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                btnCreateGame.setEnabled(true);
                btnJoinGame.setEnabled(true);
            }
        }, 5000);

        gameService.connectAndJoinGame(ip, port, userName, gameCode);
    }

    @Override
    public void showError(String name) {

    }
}
