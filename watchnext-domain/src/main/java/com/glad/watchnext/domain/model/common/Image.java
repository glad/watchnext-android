package com.glad.watchnext.domain.model.common;

import com.glad.watchnext.domain.exception.InvalidImageException;
import com.glad.watchnext.domain.util.ValueHelper;

import java.io.Serializable;

/**
 * Created by Gautam Lad
 */
public final class Image implements Serializable {
    private final String url;
    private final int width;
    private final int height;
    private float aspectRatio;

    public static Builder newBuilder() {
        return new Builder();
    }

    private Image(final Builder builder) throws InvalidImageException {
        try {
            url = ValueHelper.requireValue(builder.url, "Url cannot be null or empty");
            width = builder.width > 0 ? builder.width : 0;
            height = builder.height > 0 ? builder.height : 0;
            aspectRatio = height > 0 ? ((float) width / (float) height) : 0.0f;
        } catch (final NullPointerException | IllegalArgumentException e) {
            throw new InvalidImageException(e);
        }
    }

    //region Getters

    /**
     * @return The url of the imagee (containing a non-null/non-empty value)
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return The width of the image or 0 if not available
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return The height of the image or 0 if not available
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return The aspect ratio (width / height) of the image or 0 if not available
     */
    public float getAspectRatio() {
        return aspectRatio;
    }

    //endregion Getters

    //region Builder
    public static final class Builder {
        private String url;
        private int width;
        private int height;

        Builder() {
        }

        /**
         * Set the url. Cannot be null or empty.
         */
        public Builder url(final String val) {
            url = val;
            return this;
        }

        /**
         * Set the width.  Must be >= 0.
         */
        public Builder width(final int val) {
            width = val;
            return this;
        }

        /**
         * Set the height.  Must be >= 0.
         */
        public Builder height(final int val) {
            height = val;
            return this;
        }

        /**
         * Build the model
         *
         * @throws InvalidImageException if the model cannot be built
         */
        public Image build() throws InvalidImageException {
            return new Image(this);
        }
    }
    //endregion Builder
}