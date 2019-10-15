package com.glad.watchnext.domain.model.movie;

import com.glad.watchnext.domain.constant.ModelDefaults;
import com.glad.watchnext.domain.exception.InvalidMovieException;
import com.glad.watchnext.domain.model.common.Image;
import com.glad.watchnext.domain.util.ValueHelper;

import java.io.Serializable;

/**
 * Simplified information about a Movie
 * <p>
 * Created by Gautam Lad
 */
public final class MovieSimplified implements Serializable {
    private final String id;
    private final String title;
    private final String overview;
    private final Image posterImage;
    private final Image backdropImage;

    public static Builder newBuilder() {
        return new Builder();
    }

    private MovieSimplified(final Builder builder) throws InvalidMovieException {
        try {
            id = ValueHelper.requireValue(builder.id, "Id cannot be null or empty");
            title = ValueHelper.requireValue(builder.title, "Title cannot be null or empty");
            overview = ValueHelper.nullToDefault(ValueHelper.emptyToNull(builder.overview), ModelDefaults.MOVIE_MISSING_OVERVIEW);
            posterImage = builder.posterImage;
            backdropImage = builder.backdropImage;
        } catch (final NullPointerException | IllegalArgumentException e) {
            throw new InvalidMovieException(e);
        }
    }

    @Override
    public String toString() {
        return "MovieSimplified{" +
                "\n\tid='" + id + '\'' +
                "\n\ttitle='" + title + '\'' +
                "\n\toverview='" + overview + '\'' +
                "\n\tposterImage='" + posterImage + '\'' +
                "\n\tbackdropImage='" + backdropImage + '\'' +
                '}';
    }

    //region Getters

    /**
     * @return The id of the movie (containing a non-null/non-empty value)
     */
    public String getId() {
        return id;
    }

    /**
     * @return The title of the movie (containing a non-null/non-empty value)
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The overview of the movie or {@link ModelDefaults#MOVIE_MISSING_OVERVIEW} if not available
     */
    public String getOverview() {
        return overview;
    }

    /**
     * @return The poster image of the movie or null if empty or not available
     */
    public Image getPosterImage() {
        return posterImage;
    }

    /**
     * @return The backdrop image of the movie or null if empty or not available
     */
    public Image getBackdropImage() {
        return backdropImage;
    }
    //endregion Getters

    //region Builder
    public static final class Builder {
        private String id;
        private String title;
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
         * Set the title. Cannot be null or empty.
         */
        public Builder title(final String val) {
            title = val;
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
         * @throws InvalidMovieException if the model cannot be built
         */
        public MovieSimplified build() throws InvalidMovieException {
            return new MovieSimplified(this);
        }
    }
    //endregion Builder
}