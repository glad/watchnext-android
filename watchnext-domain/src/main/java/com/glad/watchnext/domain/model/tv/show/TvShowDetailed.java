package com.glad.watchnext.domain.model.tv.show;

import com.glad.watchnext.domain.constant.ModelDefaults;
import com.glad.watchnext.domain.exception.InvalidTvShowException;
import com.glad.watchnext.domain.model.common.Genre;
import com.glad.watchnext.domain.model.common.Image;
import com.glad.watchnext.domain.util.StringHelper;
import com.glad.watchnext.domain.util.ValueHelper;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Gautam Lad
 */
public final class TvShowDetailed implements Serializable {
    private final String id;
    private final String name;
    private final String overview;
    private final List<Image> posterImages;
    private final List<Image> backdropImages;
    private final String network;
    private final String rating;
    private final String countryCode;
    private final long runtimeMinutes;
    private final List<Genre> genres;

    public static Builder newBuilder() {
        return new Builder();
    }

    private TvShowDetailed(final Builder builder) throws InvalidTvShowException {
        try {
            id = ValueHelper.requireValue(builder.id, "Id cannot be null or empty");
            name = ValueHelper.requireValue(builder.name, "Name cannot be null or empty");
            overview = ValueHelper.nullToDefault(ValueHelper.emptyToNull(builder.overview), ModelDefaults.TVSHOW_MISSING_OVERVIEW);
            posterImages = ValueHelper.emptyToNull(builder.posterImages);
            backdropImages = ValueHelper.emptyToNull(builder.backdropImages);
            network = ValueHelper.emptyToNull(builder.network);
            rating = ValueHelper.nullToDefault(ValueHelper.emptyToNull(builder.rating), ModelDefaults.TVSHOW_MISSING_RATING);
            {
                final String value = ValueHelper.emptyToNull(builder.countryCode);
                if (value != null && !value.matches("^[A-Z]{2}$")) {
                    throw new IllegalArgumentException("Country code must be 2-characters and uppercase");
                }
                countryCode = value;
            }
            runtimeMinutes = builder.runtimeMinutes > 0L ? builder.runtimeMinutes : 0L;
            genres = ValueHelper.emptyToNull(builder.genres);
        } catch (final NullPointerException | IllegalArgumentException e) {
            throw new InvalidTvShowException(e);
        }
    }

    @Override
    public String toString() {
        return "TvShowDetailed{" +
                "\n\tid='" + id + '\'' +
                "\n\tname='" + name + '\'' +
                "\n\toverview='" + overview + '\'' +
                "\n\tposterImages=" + StringHelper.delimited(", ", posterImages) +
                "\n\tbackdropImages=" + StringHelper.delimited(", ", backdropImages) +
                "\n\tnetwork='" + network + '\'' +
                "\n\trating=" + rating +
                "\n\truntimeMinutes=" + runtimeMinutes +
                "\n\tgenres=" + StringHelper.delimited(", ", genres) +
                '}';
    }

    //region Getters

    /**
     * /**
     *
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
     * @return A list of poster images of the tv show or null if empty or not available
     */
    public List<Image> getPosterImages() {
        return posterImages;
    }

    /**
     * @return A list of backdrop images of the tv show or null if empty or not available
     */
    public List<Image> getBackdropImages() {
        return backdropImages;
    }

    /**
     * @return The network name of the tv show or null if empty or not available
     */
    public String getNetwork() {
        return network;
    }

    /**
     * @return The rating of the tv show or {@link ModelDefaults#TVSHOW_MISSING_RATING} if not available
     */
    public String getRating() {
        return rating;
    }

    /**
     * @return The country of origin of the tv show or null if empty or not available
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @return The runtime in minutes of the tv show or 0L if not available
     */
    public long getRuntimeMinutes() {
        return runtimeMinutes;
    }

    /**
     * @return A list of {@link Genre}s of the tv show or null if empty if not available
     */
    public List<Genre> getGenres() {
        return genres;
    }

    //endregion Getters

    //region Builder
    public static final class Builder {
        private String id;
        private String name;
        private String overview;
        private List<Image> posterImages;
        private List<Image> backdropImages;
        private String network;
        private String rating;
        private String countryCode;
        private long runtimeMinutes;
        private List<Genre> genres;

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
         * Set the list of poster images. Can be null or empty.
         */
        public Builder posterImages(final List<Image> val) {
            posterImages = val;
            return this;
        }

        /**
         * Set the list of backdrop images. Can be null or empty.
         */
        public Builder backdropImages(final List<Image> val) {
            backdropImages = val;
            return this;
        }

        /**
         * Set the rating
         */
        public Builder rating(final String val) {
            rating = val;
            return this;
        }

        /**
         * Set the TV network
         */
        public Builder network(final String val) {
            network = val;
            return this;
        }

        /**
         * Set the two-digit ISO 3166-1 country code
         */
        public Builder countryCode(final String val) {
            countryCode = val;
            return this;
        }

        /**
         * Set the runtime in minutes
         */
        public Builder runtimeMinutes(final long val) {
            runtimeMinutes = val;
            return this;
        }

        /**
         * Set the list of genres. Can be null or empty.
         */
        public Builder genres(final List<Genre> val) {
            genres = val;
            return this;
        }

        /**
         * Build the model
         *
         * @throws InvalidTvShowException if the model cannot be built
         */
        public TvShowDetailed build() throws InvalidTvShowException {
            return new TvShowDetailed(this);
        }
    }
    //endregion Builder
}