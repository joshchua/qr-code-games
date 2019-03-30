package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseActivity;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.R;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.create.CreateActivity;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.join.JoinActivity;


/**
 * The starting activity where users can register and connect to a server to
 * join or create a new game.
 */
public class WelcomeActivity extends BaseActivity
        implements WelcomeContract.View {

    /**
     * The CreateActivity's Presenter.
     */
    private WelcomeContract.Presenter mPresenter;


    private TextView welcomeName;
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
        final Intent intent = getIntent();

        mPresenter =  new WelcomePresenter(this);

        welcomeName = findViewById(R.id.welcomeName);

        mPresenter.setUserName(intent.getStringExtra("userName"));

        Button createGameBtn = findViewById(R.id.createGameBtn);
        createGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.createGame();
            }
        });
        Button joinGameBtn = findViewById(R.id.joinGameBtn);
        joinGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.joinGame();
            }
        });

    }


    /**
     * Displays the given username by changing the TextView.
     *
     * @param userName The user's username
     */
    @Override
    public void showUserName(final String userName) {
        welcomeName.setText("Welcome, " + userName);
    }

    @Override
    public void createGame() {
        Intent intent = new Intent(this, CreateActivity.class);
        intent.putExtra("userName",mPresenter.getUserName());
        startActivity(intent);
        finish();
    }
    @Override
    public void joinGame() {
        Intent intent = new Intent(this, JoinActivity.class);
        intent.putExtra("userName",mPresenter.getUserName());
        startActivity(intent);
        finish();
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
    public void setPresenter(final WelcomeContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
