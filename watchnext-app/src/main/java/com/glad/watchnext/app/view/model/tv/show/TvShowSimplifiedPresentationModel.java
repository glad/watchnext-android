package com.glad.watchnext.app.view.model.tv.show;

import com.glad.watchnext.app.view.home.tv.shows.TvShowsView;
import com.glad.watchnext.domain.exception.InvalidTvShowException;
import com.glad.watchnext.domain.util.ValueHelper;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * A model used for presenting a tvshow with simplified information on {@link TvShowsView}
 * <p>
 * Created by Gautam Lad
 */
public final class TvShowSimplifiedPresentationModel implements Parcelable, Serializable {
    @NonNull private final String id;
    @NonNull private final String name;
    @NonNull private final String overview;
    @NonNull private final String posterImageUrl;
    @NonNull private final String backdropImageUrl;

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    private TvShowSimplifiedPresentationModel(@NonNull final Builder builder) throws InvalidTvShowException {
        try {
            id = ValueHelper.requireValue(builder.id, "Id cannot be null or empty");
            name = ValueHelper.requireValue(builder.name, "Name cannot be null or empty");
            overview = ValueHelper.requireValue(builder.overview, "Overview cannot be null or empty");
            posterImageUrl = ValueHelper.nullToEmpty(builder.posterImageUrl).trim();
            backdropImageUrl = ValueHelper.nullToEmpty(builder.backdropImageUrl).trim();
        } catch (final NullPointerException | IllegalArgumentException e) {
            throw new InvalidTvShowException(e);
        }
    }

    private TvShowSimplifiedPresentationModel(@NonNull final Parcel in) {
        id = in.readString();
        name = in.readString();
        overview = in.readString();
        posterImageUrl = in.readString();
        backdropImageUrl = in.readString();
    }

    //region Getters

    /**
     * @return The id of the tv onSubscribe
     */
    @NonNull
    public String getId() {
        return id;
    }

    /**
     * @return The name of the tv onSubscribe
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * @return The overview of the tv onSubscribe
     */
    @NonNull
    public String getOverview() {
        return overview;
    }

    /**
     * @return The poster image url of the tv onSubscribe or null if empty or not available
     */
    @NonNull
    public String getPosterImageUrl() {
        return posterImageUrl;
    }

    /**
     * @return The backdrop image url of the tv onSubscribe or null if empty or not available
     */
    @NonNull
    public String getBackdropImageUrl() {
        return backdropImageUrl;
    }
    //endregion Getters

    //region Builder
    public static final class Builder {
        @NonNull private String id = "";
        @NonNull private String name = "";
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
        public TvShowSimplifiedPresentationModel build() throws InvalidTvShowException {
            return new TvShowSimplifiedPresentationModel(this);
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
    }

    public static final Creator<TvShowSimplifiedPresentationModel> CREATOR = new Creator<TvShowSimplifiedPresentationModel>() {
        @Override
        public TvShowSimplifiedPresentationModel createFromParcel(Parcel source) {
            return new TvShowSimplifiedPresentationModel(source);
        }

        @Override
        public TvShowSimplifiedPresentationModel[] newArray(int size) {
            return new TvShowSimplifiedPresentationModel[size];
        }
    };
}