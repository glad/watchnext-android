package com.glad.watchnext.domain.model.common;

import com.glad.watchnext.domain.constant.ModelDefaults;
import com.glad.watchnext.domain.exception.InvalidMovieCreditException;
import com.glad.watchnext.domain.util.ValueHelper;

import java.io.Serializable;

/**
 * Movie credit information for a person
 * <p>
 * Created by Gautam Lad
 */
public final class MovieCredit implements Serializable {
    private final String id;
    private final String title;
    private final String overview;
    private final Image posterImage;
    private final Image backdropImage;
    private final String character;

    public static Builder newBuilder() {
        return new Builder();
    }

    private MovieCredit(final Builder builder) throws InvalidMovieCreditException {
        try {
            id = ValueHelper.requireValue(builder.id, "Id cannot be null or empty");
            title = ValueHelper.requireValue(builder.title, "Title cannot be null or empty");
            overview = ValueHelper.nullToDefault(ValueHelper.emptyToNull(builder.overview), ModelDefaults.MOVIE_MISSING_OVERVIEW);
            posterImage = builder.posterImage;
            backdropImage = builder.backdropImage;
            character = ValueHelper.emptyToNull(builder.character);
        } catch (final NullPointerException | IllegalArgumentException e) {
            throw new InvalidMovieCreditException(e);
        }
    }

    @Override
    public String toString() {
        return "MovieCredit{" +
                "\n\tid='" + id + '\'' +
                "\n\ttitle='" + title + '\'' +
                "\n\toverview='" + overview + '\'' +
                "\n\tposterImage='" + posterImage + '\'' +
                "\n\tbackdropImage='" + backdropImage + '\'' +
                "\n\tcharacter='" + character + '\'' +
                '}';
    }
    //region Getters

    /**
     * @return The id of the credit (containing a non-null/non-empty value)
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

    /**
     * @return The character name of the cast or null if empty or not available
     */
    public String getCharacter() {
        return character;
    }
    //endregion Getters

    //region Builder
    public static final class Builder {
        private String id;
        private String title;
        private String overview;
        private Image posterImage;
        private Image backdropImage;
        private String character;

        Builder() {
        }

        public Builder id(final String val) {
            id = val;
            return this;
        }

        public Builder title(final String val) {
            title = val;
            return this;
        }

        public Builder overview(final String val) {
            overview = val;
            return this;
        }

        public Builder posterImage(final Image val) {
            posterImage = val;
            return this;
        }

        public Builder backdropImage(final Image val) {
            backdropImage = val;
            return this;
        }

        public Builder character(final String val) {
            character = val;
            return this;
        }

        public MovieCredit build() throws InvalidMovieCreditException {
            return new MovieCredit(this);
        }
    }
    //endregion Builder
}