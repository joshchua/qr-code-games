package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.lobby;

import android.content.Intent;
import android.os.Bundle;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseActivity;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.R;

public class LobbyActivity extends BaseActivity implements LobbyContract.View{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        Intent intent = getIntent();
        // do stuff with received information
    }

    @Override
    public void setPresenter(LobbyContract.Presenter presenter) {

    }

    @Override
    public void handleGameEvent(Bundle bundle) {

    }
}
