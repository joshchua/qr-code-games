package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.game;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.BaseActivity;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.R;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.barcodescanning.BarcodeScanningProcessor;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.barcodescanning.CameraSource;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.barcodescanning.CameraSourcePreview;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.barcodescanning.GraphicOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Game activity.
 */
public class GameActivity extends BaseActivity implements GameContract.View {

    /**
     *Used for logging the camera in the acitivity
     */
    private static final String TAG = "GameActivity";
    /**
     * Defines number of the permissions requested
     */
    private static final int PERMISSION_REQUESTS = 1;

    /**
     * Presenter for Game Activity.
     */
    private GameContract.Presenter presenter;

    /**
     * Source of the camera
     */
    private CameraSource cameraSource = null;
    /**
     * Preview of th camera source
     */
    private CameraSourcePreview preview;
    /**
     *Graphic overlay for the camera
     */
    private GraphicOverlay graphicOverlay;

    /**
     * ListView for game events
     */
    private ListView gameEventView;
    /**
     * List of game events
     */
    private ArrayList<String> gameEventList;

    /**
     * Array adapter for the event ListView.
     */
    private ArrayAdapter<String> gameEventAdapter;

    /**
     * Updates the adapter with a new event in the game.
     */
    @Override
    public void showGameEvent() {
        gameEventAdapter.notifyDataSetChanged();
    }

    /**
     * Call this when the activity is created.
     * @param savedInstanceState The bundle saved from previous instances of this activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_game);

        presenter = new GamePresenter(this);

        gameEventView = findViewById(R.id.list_gameEvents);
        gameEventList = presenter.getGameEventList();
        gameEventAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gameEventList);
        gameEventView.setAdapter(gameEventAdapter);

        final Intent startingIntent = getIntent();

        if (startingIntent != null) {
            String startingMessage = startingIntent.getStringExtra("message");
            boolean isPlaying = startingIntent.getBooleanExtra("is_playing", false);
            presenter.handleGameEvent(startingMessage, isPlaying);
        }

        preview = (CameraSourcePreview) findViewById(R.id.firePreview);
        if (preview == null) {
            Log.d(TAG, "Preview is null");
        }
        graphicOverlay = (GraphicOverlay) findViewById(R.id.fireFaceOverlay);
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null");
        }

        if (allPermissionsGranted()) {
            createCameraSource();
        } else {
            getRuntimePermissions();
        }

    }

    /**
     * Sets this activity's presenter
     *
     * @param presenter The presenter for this activity
     */
    @Override
    public void setPresenter(GameContract.Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     *
     */
    private void createCameraSource() {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
        }

        try {
            Log.i(TAG, "Using Barcode Detector Processor");
            cameraSource.setMachineLearningFrameProcessor(new BarcodeScanningProcessor() {
                @Override
                public void scanCallback(String rawValue) {
                    presenter.handleScan(rawValue);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "can not create camera source");
        }
    }

    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null");
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null");
                }
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    /**
     * Resumes the camera
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        startCameraSource();
    }

    /** Stops the camera. */
    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
    }

    /**
     * Called when this activity receives a Local Broadcast from the ClientService
     * @param bundle The bundle holding relevant extras
     */
    @Override
    protected void handleGameEvent(Bundle bundle) {
        String key = bundle.getString("key");
        if (key.equals("game_event")) {
            String gameEvent = bundle.getString("message");
            boolean isPlaying = bundle.getBoolean("is_playing");
            presenter.handleGameEvent(gameEvent, isPlaying);
        }
    }

    /**
     * Releases the camera source
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    /**
     * Asks the user for permissions
     * @return a permission
     */
    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    /**
     * Finds if all permissions needed are granted
     * @return True if all permissions granted, false if at least one is missing
     */
    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Asks the user for each permission specifically
     */
    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    /**
     * Gets the response to the permission request from the user
     * @param requestCode The request code passed in
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        Log.i(TAG, "Permission granted!");
        if (allPermissionsGranted()) {
            createCameraSource();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Checks if permission is granted
     * @param context Context why permission is asked
     * @param permission One of the permissions
     * @return True if permission granted, False if not
     */
    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted: " + permission);
            return true;
        }
        Log.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }

    /**
     * Sends the value of the barcode scan
     * @param barcodeValue value of the scan
     */
    @Override
    public void sendScanRequest(String barcodeValue) {
        gameService.sendScan(barcodeValue);
    }
}
