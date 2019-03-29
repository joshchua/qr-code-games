package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseActivity;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.R;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.ClientService;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.registerusername.RegisterActivity;

/**
 * The starting activity where users can register and connect to a server to
 * join or create a new game.
 */
public class WelcomeActivity extends BaseActivity
        implements CreateContract.View {

    /**
     * The CreateActivity's Presenter.
     */
    private CreateContract.Presenter mPresenter;

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
        setContentView(R.layout.activity_welcome);

        Intent intent = new Intent(this, ClientService.class);
        startService(intent);

        mPresenter = new CreatePresenter(this);
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
