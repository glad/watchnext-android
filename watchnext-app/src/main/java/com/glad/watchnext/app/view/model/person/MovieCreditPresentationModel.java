package com.glad.watchnext.app.view.model.person;

import com.glad.watchnext.domain.exception.InvalidMovieCreditException;
import com.glad.watchnext.domain.util.ValueHelper;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * A model used for presenting a person's movie credit
 * <p>
 * Created by Gautam Lad
 */
public final class MovieCreditPresentationModel implements Parcelable, Serializable {
    @NonNull private final String id;
    @NonNull private final String title;
    @NonNull private final String overview;
    @NonNull private final String posterImageUrl;
    @NonNull private final String backdropImageUrl;
    @NonNull private final String character;

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    private MovieCreditPresentationModel(@NonNull final Builder builder) throws InvalidMovieCreditException {
        try {
            id = ValueHelper.requireValue(builder.id, "Id cannot be null or empty");
            title = ValueHelper.requireValue(builder.title, "Title cannot be null or empty");
            overview = ValueHelper.requireValue(builder.overview, "Overview cannot be null or empty");
            posterImageUrl = ValueHelper.nullToEmpty(builder.posterImageUrl).trim();
            backdropImageUrl = ValueHelper.nullToEmpty(builder.backdropImageUrl).trim();
            character = ValueHelper.nullToEmpty(builder.character).trim();
        } catch (final NullPointerException | IllegalArgumentException e) {
            throw new InvalidMovieCreditException(e);
        }
    }

    private MovieCreditPresentationModel(@NonNull final Parcel in) {
        id = in.readString();
        title = in.readString();
        overview = in.readString();
        posterImageUrl = in.readString();
        backdropImageUrl = in.readString();
        character = in.readString();
    }

    //region Getters

    /**
     * @return The id of the movie
     */
    @NonNull
    public String getId() {
        return id;
    }

    /**
     * @return The name of the movie
     */
    @NonNull
    public String getTitle() {
        return title;
    }

    /**
     * @return The overview of the movie
     */
    @NonNull
    public String getOverview() {
        return overview;
    }

    /**
     * @return The poster image url of the movie
     */
    @NonNull
    public String getPosterImageUrl() {
        return posterImageUrl;
    }

    /**
     * @return The backdrop image url of the movie
     */
    @NonNull
    public String getBackdropImageUrl() {
        return backdropImageUrl;
    }

    /**
     * @return The character name in the movie
     */
    @NonNull
    public String getCharacter() {
        return character;
    }
    //endregion Getters

    //region Builder
    public static final class Builder {
        @NonNull private String id = "";
        @NonNull private String title = "";
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
        public final Builder title(@NonNull final String val) {
            title = val;
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
        public MovieCreditPresentationModel build() throws InvalidMovieCreditException {
            return new MovieCreditPresentationModel(this);
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
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.posterImageUrl);
        dest.writeString(this.backdropImageUrl);
        dest.writeString(this.character);
    }

    public static final Creator<MovieCreditPresentationModel> CREATOR = new Creator<MovieCreditPresentationModel>() {
        @Override
        public MovieCreditPresentationModel createFromParcel(Parcel source) {
            return new MovieCreditPresentationModel(source);
        }

        @Override
        public MovieCreditPresentationModel[] newArray(int size) {
            return new MovieCreditPresentationModel[size];
        }
    };
}