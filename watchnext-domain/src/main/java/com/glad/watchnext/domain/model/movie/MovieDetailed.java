package com.glad.watchnext.domain.model.movie;

import com.glad.watchnext.domain.constant.ModelDefaults;
import com.glad.watchnext.domain.exception.InvalidMovieException;
import com.glad.watchnext.domain.model.common.Genre;
import com.glad.watchnext.domain.model.common.Image;
import com.glad.watchnext.domain.util.StringHelper;
import com.glad.watchnext.domain.util.ValueHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Detailed information about a Movie
 * <p>
 * Created by Gautam Lad
 */
public final class MovieDetailed implements Serializable {
    private final String id;
    private final String title;
    private final String overview;
    private final List<Image> posterImages;
    private final List<Image> backdropImages;
    private final Date releaseDate;
    private final String certification;
    private final String countryCode;
    private final long runtimeMinutes;
    private final List<Genre> genres;

    public static Builder newBuilder() {
        return new Builder();
    }

    private MovieDetailed(final Builder builder) throws InvalidMovieException {
        try {
            id = ValueHelper.requireValue(builder.id, "Id cannot be null or empty");
            title = ValueHelper.requireValue(builder.title, "Title cannot be null or empty");
            overview = ValueHelper.nullToDefault(ValueHelper.emptyToNull(builder.overview), ModelDefaults.MOVIE_MISSING_OVERVIEW);
            posterImages = ValueHelper.emptyToNull(builder.posterImages);
            backdropImages = ValueHelper.emptyToNull(builder.backdropImages);
            releaseDate = builder.releaseDate;
            certification = ValueHelper.nullToDefault(ValueHelper.emptyToNull(builder.certification), ModelDefaults.MOVIE_MISSING_CERTIFICATION);
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
            throw new InvalidMovieException(e);
        }
    }

    @Override
    public String toString() {
        return "MovieDetailed{" +
                "\n\tid='" + id + '\'' +
                "\n\ttitle='" + title + '\'' +
                "\n\toverview='" + overview + '\'' +
                "\n\tposterImages=" + StringHelper.delimited(", ", posterImages) +
                "\n\tbackdropImages=" + StringHelper.delimited(", ", backdropImages) +
                "\n\treleaseDate='" + releaseDate + '\'' +
                "\n\tcertification='" + certification + '\'' +
                "\n\tcountryCode='" + countryCode + '\'' +
                "\n\truntimeMinutes=" + runtimeMinutes +
                "\n\tgenres=" + StringHelper.delimited(", ", genres) +
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
     * @return A list of poster images of the movie or null if empty or not available
     */
    public List<Image> getPosterImages() {
        return posterImages;
    }

    /**
     * @return A list of backdrop images of the movie or null if empty or not available
     */
    public List<Image> getBackdropImages() {
        return backdropImages;
    }

    /**
     * @return The release date of the movie or null if empty or not available
     */
    public Date getReleaseDate() {
        return releaseDate;
    }

    /**
     * @return The certification of the movie or {@link ModelDefaults#MOVIE_MISSING_CERTIFICATION} if not available
     */
    public String getCertification() {
        return certification;
    }

    /**
     * @return The country of origin of the movie or null if empty or not available
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @return The runtime in minutes of the movie or 0L if not available
     */
    public long getRuntimeMinutes() {
        return runtimeMinutes;
    }

    /**
     * @return A list of {@link Genre}s of the movie or null if empty if not available
     */
    public List<Genre> getGenres() {
        return genres;
    }
    //endregion Getters

    //region Builder
    public static final class Builder {
        private String id;
        private String title;
        private String overview;
        private List<Image> posterImages;
        private List<Image> backdropImages;
        private Date releaseDate;
        private String certification;
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
         * Set the release date. Can be null.
         */
        public Builder releaseDate(final Date val) {
            releaseDate = val;
            return this;
        }

        /**
         * Set the certification
         */
        public Builder certification(final String val) {
            certification = val;
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
         * @throws InvalidMovieException if the model cannot be built
         */
        public MovieDetailed build() throws InvalidMovieException {
            return new MovieDetailed(this);
        }
    }
    //endregion Builder
}