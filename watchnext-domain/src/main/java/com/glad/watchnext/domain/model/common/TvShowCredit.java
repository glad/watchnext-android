package com.glad.watchnext.domain.model.common;

import com.glad.watchnext.domain.constant.ModelDefaults;
import com.glad.watchnext.domain.exception.InvalidTvShowCreditException;
import com.glad.watchnext.domain.util.ValueHelper;

import java.io.Serializable;

/**
 * TV Show credit information for a person
 * <p>
 * Created by Gautam Lad
 */
public final class TvShowCredit implements Serializable {
    private final String id;
    private final String name;
    private final String overview;
    private final Image posterImage;
    private final Image backdropImage;
    private final String character;

    public static Builder newBuilder() {
        return new Builder();
    }

    private TvShowCredit(final Builder builder) throws InvalidTvShowCreditException {
        try {
            id = ValueHelper.requireValue(builder.id, "Id cannot be null or empty");
            name = ValueHelper.requireValue(builder.name, "Name cannot be null or empty");
            overview = ValueHelper.nullToDefault(ValueHelper.emptyToNull(builder.overview), ModelDefaults.TVSHOW_MISSING_OVERVIEW);
            posterImage = builder.posterImage;
            backdropImage = builder.backdropImage;
            character = ValueHelper.emptyToNull(builder.character);
        } catch (final NullPointerException | IllegalArgumentException e) {
            throw new InvalidTvShowCreditException(e);
        }
    }

    @Override
    public String toString() {
        return "TvShowCredit{" +
                "\n\tid='" + id + '\'' +
                "\n\tname='" + name + '\'' +
                "\n\toverview='" + overview + '\'' +
                "\n\tposterImage='" + posterImage + '\'' +
                "\n\tbackdropImage='" + backdropImage + '\'' +
                "\n\tcharacter='" + character + '\'' +
                '}';
    }

    //region Getters

    /**
     * @return The id of the cast (containing a non-null/non-empty value)
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
        private String name;
        private String overview;
        private Image posterImage;
        private Image backdropImage;
        private String character;

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
         * Set the overview
         */
        public Builder overview(final String val) {
            overview = val;
            return this;
        }

        /**
         * Set the poster image or null if not available
         */
        public Builder posterImage(final Image val) {
            posterImage = val;
            return this;
        }

        /**
         * Set the backdrop image or null if not available
         */
        public Builder backdropImage(final Image val) {
            backdropImage = val;
            return this;
        }

        /**
         * Set the name of the character
         */
        public Builder character(final String val) {
            character = val;
            return this;
        }

        /**
         * Build the model
         *
         * @throws InvalidTvShowCreditException if the model cannot be built
         */
        public TvShowCredit build() throws InvalidTvShowCreditException {
            return new TvShowCredit(this);
        }
    }
    //endregion Builder
}