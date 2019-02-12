package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.GameActivity;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.R;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.connect.ConnectPresenter;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.data.ClientService;

public class LobbyActivity extends GameActivity implements LobbyContract.View{

    private LobbyContract.Presenter mPresenter;

    private TextView gameID;

    private ListView listTeam1;
    private ListView listTeam2;

    private Button btnSwitchTeams;
    private Button btnStartGame;

    private ArrayList<String> teamArray1;
    private ArrayList<String> teamArray2;

    private ArrayAdapter<String> adapter1;
    private ArrayAdapter<String> adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        final Intent intent = getIntent();

        mPresenter = new LobbyPresenter(this);

        gameID = findViewById(R.id.gameID);
        gameID.setText(intent.getStringExtra("gameCode"));

        //update team list
        listTeam1 = findViewById(R.id.list_Team1);
        listTeam2 = findViewById(R.id.list_Team2);
        teamArray1= new ArrayList<>();
        teamArray2= new ArrayList<>();
        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,teamArray1);
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,teamArray2);
        listTeam1.setAdapter(adapter1);
        listTeam2.setAdapter(adapter2);

        mPresenter.updateTeams(intent.getStringArrayExtra("team1"),
                intent.getStringArrayExtra("team2"),teamArray1,teamArray2);

        //Switch teams button
        btnSwitchTeams = findViewById(R.id.btn_SwitchTeams);
        btnSwitchTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = intent.getStringExtra("userName");
                String gameCode = intent.getStringExtra("gameCode");
                 mPresenter.switchTeams(userName,gameCode);
            }
        });

        //Start game button
        btnStartGame = findViewById(R.id.btn_StartGame);
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Start game");
            }
        });

    }


    @Override
    public void setPresenter(LobbyContract.Presenter presenter) { mPresenter = presenter; }

    @Override
    public void handleGameEvent(Bundle bundle) {
        String key = (String)bundle.get("key");
        if (key.equals("lobby_received")) {
            mPresenter.updateTeams(bundle.getStringArray("team1"),
                    bundle.getStringArray("team2"),teamArray1,teamArray2);
        }

    }

    @Override
    public void sendSwitchTeamRequest(String userName, String gameCode) {
        gameService.switchTeam(userName,gameCode);
    }

    @Override
    public void updateTeams() {
        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
    }
}
