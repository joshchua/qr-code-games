package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.join;

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
     * The EditText view to hold IP address input.
     */
    private EditText txtIp;

    /**
     * The EditText view to hold port number input.
     */
    private EditText txtPort;
    /**
     * The EditText view to hold gameCode input.
     */
    private EditText txtGameCode;

    /**
     * The Button view used to create a new game.
     */
    private Button btnCreateGame;

    /**
     * The Button view used to join an existing game.
     */
    private Button btnJoinGame;

    /**
     * The Button view to launch the username scanner.
     */
    private Button btnRegisterUserName;

    /**
     * The TextView that displays the user's username.
     */
    private TextView tvUserName;

    /**
     * Called when this activity is created.
     *
     * @param savedInstanceState The bundle saved from previous instances of
     *                          this activity
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Intent intent = new Intent(this, ClientService.class);
        startService(intent);

        mPresenter = new JoinPresenter(this);


    }

    /**
     * Called when this activity is resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        final Intent intent = getIntent();
        if (intent != null) {
            String key = intent.getStringExtra("key");
            if (key == null) {
                return;
            }

            if (key.equals("scanned_username")) {
                String userName = intent.getStringExtra("username");
                mPresenter.setUserName(userName);
            }
        }
    }

    /**
     * Get's the the text from the IP address text box.
     *
     * @return IP address string
     */
    @Override
    public String getIpAddress() {
        return txtIp.getText().toString();
    }

    /**
     * Displays the given username by changing the TextView.
     *
     * @param userName The user's username
     */
    @Override
    public void showUserName(final String userName) {
        tvUserName.setText("You set your username to \"" + userName + "\".");
    }

    /**
     * Enables/Disables the connect/join buttons.
     *
     * @param isEnabled If true, the buttons will be enabled. If false, the
     *                 buttons will be disabled.
     */
    @Override
    public void setConnectBtnEnabled(final boolean isEnabled) {
        btnCreateGame.setEnabled(isEnabled);
        btnJoinGame.setEnabled(isEnabled);
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
     * Attempts to connect to the game server, and if successful, will create
     * a new game.
     *
     * @param ip The IP address of the server
     * @param port The server's port reserved for this game
     * @param userName The user's username
     * @param game The game the user wishes to play
     */
    @Override
    public void sendCreateGameRequest(final String ip, final int port,
                                      final String userName, final int game) {
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

    /**
     * Attempts to connect to the game server, and if successful, will join an
     * existing game.
     *
     * @param ip The IP address of the server
     * @param port The server's port reserved for this game
     * @param userName The user's username
     * @param gameCode The game code of the existing session on the server
     */
    @Override
    public void sendJoinGameRequest(final String ip, final int port,
                                    final String userName,
                                    final String gameCode) {
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
     * Starts the RegisterActivity, so a user can scan their username.
     */
    @Override
    public void showScanner() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("key", "register_username");
        startActivity(intent);
    }
}
