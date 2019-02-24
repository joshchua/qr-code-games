package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.barcodescanning;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera.CameraInfo;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;


/**
 * Utils functions for bitmap conversions.
 */
public class BitmapUtils {


    /**
     * Gets a bitmap from a ByteBuffer of a live stream of image data.
     *
     * @param data The image data
     * @param metadata The frame metadata
     * @return The bitmap returned.
     */
    @Nullable
    public static Bitmap getBitmap(final ByteBuffer data,
                                   final FrameMetadata metadata) {
        data.rewind();
        byte[] imageInBuffer = new byte[data.limit()];
        data.get(imageInBuffer, 0, imageInBuffer.length);
        try {
            YuvImage image =
                    new YuvImage(
                            imageInBuffer, ImageFormat.NV21,
                            metadata.getWidth(), metadata.getHeight(),
                            null);
            if (image != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compressToJpeg(new Rect(0, 0,
                        metadata.getWidth(), metadata.getHeight()),
                        80, stream);

                Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(),
                        0, stream.size());

                stream.close();
                return rotateBitmap(bmp, metadata.getRotation(),
                        metadata.getCameraFacing());
            }
        } catch (Exception e) {
            Log.e("VisionProcessorBase", "Error: " + e.getMessage());
        }
        return null;
    }


    /**
     * Rotates the bitmap.
     *
     * @param bitmap The bitmap to be rotated
     * @param rotation The angle of the rotation in degrees
     * @param facing The front/back facing camera
     * @return The rotated bitmap
     */
    private static Bitmap rotateBitmap(final Bitmap bitmap, final int rotation,
                                       final int facing) {
        Matrix matrix = new Matrix();
        int rotationDegree = 0;
        switch (rotation) {
            case FirebaseVisionImageMetadata.ROTATION_90:
                rotationDegree = 90;
                break;
            case FirebaseVisionImageMetadata.ROTATION_180:
                rotationDegree = 180;
                break;
            case FirebaseVisionImageMetadata.ROTATION_270:
                rotationDegree = 270;
                break;
            default:
                break;
        }

        // Rotate the image back to straight.}
        matrix.postRotate(rotationDegree);
        if (facing == CameraInfo.CAMERA_FACING_BACK) {
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        } else {
            // Mirror the image along X axis for front-facing camera image.
            matrix.postScale(-1.0f, 1.0f);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        }
    }
}

