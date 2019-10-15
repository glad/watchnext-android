package com.glad.watchnext.app.view.model.common;

import com.glad.watchnext.domain.exception.InvalidCastException;
import com.glad.watchnext.domain.util.ValueHelper;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * A model used for presenting a cast credit
 * <p>
 * Created by Gautam Lad
 */
public final class CastCreditPresentationModel implements Parcelable, Serializable {
    @NonNull private final String id;
    @NonNull private final String name;
    @NonNull private final String character;
    @NonNull private final String profileImageUrl;

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    private CastCreditPresentationModel(@NonNull final Builder builder) throws InvalidCastException {
        try {
            id = ValueHelper.requireValue(builder.id, "Id cannot be null or empty");
            name = ValueHelper.requireValue(builder.name, "Name cannot be null or empty");
            character = ValueHelper.requireValue(builder.character, "Character cannot be null or empty");
            profileImageUrl = ValueHelper.nullToEmpty(builder.profileImageUrl).trim();
        } catch (final NullPointerException | IllegalArgumentException e) {
            throw new InvalidCastException(e);
        }
    }

    private CastCreditPresentationModel(@NonNull final Parcel in) {
        id = in.readString();
        name = in.readString();
        character = in.readString();
        profileImageUrl = in.readString();
    }

    //region Getters

    /**
     * @return The id of the cast
     */
    @NonNull
    public String getId() {
        return id;
    }

    /**
     * @return The name of the cast
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * @return The character of the cast
     */
    @NonNull
    public String getCharacter() {
        return character;
    }

    /**
     * @return The profile image url of the cast
     */
    @NonNull
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    //endregion Getters

    //region Builder
    public static final class Builder {
        @NonNull private String id = "";
        @NonNull private String name = "";
        @NonNull private String character = "";
        @NonNull private String profileImageUrl = "";

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
        public final Builder character(@NonNull final String val) {
            character = val;
            return this;
        }

        @NonNull
        public final Builder profileImageUrl(@NonNull final String val) {
            profileImageUrl = val;
            return this;
        }

        @NonNull
        public CastCreditPresentationModel build() throws InvalidCastException {
            return new CastCreditPresentationModel(this);
        }
    }
    //endregion Builder

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.character);
        dest.writeString(this.profileImageUrl);
    }

    public static final Creator<CastCreditPresentationModel> CREATOR = new Creator<CastCreditPresentationModel>() {
        @Override
        public CastCreditPresentationModel createFromParcel(Parcel source) {
            return new CastCreditPresentationModel(source);
        }

        @Override
        public CastCreditPresentationModel[] newArray(int size) {
            return new CastCreditPresentationModel[size];
        }
    };
}