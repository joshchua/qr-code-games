package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.game;

import android.os.Bundle;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseActivity;

public class GameActivity extends BaseActivity implements GameContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void setPresenter(GameContract.Presenter presenter) {

    }

    @Override
    public void handleGameEvent(Bundle bundle) {

    }
}
