package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.barcodescanning;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.google.android.gms.common.images.Size;

import java.io.IOException;

/** Preview the camera image in the screen. */
public class CameraSourcePreview extends ViewGroup {

    /**
     * The tag for this object used for debug logs
     */
    private static final String TAG = "QRCodeGames:Preview";

    /**
     * The application context
     */
    private Context context;

    /**
     * Surface view
     */
    private SurfaceView surfaceView;

    /**
     * If a start has been requested
     */
    private boolean startRequested;

    /**
     * If there is a surface to draw to that is available
     */
    private boolean surfaceAvailable;

    /**
     * The imaging data from the camera
     */
    private CameraSource cameraSource;

    /**
     * The overlay used to draw graphics on top of the overlay
     */
    private GraphicOverlay overlay;

    /**
     * Creates a preview for the camera with MLKit detection
     *
     * @param context The application context
     * @param attrs A collection of attributes for this custom view
     */
    public CameraSourcePreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        startRequested = false;
        surfaceAvailable = false;

        surfaceView = new SurfaceView(context);
        surfaceView.getHolder().addCallback(new SurfaceCallback());
        addView(surfaceView);
    }

    /**
     * Starts the camera stream of image data
     *
     * @param cameraSource The source of image data
     * @throws IOException
     */
    public void start(CameraSource cameraSource) throws IOException {
        if (cameraSource == null) {
            stop();
        }

        this.cameraSource = cameraSource;

        if (this.cameraSource != null) {
            startRequested = true;
            startIfReady();
        }
    }

    /**
     * Starts the camera stream of image data
     *
     * @param cameraSource The source of image data
     * @param overlay The graphic overlay used for drawing
     * @throws IOException
     */
    public void start(CameraSource cameraSource, GraphicOverlay overlay) throws IOException {
        this.overlay = overlay;
        start(cameraSource);
    }

    /**
     * Stops the camera
     */
    public void stop() {
        if (cameraSource != null) {
            cameraSource.stop();
        }
    }

    /**
     * Release the camera
     */
    public void release() {
        if (cameraSource != null) {
            cameraSource.release();
            cameraSource = null;
        }
    }

    /**
     * Starts the camera if the camera is ready
     *
     * @throws IOException
     */
    @SuppressLint("MissingPermission")
    private void startIfReady() throws IOException {
        if (startRequested && surfaceAvailable) {
            cameraSource.start();
            if (overlay != null) {
                Size size = cameraSource.getPreviewSize();
                int min = Math.min(size.getWidth(), size.getHeight());
                int max = Math.max(size.getWidth(), size.getHeight());
                if (isPortraitMode()) {
                    // Swap width and height sizes when in portrait, since it will be rotated by
                    // 90 degrees
                    overlay.setCameraInfo(min, max, cameraSource.getCameraFacing());
                } else {
                    overlay.setCameraInfo(max, min, cameraSource.getCameraFacing());
                }
                overlay.clear();
            }
            startRequested = false;
        }
    }

    /**
     * The callback for the SurfaceHolder
     */
    private class SurfaceCallback implements SurfaceHolder.Callback {
        /**
         * Called when the surface is created
         *
         * @param surface The surface holder used
         */
        @Override
        public void surfaceCreated(SurfaceHolder surface) {
            surfaceAvailable = true;
            try {
                startIfReady();
            } catch (IOException e) {
                Log.e(TAG, "Could not start camera source.", e);
            }
        }

        /**
         * Called when the surface is destroyed
         *
         * @param surface The surface holder used
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder surface) {
            surfaceAvailable = false;
        }

        /**
         * Called when the surface is changed
         *
         * @param holder The holder for the surface
         * @param format The format for the surface
         * @param width The width of the surface
         * @param height The height of the surface
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
    }

    /**
     * Called when this view group is on the layout
     *
     * @param changed If the view has been changed
     * @param left The left position
     * @param top The top position
     * @param right The right position
     * @param bottom The bottom position
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = 320;
        int height = 240;
        if (cameraSource != null) {
            Size size = cameraSource.getPreviewSize();
            if (size != null) {
                width = size.getWidth();
                height = size.getHeight();
            }
        }

        // Swap width and height sizes when in portrait, since it will be rotated 90 degrees
        if (isPortraitMode()) {
            int tmp = width;
            width = height;
            height = tmp;
        }

        final int layoutWidth = right - left;
        final int layoutHeight = bottom - top;

        // Computes height and width for potentially doing fit width.
        int childWidth = layoutWidth;
        int childHeight = (int) (((float) layoutWidth / (float) width) * height);

        // If height is too tall using fit width, does fit height instead.
        if (childHeight > layoutHeight) {
            childHeight = layoutHeight;
            childWidth = (int) (((float) layoutHeight / (float) height) * width);
        }

        for (int i = 0; i < getChildCount(); ++i) {
            getChildAt(i).layout(0, 0, childWidth, childHeight);
            Log.d(TAG, "Assigned view: " + i);
        }

        try {
            startIfReady();
        } catch (IOException e) {
            Log.e(TAG, "Could not start camera source.", e);
        }
    }

    /**
     * Checks if the device is in portrait mode
     *
     * @return If the device is in portrait
     */
    private boolean isPortraitMode() {
        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false;
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return true;
        }

        Log.d(TAG, "isPortraitMode returning false by default");
        return false;
    }
}
