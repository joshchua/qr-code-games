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

    private Button btnCreateGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        Intent intent = new Intent(this, ClientService.class);
        startService(intent);

        mPresenter = new ConnectPresenter(this);

        btnCreateGame = findViewById(R.id.btn_createGame);

        btnCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtIp = findViewById(R.id.editText_ipAddress);
                String ip = txtIp.getText().toString();

                EditText txtPort = findViewById(R.id.editText_port);
                String port = txtPort.getText().toString();

                EditText txtUserName = findViewById(R.id.editText_userName);
                String userName = txtUserName.getText().toString();

                String game = "0";

                mPresenter.createGame(ip, port, userName, game);
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
        btnCreateGame.setEnabled(false);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                btnCreateGame.setEnabled(true);
            }
        }, 5000);
        gameService.connectAndCreateGame(ip, port, userName, game);
    }

    @Override
    public void sendJoinGameRequest(String ip, int port, String userName, String gameCode) {
        gameService.connectAndJoinGame(ip, port, userName, gameCode);
    }

    @Override
    public void showError(String name) {

    }
}
