package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.barcodescanning;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.barcodescanning.GraphicOverlay.Graphic;


/**
 * Graphic instance for rendering Barcode position and content information in an overlay view.
 */
public class BarcodeGraphic extends Graphic {

    /**
     * The default text color
     */
    private static final int TEXT_COLOR = Color.WHITE;

    /**
     * The default text size
     */
    private static final float TEXT_SIZE = 54.0f;

    /**
     * The default stroke width
     */
    private static final float STROKE_WIDTH = 4.0f;

    /**
     * The rectangle drawn on the screen
     */
    private final Paint rectPaint;

    /**
     * The text containing the value of the barcode drawn on the screen
     */
    private final Paint barcodePaint;

    /**
     * The MLKit object holding information about the barcode
     */
    private final FirebaseVisionBarcode barcode;

    /**
     * Creates a new Barcode Graphic to be drawn in GraphicOverlay
     *
     * @param overlay The GraphicOverlay used
     * @param barcode The MLKit barcode object to extract information
     */
    BarcodeGraphic(GraphicOverlay overlay, FirebaseVisionBarcode barcode) {
        super(overlay);

        this.barcode = barcode;

        rectPaint = new Paint();
        rectPaint.setColor(TEXT_COLOR);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(STROKE_WIDTH);

        barcodePaint = new Paint();
        barcodePaint.setColor(TEXT_COLOR);
        barcodePaint.setTextSize(TEXT_SIZE);
    }


    /**
     * Draws the barcode block annotations for position, size, and raw value on the supplied canvas.
     *
     * @param canvas drawing canvas
     */
    @Override
    public void draw(Canvas canvas) {
        if (barcode == null) {
            throw new IllegalStateException("Attempting to draw a null barcode.");
        }

        // Draws the bounding box around the BarcodeBlock.
        RectF rect = new RectF(barcode.getBoundingBox());
        rect.left = translateX(rect.left);
        rect.top = translateY(rect.top);
        rect.right = translateX(rect.right);
        rect.bottom = translateY(rect.bottom);
        canvas.drawRect(rect, rectPaint);

        // Renders the barcode at the bottom of the box.
        canvas.drawText(barcode.getRawValue(), rect.left, rect.bottom, barcodePaint);
    }
}
