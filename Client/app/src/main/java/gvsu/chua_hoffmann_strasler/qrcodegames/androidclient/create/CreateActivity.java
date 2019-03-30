package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.create;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseActivity;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.R;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.lobby.LobbyActivity;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.welcome.WelcomeActivity;

/**
 * The starting activity where users can register and connect to a server to
 * join or create a new game.
 */
public class CreateActivity extends BaseActivity
        implements CreateContract.View {

    /**
     * The CreateActivity's Presenter.
     */
    private CreateContract.Presenter mPresenter;

    private TextInputEditText ipAddressBox;

    private TextInputEditText portNumberBox;

    private Spinner gameModeBox;
    /**
     * Called when this activity is created.
     *
     * @param savedInstanceState The bundle saved from previous instances of
     *                          this activity
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        final Intent intent = getIntent();
        startService(intent);

        mPresenter = new CreatePresenter(this);
        mPresenter.setUserName(intent.getStringExtra("userName"));

        ipAddressBox = findViewById(R.id.ipAddressBox);
        portNumberBox = findViewById(R.id.portNumberBox);
        gameModeBox = findViewById(R.id.gameModeBox);
    }

    @Override
    public void onBackPressed() {
        mPresenter.getBack();
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
        return ipAddressBox.getText().toString();
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
    public void setPresenter(final CreateContract.Presenter presenter) {
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

        gameService.connectAndCreateGame(ip, port, userName, game);
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



    @Override
    public void getBack() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.putExtra("userName",mPresenter.getUserName());
        startActivity(intent);
        finish();
    }
}
