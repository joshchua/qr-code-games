package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.barcodescanning;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.barcodescanning.GraphicOverlay.Graphic;


/**
 * Draw camera image to background.
 */
public class CameraImageGraphic extends Graphic {

    /**
     * The bitmap to be drawn on the canvas
     */
    private final Bitmap bitmap;

    /**
     * Creates a CameraImageGraphic
     *
     * @param overlay The GraphicOverlay view
     * @param bitmap The camera image
     */
    public CameraImageGraphic(GraphicOverlay overlay, Bitmap bitmap) {
        super(overlay);
        this.bitmap = bitmap;
    }

    /**
     * @param canvas drawing canvas
     */
    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, null, new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), null);
    }
}

