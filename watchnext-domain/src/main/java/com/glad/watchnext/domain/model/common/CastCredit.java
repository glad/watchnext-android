package com.glad.watchnext.domain.model.common;

import com.glad.watchnext.domain.exception.InvalidCastException;
import com.glad.watchnext.domain.util.ValueHelper;

import java.io.Serializable;

/**
 * CastCredit information for a Movie or TV Show
 * <p>
 * Created by Gautam Lad
 */
public final class CastCredit implements Serializable {
    private final String id;
    private final String name;
    private final String character;
    private final Image profileImage;

    public static Builder newBuilder() {
        return new Builder();
    }

    private CastCredit(final Builder builder) throws InvalidCastException {
        try {
            id = ValueHelper.requireValue(builder.id, "Id cannot be null or empty");
            name = ValueHelper.requireValue(builder.name, "Name cannot be null or empty");
            character = ValueHelper.requireValue(builder.name, "Character cannot be null or empty");
            profileImage = builder.profileImage;
        } catch (final NullPointerException | IllegalArgumentException e) {
            throw new InvalidCastException(e);
        }
    }

    //region Getters

    /**
     * @return The id of the cast member (containing a non-null/non-empty value)
     */
    public String getId() {
        return id;
    }

    /**
     * @return The name of the cast member (containing a non-null/non-empty value)
     */
    public String getName() {
        return name;
    }

    /**
     * @return The name of the character
     */
    public String getCharacter() {
        return character;
    }

    /**
     * @return The profile image
     */
    public Image getProfileImage() {
        return profileImage;
    }
    //endregion Getters

    //region Builder
    public static final class Builder {
        private String id;
        private String name;
        private String character;
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
         * Set the name of the cast member. Cannot be null or empty.
         */
        public Builder name(final String val) {
            name = val;
            return this;
        }

        /**
         * Set the character name of the cast member. Cannot be null or empty.
         */
        public Builder character(final String val) {
            character = val;
            return this;
        }

        /**
         * Set the profile image of the cast member. Can be null.
         */
        public Builder profileImage(final Image val) {
            profileImage = val;
            return this;
        }

        /**
         * Build the model
         *
         * @throws InvalidCastException if the model cannot be built
         */
        public CastCredit build() throws InvalidCastException {
            return new CastCredit(this);
        }
    }
    //endregion Builder
}