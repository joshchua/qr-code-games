package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.barcodescanning;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.gms.vision.CameraSource;

import java.util.ArrayList;
import java.util.List;

/**
 * A view which renders a series of custom graphics to be overlayed on top of
 * an associated preview (i.e., the camera preview). The creator can add
 * graphics objects, update the objects, and remove them, triggering the
 * appropriate drawing and invalidation within the view.
 *
 * <p>Supports scaling and mirroring of the graphics relative the camera's
 * preview properties. The idea is that detection items are expressed in
 * terms of a preview size, but need to be scaled up to the full view size,
 * and also mirrored in the case of the front-facing camera.
 *
 * <p>Associated {@link Graphic} items should use the following methods to
 * convert to view coordinates for the graphics that are drawn:
 *
 * <ol>
 * <li>{@link Graphic#scaleX(float)} and {@link Graphic#scaleY(float)} adjust
 * the size of the supplied value from the preview scale to the view scale.
 * <li>{@link Graphic#translateX(float)} and {@link Graphic#translateY(float)}
 * adjust the coordinate from the preview's coordinate system to the view
 * coordinate system.
 * </ol>
 */
public class GraphicOverlay extends View {

    /**
     * Lock that synchronizes the camera preview.
     */
    private final Object lock = new Object();

    /**
     * List of graphics.
     */
    private final List<Graphic> graphics = new ArrayList<>();

    /**
     * Size of the preview.
     */
    private int previewWidth;

    /**
     * Scale factor for width coordinates.
     */
    private float widthScaleFactor = 1.0f;

    /**
     * Size of the preview.
     */
    private int previewHeight;

    /**
     * Scale factor for height coordinates.
     */
    private float heightScaleFactor = 1.0f;

    /**
     * Front facing camera.
     */
    private int facing = CameraSource.CAMERA_FACING_BACK;

    /**
     *  Graphic overlay for the camera preview.
     *
     * @param context Context of the preview
     * @param attrs Attributes for the overlay
     */
    public GraphicOverlay(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Removes all graphics from the overlay.
     */
    public void clear() {
        synchronized (lock) {
            graphics.clear();
        }
        postInvalidate();
    }

    /**
     * Adds a graphic to the overlay.
     *
     * @param graphic graphic
     */
    public void add(final Graphic graphic) {
        synchronized (lock) {
            graphics.add(graphic);
        }
    }

    /**
     * Removes a graphic from the overlay.
     *
     * @param graphic graphic
     */
    public void remove(final Graphic graphic) {
        synchronized (lock) {
            graphics.remove(graphic);
        }
        postInvalidate();
    }

    /**
     * Sets the camera attributes for size and facing direction, which
     * informs  how to transform image coordinates later.
     *
     * @param previewWidth width of the camera preview
     * @param previewHeight height of the camera preview
     * @param facing front/back facing camera
     */
    public void setCameraInfo(final int previewWidth, final int previewHeight,
                             final int facing) {
        synchronized (lock) {
            this.previewWidth = previewWidth;
            this.previewHeight = previewHeight;
            this.facing = facing;
        }
        postInvalidate();
    }

    /**
     * Draws the overlay with its associated graphic objects.
     *
     * @param canvas canvas to be drawn on
     */
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        synchronized (lock) {
            if ((previewWidth != 0) && (previewHeight != 0)) {
                widthScaleFactor = (float) canvas.getWidth()
                        / (float) previewWidth;
                heightScaleFactor = (float) canvas.getHeight()
                        / (float) previewHeight;
            }

            for (Graphic graphic : graphics) {
                graphic.draw(canvas);
            }
        }
    }

    /**
     * Base class for a custom graphics object to be rendered within the
     * graphic overlay. Subclass this and implement
     * the{@link Graphic#draw(Canvas)} method to define the graphics element.
     * Add instances to the overlay using {@link GraphicOverlay#add(Graphic)}.
     */
    public abstract static class Graphic {
        /**
         * Graphic overlay for the camera.
         */
        private GraphicOverlay overlay;

        /**
         * Graphic overlay constructor for the camera.
         *
         * @param overlay overlay to be constructed
         */
        public Graphic(final GraphicOverlay overlay) {
            this.overlay = overlay;
        }

        /**
         * Draw the graphic on the supplied canvas. Drawing should use the
         * following methods to convert to view coordinates for the graphics
         * that are drawn:
         *
         * <ol>
         * <li>{@link Graphic#scaleX(float)} and
         * {@link Graphic#scaleY(float)}  adjust the size of the supplied
         * value from the preview scale to the view scale.
         * <li>{@link Graphic#translateX(float)} and
         * {@link Graphic#translateY(float)} adjust the coordinate from the
         * preview's coordinate system to the view coordinate system.
         * </ol>
         *
         * @param canvas drawing canvas
         */
        public abstract void draw(Canvas canvas);

        /**
         * Adjusts a horizontal value of the supplied value from the preview
         * scale to the view scale.
         *
         * @param horizontal horizontal coordinate to be scaled
         *
         * @return scaled horizontal value
         */
        public float scaleX(final float horizontal) {
            return horizontal * overlay.widthScaleFactor;
        }

        /**
         * Adjusts a vertical value of the supplied value from the preview
         * scale to the view scale.
         *
         * @param vertical vertical coordinate to be scaled
         *
         * @return scaled vertical value
         */
        public float scaleY(final float vertical) {
            return vertical * overlay.heightScaleFactor;
        }

        /**
         * Returns the application context of the app.
         *
         * @return application context
         */
        public Context getApplicationContext() {
            return overlay.getContext().getApplicationContext();
        }

        /**
         * Adjusts the x coordinate from the preview's coordinate system to
         * the view coordinate system.
         *
         * @param x x coordinate
         *
         * @return scaled x value
         */
        public float translateX(final float x) {
            if (overlay.facing == CameraSource.CAMERA_FACING_FRONT) {
                return overlay.getWidth() - scaleX(x);
            } else {
                return scaleX(x);
            }
        }

        /**
         * Adjusts the y coordinate from the preview's coordinate system to
         * the view coordinate system.
         *
         * @param y y coordinate
         *
         * @return scaled y value
         */
        public float translateY(final float y) {
            return scaleY(y);
        }

        /**
         * Tells the system that the view has changed and needs to be redrawn.
         */
        public void postInvalidate() {
            overlay.postInvalidate();
        }
    }
}
