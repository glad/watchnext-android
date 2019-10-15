package com.glad.watchnext.app.view.model.person;

import com.glad.watchnext.app.view.home.people.PeopleView;
import com.glad.watchnext.domain.exception.InvalidPersonException;
import com.glad.watchnext.domain.util.ValueHelper;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * A model used for presenting a person with simplified information on {@link PeopleView}
 * <p>
 * Created by Gautam Lad
 */
public final class PersonDetailedPresentationModel implements Parcelable, Serializable {
    @NonNull private final String id;
    @NonNull private final String name;
    @NonNull private final String metadataLine1;
    @NonNull private final String metadataLine2;
    @NonNull private final String metadataLine3;
    @NonNull private final String biography;
    @NonNull private final List<String> profileImageUrls;
    @NonNull private final List<String> backdropImageUrls;

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    private PersonDetailedPresentationModel(@NonNull final Builder builder) throws InvalidPersonException {
        try {
            id = ValueHelper.requireValue(builder.id, "Id cannot be null or empty");
            name = ValueHelper.requireValue(builder.name, "Name cannot be null or empty");
            biography = ValueHelper.nullToEmpty(builder.biography).trim();

            final String birthPlace = ValueHelper.nullToEmpty(builder.birthPlace).trim();
            if (builder.birthDate != null) {
                final Calendar birthDate = GregorianCalendar.getInstance();
                birthDate.setTime(builder.birthDate);

                final String birthLine = "Born " + new SimpleDateFormat("LLLL d, yyyy", Locale.getDefault()).format(builder.birthDate);
                final String deathLine = (builder.deathDate != null) ?
                        "Died " + new SimpleDateFormat("LLLL d, yyyy", Locale.getDefault()).format(builder.deathDate) : null;

                final long nowMs;
                if (builder.deathDate != null) {
                    final Calendar deathDate = new GregorianCalendar();
                    deathDate.setTime(builder.deathDate);
                    nowMs = deathDate.getTimeInMillis();
                } else {
                    nowMs = new GregorianCalendar().getTimeInMillis();
                }
                final String ageLine = " (Age " + (Math.round((nowMs - birthDate.getTimeInMillis()) / 31556952000L)) + ")";

                if (deathLine != null) {
                    metadataLine1 = birthLine;
                    metadataLine2 = deathLine + ageLine;
                    metadataLine3 = birthPlace;
                } else {
                    metadataLine1 = birthLine + ageLine;
                    metadataLine2 = birthPlace;
                    metadataLine3 = "";
                }
            } else {
                metadataLine1 = birthPlace;
                metadataLine2 = "";
                metadataLine3 = "";
            }

            profileImageUrls = ValueHelper.nullToDefault(builder.profileImageUrls, new ArrayList<>());
            backdropImageUrls = ValueHelper.nullToDefault(builder.backdropImageUrls, new ArrayList<>());
        } catch (final NullPointerException | IllegalArgumentException e) {
            throw new InvalidPersonException(e);
        }
    }

    private PersonDetailedPresentationModel(@NonNull final Parcel in) {
        id = in.readString();
        name = in.readString();
        biography = in.readString();
        metadataLine1 = in.readString();
        metadataLine2 = in.readString();
        metadataLine3 = in.readString();
        in.readStringList(profileImageUrls = new ArrayList<>());
        in.readStringList(backdropImageUrls = new ArrayList<>());
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
     * @return The biography of the person
     */
    @NonNull
    public String getBiography() {
        return biography;
    }

    @NonNull
    public List<String> getProfileImageUrls() {
        return profileImageUrls;
    }

    @NonNull
    public List<String> getBackdropImageUrls() {
        return backdropImageUrls;
    }

    @NonNull
    public String getMetadataLine1() {
        return metadataLine1;
    }

    @NonNull
    public String getMetadataLine2() {
        return metadataLine2;
    }

    @NonNull
    public String getMetadataLine3() {
        return metadataLine3;
    }
    //endregion Getters

    //region Builder
    public static final class Builder {
        @NonNull private String id = "";
        @NonNull private String name = "";
        @Nullable private Date birthDate = null;
        @Nullable private Date deathDate = null;
        @NonNull private String birthPlace = "";
        @NonNull private String biography = "";
        @NonNull private List<String> profileImageUrls = new ArrayList<>();
        @NonNull private List<String> backdropImageUrls = new ArrayList<>();

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
        public final Builder birthDate(@Nullable final Date val) {
            birthDate = val;
            return this;
        }

        @NonNull
        public final Builder deathDate(@Nullable final Date val) {
            deathDate = val;
            return this;
        }

        @NonNull
        public final Builder birthPlace(@NonNull final String val) {
            birthPlace = val;
            return this;
        }

        @NonNull
        public final Builder biography(@NonNull final String val) {
            biography = val;
            return this;
        }

        @NonNull
        public final Builder profileImageUrls(@NonNull final List<String> val) {
            profileImageUrls = val;
            return this;
        }

        @NonNull
        public final Builder backdropImageUrls(@NonNull final List<String> val) {
            backdropImageUrls = val;
            return this;
        }

        @NonNull
        public PersonDetailedPresentationModel build() throws InvalidPersonException {
            return new PersonDetailedPresentationModel(this);
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
        dest.writeString(this.biography);
        dest.writeString(this.metadataLine1);
        dest.writeString(this.metadataLine2);
        dest.writeString(this.metadataLine3);
        dest.readStringList(this.profileImageUrls);
        dest.readStringList(this.backdropImageUrls);
    }

    public static final Creator<PersonDetailedPresentationModel> CREATOR = new Creator<PersonDetailedPresentationModel>() {
        @Override
        public PersonDetailedPresentationModel createFromParcel(Parcel source) {
            return new PersonDetailedPresentationModel(source);
        }

        @Override
        public PersonDetailedPresentationModel[] newArray(int size) {
            return new PersonDetailedPresentationModel[size];
        }
    };
}