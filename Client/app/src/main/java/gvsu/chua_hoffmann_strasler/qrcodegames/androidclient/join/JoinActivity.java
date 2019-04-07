package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.join;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseActivity;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.R;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.lobby.LobbyActivity;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.welcome.WelcomeActivity;

/**
 * The starting activity where users can register and connect to a server to
 * join or create a new game.
 */
public class JoinActivity extends BaseActivity
        implements JoinContract.View {

    /**
     * The CreateActivity's Presenter.
     */
    private JoinContract.Presenter mPresenter;

    /**
     * TextView holding IP Address
     */
    private TextView ipAddressView;

    /**
     * TextView holding IP Address
     */
    private TextView portNumberView;

    /**
     * TextView holding Game Code
     */
    private TextView gameCodeView;

    /**
     * Sends request to join a game on the server
     */
    private Button joinGameBtn;


    /**
     * Called when this activity is created.
     *
     * @param savedInstanceState The bundle saved from previous instances of
     *                           this activity
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        final Intent intent = getIntent();
        startService(intent);

        mPresenter = new JoinPresenter(this);
        mPresenter.setUserName(intent.getStringExtra("userName"));


        ipAddressView = findViewById(R.id.ipAddressView);
        portNumberView = findViewById(R.id.portNumberView);
        gameCodeView = findViewById(R.id.gameCodeView);

        joinGameBtn = findViewById(R.id.joinGameBtn);
        joinGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.joinGame(ipAddressView.getText().toString(),
                        portNumberView.getText().toString(), gameCodeView.getText().toString());
            }
        });
    }

    /**
     * Returns to previous activity on back button press
     */
    @Override
    public void onBackPressed() {
        mPresenter.getBack();
    }


    /**
     * Called when this activity receives a Local Broadcast from the
     * ClientService.
     *
     * @param bundle The bundle holding relevant extras
     */
    @Override
    protected void handleGameEvent(final Bundle bundle) {
        String key = bundle.getString("key");
        if (key != null && key.equals("lobby_received")) {
            Intent intent = new Intent(this, LobbyActivity.class);
            intent.removeExtra("key");
            intent.putExtras(bundle);
            intent.putExtra("userName", mPresenter.getUserName());
            intent.putExtra("ip", ipAddressView.getText().toString());
            startActivity(intent);
            finish();
        } else if (key != null && key.equals("join_game_error")) {
            Toast.makeText(this, bundle.getString("message"),
                    Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Sets this activity's presenter.
     *
     * @param presenter The presenter for this activity
     */
    @Override
    public void setPresenter(final JoinContract.Presenter presenter) {
        mPresenter = presenter;
    }


    /**
     * Attempts to connect to the game server, and if successful, will join an
     * existing game.
     *
     * @param ip       The IP address of the server
     * @param port     The server's port reserved for this game
     * @param userName The user's username
     * @param gameCode The game code of the existing session on the server
     */
    @Override
    public void sendJoinGameRequest(final String ip, final int port,
                                    final String userName,
                                    final String gameCode) {
        gameService.connectAndJoinGame(ip, port, userName, gameCode);
        joinGameBtn.setEnabled(false);
        joinGameBtn.setText(R.string.joining);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                joinGameBtn.setText(R.string.joinGame);
                joinGameBtn.setEnabled(true);
            }
        }, 5000);

    }

    /**
     * Displays a Toast with given error message.
     *
     * @param error The error message
     */
    @Override
    public void showError(final String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }


    /**
     * Returns to previous activity on back button press
     */
    @Override
    public void getBack() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.putExtra("userName", mPresenter.getUserName());
        startActivity(intent);
        finish();
    }
}
