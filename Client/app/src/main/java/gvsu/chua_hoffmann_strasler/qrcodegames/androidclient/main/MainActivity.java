package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseActivity;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.R;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.ClientService;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.registerusername.RegisterActivity;

/**
 * The starting activity where users can register and connect to a server to
 * join or create a new game.
 */
public class MainActivity extends BaseActivity
        implements MainContract.View {

    /**
     * The CreateActivity's Presenter.
     */
    private MainContract.Presenter mPresenter;

    /**
     * Called when this activity is created.
     *
     * @param savedInstanceState The bundle saved from previous instances of
     *                          this activity
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, ClientService.class);
        startService(intent);

        mPresenter = new MainPresenter(this);


        Button openQRScanBtn = findViewById(R.id.openQRScanBtn);
        openQRScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mPresenter.scanUserName();
            }
        });

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
    public void setPresenter(final MainContract.Presenter presenter) {
        mPresenter = presenter;
    }


    /**
     * Starts the RegisterActivity, so a user can scan their username.
     */
    @Override
    public void showScanner() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("key", "register_username");
        startActivity(intent);
        finish();
    }
}
