package com.glad.watchnext.domain.model.person;

import com.glad.watchnext.domain.exception.InvalidPersonException;
import com.glad.watchnext.domain.model.common.Image;
import com.glad.watchnext.domain.util.ValueHelper;

import java.io.Serializable;
import java.util.List;

/**
 * Simplified information about a Person
 * <p>
 * Created by Gautam Lad
 */
public final class PersonSimplified implements Serializable {
    private final String id;
    private final String name;
    private final Image profileImage;
    private final List<String> knownFor;

    public static Builder newBuilder() {
        return new Builder();
    }

    private PersonSimplified(final Builder builder) throws InvalidPersonException {
        try {
            id = ValueHelper.requireValue(builder.id, "Id cannot be null or empty");
            name = ValueHelper.requireValue(builder.name, "Name cannot be null or empty");
            profileImage = builder.profileImage;
            knownFor = ValueHelper.emptyToNull(builder.knownFor);
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
     * @return The profile image of the person or null if empty or not available
     */
    public Image getProfileImage() {
        return profileImage;
    }

    /**
     * @return A list of titles the person is known for or null if empty or not available
     */
    public List<String> getKnownFor() {
        return knownFor;
    }

    //endregion Getters

    //region Builder
    public static final class Builder {
        private String id;
        private String name;
        private List<String> knownFor;
        private Image profileImage;

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
         * Set the profile image. Can be null.
         */
        public Builder profileImage(final Image val) {
            profileImage = val;
            return this;
        }

        /**
         * Set the known for list. Can be null.
         */
        public Builder knownFor(final List<String> val) {
            knownFor = val;
            return this;
        }

        /**
         * Build the model
         *
         * @throws InvalidPersonException if the model cannot be built
         */
        public PersonSimplified build() throws InvalidPersonException {
            return new PersonSimplified(this);
        }
    }
    //endregion Builder
}