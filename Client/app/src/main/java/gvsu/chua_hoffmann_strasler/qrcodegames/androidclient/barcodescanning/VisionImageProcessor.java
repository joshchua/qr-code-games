package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.barcodescanning;

import android.graphics.Bitmap;

import com.google.firebase.ml.common.FirebaseMLException;

import java.nio.ByteBuffer;

/** An interface to process the images with different ML Kit detectors and
 *  custom image models. */
public interface VisionImageProcessor {

    /** Processes the images with the underlying machine learning models.
     * @param data The data associated
     * @param frameMetadata The frame metadata
     * @param graphicOverlay The camera overlay
     * @throws FirebaseMLException Firebase exception
     */
    void process(ByteBuffer data, FrameMetadata frameMetadata,
                 GraphicOverlay graphicOverlay)
            throws FirebaseMLException;

    /**
     * Processes the bitmap images.
     * @param bitmap The bitmap to process
     * @param graphicOverlay The overlay to use
     */

    void process(Bitmap bitmap, GraphicOverlay graphicOverlay);

    /** Stops the underlying machine learning model and release resources. */
    void stop();
}
