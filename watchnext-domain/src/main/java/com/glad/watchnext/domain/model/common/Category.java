package com.glad.watchnext.domain.model.common;

import com.glad.watchnext.domain.exception.InvalidCategoryException;
import com.glad.watchnext.domain.util.ValueHelper;

import java.io.Serializable;

/**
 * Category as used by Movie and TV Show collections
 * <p>
 * Created by Gautam Lad
 */
public final class Category implements Serializable {
    private final String id;
    private final String name;

    public static Builder newBuilder() {
        return new Builder();
    }

    private Category(final Builder builder) throws InvalidCategoryException {
        try {
            id = ValueHelper.requireValue(builder.id, "Id cannot be null or empty");
            name = ValueHelper.requireValue(builder.name, "Name cannot be null or empty");
        } catch (final NullPointerException | IllegalArgumentException e) {
            throw new InvalidCategoryException(e);
        }
    }

    //region Getters

    /**
     * @return The id of the category (containing a non-null/non-empty value)
     */
    public String getId() {
        return id;
    }

    /**
     * @return The name of the category (containing a non-null/non-empty value)
     */
    public String getName() {
        return name;
    }
    //endregion Getters

    //region Builder
    public static final class Builder {
        private String id;
        private String name;

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
         * Build the model
         *
         * @throws InvalidCategoryException if the model cannot be built
         */
        public Category build() throws InvalidCategoryException {
            return new Category(this);
        }
    }
    //endregion Builder
}