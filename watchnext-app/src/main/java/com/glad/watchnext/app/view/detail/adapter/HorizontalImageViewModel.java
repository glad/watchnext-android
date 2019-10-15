package com.glad.watchnext.app.view.detail.adapter;

import android.support.annotation.NonNull;

/**
 * Created by Gautam Lad
 */
public final class HorizontalImageViewModel {
    @NonNull private final String id;
    @NonNull private final String imageUrl;

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    private HorizontalImageViewModel(final Builder builder) {
        id = builder.id;
        imageUrl = builder.imageUrl;
    }

    //region Getters
    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getImageUrl() {
        return imageUrl;
    }
    //endregion Getters

    //region Builder
    public static final class Builder {
        @NonNull private String id = "";
        @NonNull private String imageUrl = "";

        private Builder() {
        }

        @NonNull
        public Builder id(@NonNull final String val) {
            id = val;
            return this;
        }

        @NonNull
        public Builder imageUrl(@NonNull final String val) {
            imageUrl = val;
            return this;
        }

        @NonNull
        public HorizontalImageViewModel build() {
            return new HorizontalImageViewModel(this);
        }
    }
    //endregion Builder
}