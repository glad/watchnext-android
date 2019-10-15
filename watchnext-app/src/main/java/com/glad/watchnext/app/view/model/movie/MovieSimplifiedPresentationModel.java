package com.glad.watchnext.app.view.model.movie;

import com.glad.watchnext.app.view.home.movies.MoviesView;
import com.glad.watchnext.domain.exception.InvalidMovieException;
import com.glad.watchnext.domain.util.ValueHelper;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * A model used for presenting a movie with simplified information on {@link MoviesView}
 * <p>
 * Created by Gautam Lad
 */
public final class MovieSimplifiedPresentationModel implements Parcelable, Serializable {
    @NonNull private final String id;
    @NonNull private final String title;
    @NonNull private final String overview;
    @NonNull private final String posterImageUrl;
    @NonNull private final String backdropImageUrl;

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    private MovieSimplifiedPresentationModel(@NonNull final Builder builder) throws InvalidMovieException {
        try {
            id = ValueHelper.requireValue(builder.id, "Id cannot be null or empty");
            title = ValueHelper.requireValue(builder.title, "Title cannot be null or empty");
            overview = ValueHelper.requireValue(builder.overview, "Overview cannot be null or empty");
            posterImageUrl = ValueHelper.nullToEmpty(builder.posterImageUrl).trim();
            backdropImageUrl = ValueHelper.nullToEmpty(builder.backdropImageUrl).trim();
        } catch (final NullPointerException | IllegalArgumentException e) {
            throw new InvalidMovieException(e);
        }
    }

    private MovieSimplifiedPresentationModel(@NonNull final Parcel in) {
        id = in.readString();
        title = in.readString();
        overview = in.readString();
        posterImageUrl = in.readString();
        backdropImageUrl = in.readString();
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
    //endregion Getters

    //region Builder
    public static final class Builder {
        @NonNull private String id = "";
        @NonNull private String title = "";
        @NonNull private String overview = "";
        @NonNull private String posterImageUrl = "";
        @NonNull private String backdropImageUrl = "";

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
        public MovieSimplifiedPresentationModel build() throws InvalidMovieException {
            return new MovieSimplifiedPresentationModel(this);
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
    }

    public static final Creator<MovieSimplifiedPresentationModel> CREATOR = new Creator<MovieSimplifiedPresentationModel>() {
        @Override
        public MovieSimplifiedPresentationModel createFromParcel(Parcel source) {
            return new MovieSimplifiedPresentationModel(source);
        }

        @Override
        public MovieSimplifiedPresentationModel[] newArray(int size) {
            return new MovieSimplifiedPresentationModel[size];
        }
    };
}