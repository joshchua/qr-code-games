package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.connect;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseActivity;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.R;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.ClientService;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.lobby.LobbyActivity;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.registerusername.RegisterActivity;

public class ConnectActivity extends BaseActivity implements ConnectContract.View {

    private ConnectContract.Presenter mPresenter;

    private EditText txtIp;
    private EditText txtPort;
    private EditText txtGameCode;

    private Button btnCreateGame;
    private Button btnJoinGame;
    private Button btnRegisterUserName;

    private TextView tvUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        Intent intent = new Intent(this, ClientService.class);
        startService(intent);

        mPresenter = new ConnectPresenter(this);

        btnCreateGame = findViewById(R.id.btn_createGame);
        btnJoinGame = findViewById(R.id.btn_joinGame);
        btnRegisterUserName = findViewById(R.id.btn_registerUserName);

        txtIp = findViewById(R.id.editText_ipAddress);
        txtPort = findViewById(R.id.editText_port);
        txtGameCode = findViewById(R.id.editText_gameCode);

        tvUserName = findViewById(R.id.tv_userName);

        btnRegisterUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.scanUserName();
            }
        });

        btnCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String game = "0";
                String ip = txtIp.getText().toString();
                String port = txtPort.getText().toString();
                mPresenter.createGame(ip, port, game);
            }
        });

        btnJoinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = txtIp.getText().toString();
                String port = txtPort.getText().toString();
                String gameCode = txtGameCode.getText().toString();
                mPresenter.joinGame(ip, port, gameCode);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Intent intent = getIntent();
        if (intent != null) {
            String key = intent.getStringExtra("key");
            if (key == null) return;
            if (key.equals("scanned_username")) {
                String userName = intent.getStringExtra("username");
                mPresenter.setUserName(userName);
            }
        }
    }

    @Override
    public String getIpAddress() {
        return txtIp.getText().toString();
    }

    @Override
    public void showUserName(String userName) {
        tvUserName.setText("You set your username to \""+ userName +"\".");
    }

    @Override
    public void setConnectBtnEnabled(boolean isEnabled) {
        btnCreateGame.setEnabled(isEnabled);
        btnJoinGame.setEnabled(isEnabled);
    }

    @Override
    protected void handleGameEvent(Bundle bundle) {
        String key = bundle.getString("key");
        if (key.equals("lobby_received")) {
            Intent intent = new Intent(this, LobbyActivity.class);
            intent.removeExtra("key");
            intent.putExtras(bundle);
            intent.putExtra("userName", mPresenter.getUserName());
            intent.putExtra("ip", getIpAddress());
            startActivity(intent);
            finish();
        } else if (key.equals("join_game_error")) {
            Toast.makeText(this, bundle.getString("message"),
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void setPresenter(ConnectContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void sendCreateGameRequest(String ip, int port, String userName, int game) {
        // Disable buttons for 5 seconds to wait for connection attempt
        setConnectBtnEnabled(false);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                setConnectBtnEnabled(true);
            }
        }, 5000);

        gameService.connectAndCreateGame(ip, port, userName, game);
    }

    @Override
    public void sendJoinGameRequest(String ip, int port, String userName, String gameCode) {
        // Disable buttons for 5 seconds to wait for connection attempt
        setConnectBtnEnabled(false);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                setConnectBtnEnabled(true);
            }
        }, 5000);

        gameService.connectAndJoinGame(ip, port, userName, gameCode);
    }

    @Override
    public void showError(String name) {
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showScanner() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("key", "register_username");
        startActivity(intent);
    }
}
