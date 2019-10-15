package com.glad.watchnext.app.view.model.person;

import com.glad.watchnext.app.view.home.people.PeopleView;
import com.glad.watchnext.domain.exception.InvalidPersonException;
import com.glad.watchnext.domain.util.StringHelper;
import com.glad.watchnext.domain.util.ValueHelper;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A model used for presenting a person with simplified information on {@link PeopleView}
 * <p>
 * Created by Gautam Lad
 */
public final class PersonSimplifiedPresentationModel implements Parcelable, Serializable {
    @NonNull private final String id;
    @NonNull private final String name;
    @NonNull private final String profileImageUrl;
    @NonNull private final String knownFor;

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    private PersonSimplifiedPresentationModel(@NonNull final Builder builder) throws InvalidPersonException {
        try {
            id = ValueHelper.requireValue(builder.id, "Id cannot be null or empty");
            name = ValueHelper.requireValue(builder.name, "Name cannot be null or empty");
            profileImageUrl = ValueHelper.nullToEmpty(builder.profileImageUrl).trim();
            knownFor = StringHelper.delimited(", ", builder.knownFor);
        } catch (final NullPointerException | IllegalArgumentException e) {
            throw new InvalidPersonException(e);
        }
    }

    private PersonSimplifiedPresentationModel(@NonNull final Parcel in) {
        id = in.readString();
        name = in.readString();
        profileImageUrl = in.readString();
        knownFor = in.readString();
    }

    //region Getters

    /**
     * @return The id of the person
     */
    @NonNull
    public String getId() {
        return id;
    }

    /**
     * @return The name of the person
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * @return The profile image url of the person
     */
    @NonNull
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    /**
     * @return A list of comma delimited titles known for
     */
    @NonNull
    public String getKnownFor() {
        return knownFor;
    }
    //endregion Getters

    //region Builder
    public static final class Builder {
        @NonNull private String id = "";
        @NonNull private String name = "";
        @NonNull private String profileImageUrl = "";
        @NonNull private List<String> knownFor = new ArrayList<>();

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
        public final Builder profileImageUrl(@NonNull final String val) {
            profileImageUrl = val;
            return this;
        }

        @NonNull
        public final Builder knownFor(@NonNull final List<String> val) {
            knownFor = val;
            return this;
        }

        @NonNull
        public PersonSimplifiedPresentationModel build() throws InvalidPersonException {
            return new PersonSimplifiedPresentationModel(this);
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
        dest.writeString(this.profileImageUrl);
        dest.writeString(this.knownFor);
    }

    public static final Creator<PersonSimplifiedPresentationModel> CREATOR = new Creator<PersonSimplifiedPresentationModel>() {
        @Override
        public PersonSimplifiedPresentationModel createFromParcel(Parcel source) {
            return new PersonSimplifiedPresentationModel(source);
        }

        @Override
        public PersonSimplifiedPresentationModel[] newArray(int size) {
            return new PersonSimplifiedPresentationModel[size];
        }
    };
}