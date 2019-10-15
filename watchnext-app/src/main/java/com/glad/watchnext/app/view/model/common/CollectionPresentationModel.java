package com.glad.watchnext.app.view.model.common;

import com.glad.watchnext.domain.exception.InvalidCategoryException;
import com.glad.watchnext.domain.util.ValueHelper;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * A model used for presenting a collection (of Movies, TV Shows, et.c) on {@link com.glad.watchnext.app.view.home.HomeView}
 * <p>
 * Created by Gautam Lad
 */
public final class CollectionPresentationModel implements Parcelable, Serializable {
    @NonNull private final String id;
    @NonNull private final String name;

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    private CollectionPresentationModel(final Builder builder) throws InvalidCategoryException {
        try {
            id = ValueHelper.requireValue(builder.id, "Id cannot be null or empty");
            name = ValueHelper.requireValue(builder.name, "Name cannot be null or empty");
        } catch (final NullPointerException | IllegalArgumentException e) {
            throw new InvalidCategoryException(e);
        }
    }

    private CollectionPresentationModel(@NonNull final Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    /**
     * @return The id of the collection
     */
    @NonNull
    public String getId() {
        return id;
    }

    /**
     * @return The name of the collection
     */
    @NonNull
    public String getName() {
        return name;
    }

    //region Builder
    public static final class Builder {
        @NonNull private String id = "";
        @NonNull private String name = "";

        private Builder() {
        }

        @NonNull
        public final Builder id(@NonNull final String val) {
            id = val;
            return this;
        }

        @NonNull
        public final Builder name(@NonNull final String val) {
            name = val;
            return this;
        }

        @NonNull
        public CollectionPresentationModel build() throws InvalidCategoryException {
            return new CollectionPresentationModel(this);
        }
    }
    //endregion Builder

    //region Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

    public static final Creator<CollectionPresentationModel> CREATOR = new Creator<CollectionPresentationModel>() {
        @Override
        public CollectionPresentationModel createFromParcel(Parcel source) {
            return new CollectionPresentationModel(source);
        }

        @Override
        public CollectionPresentationModel[] newArray(int size) {
            return new CollectionPresentationModel[size];
        }
    };
    //endregion Parcelable
}