package com.glad.watchnext.app.view.model.person;

import com.glad.watchnext.domain.exception.InvalidTvShowCreditException;
import com.glad.watchnext.domain.util.ValueHelper;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * A model used for presenting a person's TV Show credit
 * <p>
 * Created by Gautam Lad
 */
public final class TvShowCreditPresentationModel implements Parcelable, Serializable {
    @NonNull private final String id;
    @NonNull private final String name;
    @NonNull private final String overview;
    @NonNull private final String posterImageUrl;
    @NonNull private final String backdropImageUrl;
    @NonNull private final String character;

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    private TvShowCreditPresentationModel(@NonNull final Builder builder) throws InvalidTvShowCreditException {
        try {
            id = ValueHelper.requireValue(builder.id, "Id cannot be null or empty");
            name = ValueHelper.requireValue(builder.name, "Name cannot be null or empty");
            overview = ValueHelper.requireValue(builder.overview, "Overview cannot be null or empty");
            posterImageUrl = ValueHelper.nullToEmpty(builder.posterImageUrl).trim();
            backdropImageUrl = ValueHelper.nullToEmpty(builder.backdropImageUrl).trim();
            character = ValueHelper.nullToEmpty(builder.character).trim();
        } catch (final NullPointerException | IllegalArgumentException e) {
            throw new InvalidTvShowCreditException(e);
        }
    }

    private TvShowCreditPresentationModel(@NonNull final Parcel in) {
        id = in.readString();
        name = in.readString();
        overview = in.readString();
        posterImageUrl = in.readString();
        backdropImageUrl = in.readString();
        character = in.readString();
    }

    //region Getters

    /**
     * @return The id of the tvshow
     */
    @NonNull
    public String getId() {
        return id;
    }

    /**
     * @return The name of the tvshow
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * @return The overview of the tvshow
     */
    @NonNull
    public String getOverview() {
        return overview;
    }

    /**
     * @return The poster image url of the tvshow
     */
    @NonNull
    public String getPosterImageUrl() {
        return posterImageUrl;
    }

    /**
     * @return The backdrop image url of the tvshow
     */
    @NonNull
    public String getBackdropImageUrl() {
        return backdropImageUrl;
    }

    /**
     * @return The character name in the tvshow
     */
    @NonNull
    public String getCharacter() {
        return character;
    }
    //endregion Getters

    //region Builder
    public static final class Builder {
        @NonNull private String id = "";
        @NonNull private String name = "";
        @NonNull private String overview = "";
        @NonNull private String posterImageUrl = "";
        @NonNull private String backdropImageUrl = "";
        @NonNull private String character = "";

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
        public final Builder overview(@NonNull final String val) {
            overview = val;
            return this;
        }

        @NonNull
        public final Builder posterImageUrl(@NonNull final String val) {
            posterImageUrl = val;
            return this;
        }

        @NonNull
        public final Builder backdropImageUrl(@NonNull final String val) {
            backdropImageUrl = val;
            return this;
        }

        @NonNull
        public final Builder character(@NonNull final String val) {
            character = val;
            return this;
        }

        @NonNull
        public TvShowCreditPresentationModel build() throws InvalidTvShowCreditException {
            return new TvShowCreditPresentationModel(this);
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
        dest.writeString(this.overview);
        dest.writeString(this.posterImageUrl);
        dest.writeString(this.backdropImageUrl);
        dest.writeString(this.character);
    }

    public static final Creator<TvShowCreditPresentationModel> CREATOR = new Creator<TvShowCreditPresentationModel>() {
        @Override
        public TvShowCreditPresentationModel createFromParcel(Parcel source) {
            return new TvShowCreditPresentationModel(source);
        }

        @Override
        public TvShowCreditPresentationModel[] newArray(int size) {
            return new TvShowCreditPresentationModel[size];
        }
    };
}