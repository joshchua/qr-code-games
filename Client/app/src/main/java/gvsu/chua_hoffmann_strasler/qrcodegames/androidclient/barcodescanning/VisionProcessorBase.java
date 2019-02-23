package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.barcodescanning;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.GuardedBy;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Abstract base class for ML Kit frame processors. Subclasses need to implement {@link
 * #onSuccess(Bitmap, Object, FrameMetadata, GraphicOverlay)} to define what they want to with
 * the detection results and {@link #detectInImage(FirebaseVisionImage)} to specify the detector
 * object.
 *
 * @param <T> The type of the detected feature.
 */
public abstract class VisionProcessorBase<T> implements VisionImageProcessor {


    /**
     * To keep the latest images and its metadata.
     */
    @GuardedBy("this")
    private ByteBuffer latestImage;

    /**
     * The metadata for the latest image
     */
    @GuardedBy("this")
    private FrameMetadata latestImageMetaData;

    /**
     * To keep the images and metadata in process.
     */
    @GuardedBy("this")
    private ByteBuffer processingImage;

    /**
     * The metadata for the processing image
     */
    @GuardedBy("this")

    private FrameMetadata processingMetaData;

    /**
     * Creates a new VisionProcessorBase
     */
    public VisionProcessorBase() {
    }

    /**
     * Processes an image
     *
     * @param data The image data
     * @param frameMetadata Metadata about the frame
     * @param graphicOverlay The overlay used to draw
     */
    @Override
    public synchronized void process(
            ByteBuffer data, final FrameMetadata frameMetadata, final GraphicOverlay
            graphicOverlay) {
        latestImage = data;
        latestImageMetaData = frameMetadata;
        if (processingImage == null && processingMetaData == null) {
            processLatestImage(graphicOverlay);
        }
    }

    /**
     * Processes a static bitmap
     * @param bitmap The static bitmap to be scanned
     * @param graphicOverlay The graphic overlay used to draw
     */
    @Override
    public void process(Bitmap bitmap, final GraphicOverlay
            graphicOverlay) {
        detectInVisionImage(null /* bitmap */, FirebaseVisionImage.fromBitmap(bitmap), null,
                graphicOverlay);
    }

    /**
     * Processes the latest image
     *
     * @param graphicOverlay The graphic overlay used to draw
     */
    private synchronized void processLatestImage(final GraphicOverlay graphicOverlay) {
        processingImage = latestImage;
        processingMetaData = latestImageMetaData;
        latestImage = null;
        latestImageMetaData = null;
        if (processingImage != null && processingMetaData != null) {
            processImage(processingImage, processingMetaData, graphicOverlay);
        }
    }

    /**
     * Processes an image from a byte stream
     *
     * @param data The image data
     * @param frameMetadata The frame metadata
     * @param graphicOverlay The graphic overlay used to draw the graphic
     */
    private void processImage(
            ByteBuffer data, final FrameMetadata frameMetadata,
            final GraphicOverlay graphicOverlay) {
        FirebaseVisionImageMetadata metadata =
                new FirebaseVisionImageMetadata.Builder()
                        .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                        .setWidth(frameMetadata.getWidth())
                        .setHeight(frameMetadata.getHeight())
                        .setRotation(frameMetadata.getRotation())
                        .build();

        Bitmap bitmap = BitmapUtils.getBitmap(data, frameMetadata);
        detectInVisionImage(
                bitmap, FirebaseVisionImage.fromByteBuffer(data, metadata), frameMetadata,
                graphicOverlay);
    }

    /**
     * Detects a barcode in the image
     *
     * @param originalCameraImage
     * @param image
     * @param metadata
     * @param graphicOverlay
     */
    private void detectInVisionImage(
            final Bitmap originalCameraImage,
            FirebaseVisionImage image,
            final FrameMetadata metadata,
            final GraphicOverlay graphicOverlay) {
        detectInImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<T>() {
                            @Override
                            public void onSuccess(T results) {
                                VisionProcessorBase.this.onSuccess(originalCameraImage, results,
                                        metadata,
                                        graphicOverlay);
                                processLatestImage(graphicOverlay);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                VisionProcessorBase.this.onFailure(e);
                            }
                        });
    }

    /**
     * Called when the processor is stopped
     */
    @Override
    public void stop() {
    }

    /**
     * To be implemented by child classes to detect objects in the image
     *
     * @param image
     * @return
     */
    protected abstract Task<T> detectInImage(FirebaseVisionImage image);

    /**
     * Callback that executes with a successful detection result.
     *
     * @param originalCameraImage hold the original image from camera, used to draw the background
     *                            image.
     */
    protected abstract void onSuccess(
            @Nullable Bitmap originalCameraImage,
            @NonNull T results,
            @NonNull FrameMetadata frameMetadata,
            @NonNull GraphicOverlay graphicOverlay);

    /**
     * To be implemented by child classes to handle failures
     *
     * @param e The exception caught
     */
    protected abstract void onFailure(@NonNull Exception e);
}
