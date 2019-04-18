package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.registerusername;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.R;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.barcodescanning.BarcodeScanningProcessor;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.barcodescanning.CameraSource;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.barcodescanning.CameraSourcePreview;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.barcodescanning.GraphicOverlay;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.welcome.WelcomeActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Register activity that is created when users start scanning their name
 * from the connect activity.
 */
public class RegisterActivity extends AppCompatActivity
        implements RegisterContract.View {

    /**
     * Tag containing name of the activity.
     */
    private static final String TAG = "RegisterActivity";

    /**
     * Number of permissions requested.
     */
    private static final int PERMISSION_REQUESTS = 1;

    /**
     * Presenter for the register activity.
     */
    private RegisterContract.Presenter presenter;

    /**
     * Camera source.
     */
    private CameraSource cameraSource = null;

    /**
     * Camera preview.
     */
    private CameraSourcePreview preview;

    /**
     * Graphic overlay.
     */
    private GraphicOverlay graphicOverlay;

    /**
     * Call this when the activity is created.
     * @param savedInstanceState The bundle saved from previous instances of
     *                          this activity.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_register);

        presenter = new RegisterPresenter(this);

        preview = findViewById(R.id.firePreview);
        if (preview == null) {
            Log.d(TAG, "Preview is null");
        }
        graphicOverlay = findViewById(R.id.fireFaceOverlay);
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
     * Sets this activity's presenter.
     *
     * @param presenter The presenter for this activity
     */
    @Override
    public void setPresenter(final RegisterContract.Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Creates camera source if the is not one.
     */
    private void createCameraSource() {
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
        }

        try {
            Log.i(TAG, "Using Barcode Detector Processor");
            cameraSource.setMachineLearningFrameProcessor(
                    new BarcodeScanningProcessor() {
                @Override
                public void scanCallback(final String rawValue) {
                    presenter.handleScan(rawValue);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "can not create camera source");
        }
    }

    /**
     * Starts or restarts the camera source, if it exists. If the camera
     * source doesn't exist yet (e.g., because onResume was called before the
     * camera source was created), this will be called again when the camera
     * source is created.
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
     * Resumes the camera.
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
     * Releases the camera source.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    /**
     * Asks the user for permissions.
     * @return a permission
     */
    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(),
                                    PackageManager.GET_PERMISSIONS);
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
     * Finds if all permissions needed are granted.
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
     * Asks the user for each permission specifically.
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
                    this, allNeededPermissions.toArray(new String[0]),
                    PERMISSION_REQUESTS);
        }
    }

    /**
     * Gets the response to the permission request from the user.
     * @param requestCode The request code passed in
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     */
    @Override
    public void onRequestPermissionsResult(
            final int requestCode, final String[] permissions,
            final int[] grantResults) {
        Log.i(TAG, "Permission granted!");
        if (allPermissionsGranted()) {
            createCameraSource();
        }
        super.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }

    /**
     * Checks if permission is granted.
     * @param context Context why permission is asked
     * @param permission One of the permissions
     * @return True if permission granted, False if not
     */
    private static boolean isPermissionGranted(
           final Context context, final String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted: " + permission);
            return true;
        }
        Log.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }

    /**
     * Adds the user name to an intents and returns back to the connect
     * activity.
     * @param userName name of the user
     */
    @Override
    public void sendUserName(final String userName) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.putExtra("key", "scanned_username");
        intent.putExtra("userName", userName);
        startActivity(intent);
        finish();
    }
}
