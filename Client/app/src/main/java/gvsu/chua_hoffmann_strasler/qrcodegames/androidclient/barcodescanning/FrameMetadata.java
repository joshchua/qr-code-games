package gvsu.chua_hoffmann_strasler.qrcodegames.androidclient.barcodescanning;

/** Describing a frame info. */
public final class FrameMetadata {

    /**
     * The width of the frame.
     */
    private final int width;

    /**
     * The height of the frame.
     */
    private final int height;

    /**
     * The frame's rotation.
     */
    private final int rotation;

    /**
     * The camera used (front/back).
     */
    private final int cameraFacing;

    /**
     * Gets the width of the frame.
     *
     * @return The frame's width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the frame.
     *
     * @return The frame's height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the rotation of the frame.
     *
     * @return The rotation
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * Gets the camera used.
     *
     * @return The camera facing
     */
    public int getCameraFacing() {
        return cameraFacing;
    }

    /**
     * Creates a new FrameMetadata.
     *
     * @param width The width of the frame
     * @param height The height of the frame
     * @param rotation The rotation of the frame
     * @param facing The front/back facing camera used
     */
    private FrameMetadata(final int width,
                          final int height,
                          final int rotation,
                          final int facing) {
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        cameraFacing = facing;
    }

    /** Builder of {@link FrameMetadata}. */
    public static class Builder {

        /**
         * The width of the frame.
         */
        private int width;

        /**
         * The height of the frame.
         */
        private int height;

        /**
         * The rotation of the frame.
         */
        private int rotation;

        /**
         * The front/back facing camera used.
         */
        private int cameraFacing;

        /**
         * Sets the width.
         *
         * @param width The frame's width
         * @return A builder with this new width setting.
         */
        public Builder setWidth(final int width) {
            this.width = width;
            return this;
        }

        /**
         * Sets the height.
         *
         * @param height The frame's height.
         * @return A builder with this new height setting.
         */
        public Builder setHeight(final int height) {
            this.height = height;
            return this;
        }

        /**
         * Sets the rotation.
         *
         * @param rotation The frame's rotation.
         * @return A builder with this new rotation setting.
         */
        public Builder setRotation(final int rotation) {
            this.rotation = rotation;
            return this;
        }

        /**
         * Sets the camera used.
         *
         * @param facing The front/back facing camera setting.
         * @return A builder with this new rotation setting.
         */
        public Builder setCameraFacing(final int facing) {
            cameraFacing = facing;
            return this;
        }

        /**
         * Builds a new FrameMetaData with the current settings.
         *
         * @return FrameMetadata with the builder's settings.
         */
        public FrameMetadata build() {
            return new FrameMetadata(width, height, rotation, cameraFacing);
        }
    }
}

