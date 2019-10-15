package com.glad.watchnext.domain.model.tv.show;

import com.glad.watchnext.domain.constant.ModelDefaults;
import com.glad.watchnext.domain.exception.InvalidTvShowException;
import com.glad.watchnext.domain.model.common.Image;
import com.glad.watchnext.domain.util.ValueHelper;

import java.io.Serializable;

/**
 * Simplified information about a TV Show
 * <p>
 * Created by Gautam Lad
 */
public final class TvShowSimplified implements Serializable {
    private final String id;
    private final String name;
    private final String overview;
    private final Image posterImage;
    private final Image backdropImage;

    public static Builder newBuilder() {
        return new Builder();
    }

    private TvShowSimplified(final Builder builder) throws InvalidTvShowException {
        try {
            id = ValueHelper.requireValue(builder.id, "Id cannot be null or empty");
            name = ValueHelper.requireValue(builder.name, "Name cannot be null or empty");
            overview = ValueHelper.nullToDefault(ValueHelper.emptyToNull(builder.overview), ModelDefaults.TVSHOW_MISSING_OVERVIEW);
            posterImage = builder.posterImage;
            backdropImage = builder.backdropImage;
        } catch (final NullPointerException | IllegalArgumentException e) {
            throw new InvalidTvShowException(e);
        }
    }

    @Override
    public String toString() {
        return "TvShowSimplified{" +
                "\n\tid='" + id + '\'' +
                "\n\tname='" + name + '\'' +
                "\n\toverview='" + overview + '\'' +
                "\n\tposterImage='" + posterImage + '\'' +
                "\n\tbackdropImage='" + backdropImage + '\'' +
                '}';
    }

    //region Getters

    /**
     * @return The id of the tv show (containing a non-null/non-empty value)
     */
    public String getId() {
        return id;
    }

    /**
     * @return The name of the tv show (containing a non-null/non-empty value)
     */
    public String getName() {
        return name;
    }

    /**
     * @return The overview of the tv show or {@link ModelDefaults#TVSHOW_MISSING_OVERVIEW} if not available
     */
    public String getOverview() {
        return overview;
    }

    /**
     * @return The poster image of the tv show or null if empty or not available
     */
    public Image getPosterImage() {
        return posterImage;
    }

    /**
     * @return The backdrop image of the tv show or null if empty or not available
     */
    public Image getBackdropImage() {
        return backdropImage;
    }
    //endregion Getters

    //region Builder
    public static final class Builder {
        private String id;
        private String name;
        private String overview;
        private Image posterImage;
        private Image backdropImage;

        Builder() {
        }

        /**
         * Set the id. Cannot be null or empty.
         */
        public Builder id(final String val) {
            id = val;
            return this;
        }

        /**
         * Set the name. Cannot be null or empty.
         */
        public Builder name(final String val) {
            name = val;
            return this;
        }

        /**
         * Set the overview. Can be null or empty.
         */
        public Builder overview(final String val) {
            overview = val;
            return this;
        }

        /**
         * Set the poster image . Can be null.
         */
        public Builder posterImage(final Image val) {
            posterImage = val;
            return this;
        }

        /**
         * Set the backdrop image . Can be null.
         */
        public Builder backdropImage(final Image val) {
            backdropImage = val;
            return this;
        }

        /**
         * Build the model
         *
         * @throws InvalidTvShowException if the model cannot be built
         */
        public TvShowSimplified build() throws InvalidTvShowException {
            return new TvShowSimplified(this);
        }
    }
    //endregion Builder
}