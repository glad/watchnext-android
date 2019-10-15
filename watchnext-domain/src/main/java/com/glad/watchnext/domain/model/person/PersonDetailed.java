package com.glad.watchnext.domain.model.person;

import com.glad.watchnext.domain.exception.InvalidPersonException;
import com.glad.watchnext.domain.model.common.Image;
import com.glad.watchnext.domain.model.common.MovieCredit;
import com.glad.watchnext.domain.model.common.TvShowCredit;
import com.glad.watchnext.domain.util.ValueHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Detailed information about a Person
 * <p>
 * Created by Gautam Lad
 */
public final class PersonDetailed implements Serializable {
    private final String id;
    private final String name;
    private final String biography;
    private final Date birthDate;
    private final Date deathDate;
    private final String birthPlace;
    private final List<Image> profileImage;
    private final List<MovieCredit> movieCredits;
    private final List<TvShowCredit> tvShowCredits;

    public static Builder newBuilder() {
        return new Builder();
    }

    private PersonDetailed(final Builder builder) throws InvalidPersonException {
        try {
            id = ValueHelper.requireValue(builder.id, "Id cannot be null or empty");
            name = ValueHelper.requireValue(builder.name, "Name cannot be null or empty");
            biography = ValueHelper.emptyToNull(builder.biography);
            birthDate = builder.birthDate;
            deathDate = builder.deathDate;
            birthPlace = ValueHelper.emptyToNull(builder.birthPlace);
            profileImage = ValueHelper.emptyToNull(builder.profileImages);
            movieCredits = ValueHelper.emptyToNull(builder.movieCredits);
            tvShowCredits = ValueHelper.emptyToNull(builder.tvShowCredits);
        } catch (final NullPointerException | IllegalArgumentException e) {
            throw new InvalidPersonException(e);
        }
    }

    //region Getters

    /**
     * @return The id of the person (containing a non-null/non-empty value)
     */
    public String getId() {
        return id;
    }

    /**
     * @return The name of the person (containing a non-null/non-empty value)
     */
    public String getName() {
        return name;
    }

    /**
     * @return The profile images of the person or null if empty or not available
     */
    public List<Image> getProfileImages() {
        return profileImage;
    }

    /**
     * @return The biography of the person or null if empty or not available
     */
    public String getBiography() {
        return biography;
    }

    /**
     * @return The date of birth of the person or null if empty or not available
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * @return The date of death of the person or null if empty or not available
     */
    public Date getDeathDate() {
        return deathDate;
    }

    /**
     * @return The place of birth of the person or null if empty or not available
     */
    public String getBirthPlace() {
        return birthPlace;
    }

    /**
     * @return The person's cast info from movie credits or null if empty or not available
     */
    public List<MovieCredit> getMovieCredits() {
        return movieCredits;
    }

    /**
     * @return The person's cast info from tv show credits
     */
    public List<TvShowCredit> getTvShowCredits() {
        return tvShowCredits;
    }
    //endregion Getters

    //region Builder
    public static final class Builder {
        private String id;
        private String name;
        private String biography;
        private Date birthDate;
        private Date deathDate;
        private String birthPlace;
        private List<Image> profileImages;
        private List<MovieCredit> movieCredits;
        private List<TvShowCredit> tvShowCredits;

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
         * Set the profile biography. Can be null or empty.
         */
        public Builder biography(final String val) {
            biography = val;
            return this;
        }

        /**
         * Set the person's birth date. Can be null.
         */
        public Builder birthDate(final Date val) {
            birthDate = val;
            return this;
        }

        /**
         * Set the person's death date. Can be null.
         */
        public Builder deathDate(final Date val) {
            deathDate = val;
            return this;
        }

        /**
         * Set the person's birth place. Can be null.
         */
        public Builder birthPlace(final String val) {
            birthPlace = val;
            return this;
        }

        /**
         * Set the profile image urls. Can be null or empty.
         */
        public Builder profileImages(final List<Image> val) {
            profileImages = val;
            return this;
        }

        public Builder movieCasts(final List<MovieCredit> val) {
            movieCredits = val;
            return this;
        }

        public Builder tvShowCasts(final List<TvShowCredit> val) {
            tvShowCredits = val;
            return this;
        }

        /**
         * Build the model
         *
         * @throws InvalidPersonException if the model cannot be built
         */
        public PersonDetailed build() throws InvalidPersonException {
            return new PersonDetailed(this);
        }
    }
    //endregion Builder
}