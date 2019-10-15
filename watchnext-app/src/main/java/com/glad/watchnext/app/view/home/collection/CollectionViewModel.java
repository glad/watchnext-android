package com.glad.watchnext.app.view.home.collection;

import android.support.annotation.NonNull;

/**
 * Created by Gautam Lad
 */
public final class CollectionViewModel {
    @NonNull private final String id;
    @NonNull private final String title;
    @NonNull private final String body;
    @NonNull private final String imageUrl;

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    private CollectionViewModel(final CollectionViewModel.Builder builder) {
        id = builder.id;
        title = builder.title;
        body = builder.body;
        imageUrl = builder.imageUrl;
    }

    //region Getters
    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getBody() {
        return body;
    }

    @NonNull
    public String getImageUrl() {
        return imageUrl;
    }
    //endregion Getters

    //region Builder
    public static final class Builder {
        @NonNull private String id = "";
        @NonNull private String title = "";
        @NonNull private String body = "";
        @NonNull private String imageUrl = "";

        private Builder() {
        }

        @NonNull
        public Builder id(@NonNull final String val) {
            id = val;
            return this;
        }

        @NonNull
        public Builder title(@NonNull final String val) {
            title = val;
            return this;
        }

        @NonNull
        public Builder body(@NonNull final String val) {
            body = val;
            return this;
        }

        @NonNull
        public Builder imageUrl(@NonNull final String val) {
            imageUrl = val;
            return this;
        }

        @NonNull
        public CollectionViewModel build() {
            return new CollectionViewModel(this);
        }
    }
    //endregion Builder
}