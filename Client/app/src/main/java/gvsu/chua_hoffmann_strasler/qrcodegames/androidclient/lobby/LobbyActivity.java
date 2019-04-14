package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseActivity;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.R;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.R.string;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.game.GameActivity;

/**
 * Lobby activity that activates when a game is created. It shows all the
 * players in each teams.
 */
public class LobbyActivity extends BaseActivity implements LobbyContract.View {

    /**
     * Presenter for Lobby Activity.
     */
    private LobbyContract.Presenter mPresenter;

    /**
     * Array that holds the names of the players on team 1.
     */
    private ArrayList<String> teamArray1;
    /**
     * Array that holds the names of the players on team 2.
     */
    private ArrayList<String> teamArray2;
    /**
     * Adapter for ListView to update members of team 1.
     */
    private ArrayAdapter<String> adapter1;
    /**
     * Adapter for ListView to update members of the team 2.
     */
    private ArrayAdapter<String> adapter2;

    /**
     * Call this when the activity is created.
     *
     * @param savedInstanceState The bundle saved from previous instances of
     *                           this activity.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        final Intent intent = getIntent();

        mPresenter = new LobbyPresenter(this);
        mPresenter.setTitleAndCode(intent.getStringExtra("gameMode"),
                intent.getStringExtra(
                "gameCode"));

        //set title
        TextView title = findViewById(R.id.title);
        title.setText(mPresenter.getGameTitle());
        //show game code
        TextView gameID = findViewById(R.id.gameID);
        gameID.setText(getString(string.gameCodeTextView,
                mPresenter.getGameCode()));

        //update team list
        ListView listTeam1 = findViewById(R.id.list_Team1);
        ListView listTeam2 = findViewById(R.id.list_Team2);
        teamArray1 = new ArrayList<>();
        teamArray2 = new ArrayList<>();
        adapter1 = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, teamArray1);
        adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, teamArray2);
        listTeam1.setAdapter(adapter1);
        listTeam2.setAdapter(adapter2);

        mPresenter.updateTeams(intent.getStringArrayExtra("team1"),
                intent.getStringArrayExtra("team2"), teamArray1,
                teamArray2);

        //Switch teams button
        Button btnSwitchTeams = findViewById(R.id.btn_SwitchTeams);
        btnSwitchTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String userName = intent.getStringExtra("userName");
                String gameCode = intent.getStringExtra("gameCode");
                mPresenter.switchTeams(userName, gameCode);
            }
        });

        //Start game button
        Button btnStartGame = findViewById(R.id.btn_StartGame);
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mPresenter.startGame();
            }
        });

    }

    /**
     * Sets this activity's presenter.
     *
     * @param presenter The presenter for this activity
     */
    @Override
    public void setPresenter(final LobbyContract.Presenter presenter) {
        mPresenter = presenter;
    }

    /**
     * Called when this activity receives a Local Broadcast from the
     * ClientService.
     *
     * @param bundle The bundle holding relevant extras
     */
    @Override
    public void handleGameEvent(final Bundle bundle) {
        String key = (String) bundle.get("key");
        if (key != null && key.equals("lobby_received")) {
            mPresenter.updateTeams(bundle.getStringArray("team1"),
                    bundle.getStringArray("team2"), teamArray1,
                    teamArray2);
        }

        if (key != null && key.equals("game_event")) {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Sends request to the server to switch teams for a player.
     *
     * @param userName name of the player switching teams
     * @param gameCode ID of the game the player is in
     */
    @Override
    public void sendSwitchTeamRequest(final String userName,
                                      final String gameCode) {
        gameService.switchTeam(userName, gameCode);
    }

    /**
     * Updates lists with the players on the screen.
     */
    @Override
    public void updateTeams() {
        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
    }

    /**
     * Tries to connect to the server and start the game.
     *
     * @param gameCode Unique game ID
     */
    @Override
    public void startGame(final String gameCode) {
        gameService.startGame(gameCode);
    }
}
