package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.connect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.R;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.util.ActivityUtils;

public class ConnectActivity extends AppCompatActivity {

    private ConnectContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        ConnectFragment connectFragment = (ConnectFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (connectFragment == null) {
            connectFragment = ConnectFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    connectFragment, R.id.contentFrame);
        }

        mPresenter = new ConnectPresenter(connectFragment);
    }
}
