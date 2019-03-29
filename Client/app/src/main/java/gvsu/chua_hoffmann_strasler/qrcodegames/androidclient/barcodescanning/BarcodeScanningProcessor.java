package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.barcodescanning;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.IOException;
import java.util.List;

/**
 * Processes barcodes using MLKit.
 */
public abstract class BarcodeScanningProcessor extends
        VisionProcessorBase<List<FirebaseVisionBarcode>> {

    /**
     * This class' tag.
     */
    private static final String TAG = "BarcodeScanProc";

    /**
     * The MLKit barcode detector.
     */
    private final FirebaseVisionBarcodeDetector detector;

    /**
     * Creates a new BarcodeScanningProcessor.
     */
    public BarcodeScanningProcessor() {
        // Note that if you know which format of barcode your app is dealing
        // with, detection will be faster to specify the supported barcode
        // formats one by one, e.g.
        // new FirebaseVisionBarcodeDetectorOptions.Builder()
        //     .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
        //     .build();
        detector = FirebaseVision.getInstance().getVisionBarcodeDetector();
    }

    /**
     * A callback to be called in classes extending BarcodeScanningProcessor
     * holding the raw value of the barcode.
     *
     * @param rawValue The raw text of the barcode
     */
    public abstract void scanCallback(String rawValue);

    /**
     * Called when the processor is closed.
     */
    @Override
    public void stop() {
        try {
            detector.close();
        } catch (IOException e) {
            Log.e(TAG, "Exception thrown while trying to close Barcode"
                    + " Detector: " + e);
        }
    }

    /**
     * Asynchronously detects barcodes in a given image.
     *
     * @param image The image to be scanned
     * @return A task for the detector to look for a barcode in an image
     */
    @Override
    protected Task<List<FirebaseVisionBarcode>> detectInImage(
            final FirebaseVisionImage image) {
        return detector.detectInImage(image);
    }

    /**
     * Called when there is a successful scan.
     *
     * @param originalCameraImage hold the original image from camera, used to
     *                           draw the background
     * @param barcodes The list of MLKit barcodes
     * @param frameMetadata Metadata about the current frame
     * @param graphicOverlay The overlay used to draw on top of the original
     *                      image
     */
    @Override
    protected void onSuccess(
            @Nullable final Bitmap originalCameraImage,
            @NonNull final List<FirebaseVisionBarcode> barcodes,
            @NonNull final FrameMetadata frameMetadata,
            @NonNull final GraphicOverlay graphicOverlay) {
        graphicOverlay.clear();
        if (originalCameraImage != null) {
            CameraImageGraphic imageGraphic = new CameraImageGraphic(
                    graphicOverlay, originalCameraImage);
            graphicOverlay.add(imageGraphic);
        }
        for (int i = 0; i < barcodes.size(); ++i) {
            FirebaseVisionBarcode barcode = barcodes.get(i);
            BarcodeGraphic barcodeGraphic = new BarcodeGraphic(graphicOverlay,
                    barcode);
            graphicOverlay.add(barcodeGraphic);
            String rawValue = barcode.getRawValue();
            scanCallback(rawValue);
        }
        graphicOverlay.postInvalidate();
    }

    /**
     * Logs the error in debug when there is a failure with barcode detection.
     *
     * @param e The exception to be logged
     */
    @Override
    protected void onFailure(@NonNull final Exception e) {
        Log.e(TAG, "Barcode detection failed " + e);
    }
}
