package com.glad.watchnext.app.view.model.movie;

import com.glad.watchnext.app.view.detail.movie.MovieDetailView;
import com.glad.watchnext.domain.exception.InvalidMovieException;
import com.glad.watchnext.domain.util.StringHelper;
import com.glad.watchnext.domain.util.ValueHelper;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A model used for presenting a movie with detailed information on {@link MovieDetailView}
 * <p>
 * Created by Gautam Lad
 */
public final class MovieDetailedPresentationModel implements Parcelable, Serializable {
    @NonNull private static final String METADATA_DELIMITER = " \u2022 ";

    @NonNull private final String id;
    @NonNull private final String title;
    @NonNull private final String overview;
    @NonNull private final String metadataLine1;
    @NonNull private final String metadataLine2;
    @NonNull private final List<String> posterImageUrls;
    @NonNull private final List<String> backdropImageUrls;

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    private MovieDetailedPresentationModel(@NonNull final Builder builder) throws InvalidMovieException {
        try {
            id = ValueHelper.requireValue(builder.id, "Id cannot be null or empty");
            title = ValueHelper.requireValue(builder.title, "Title cannot be null or empty");
            overview = ValueHelper.requireValue(builder.overview, "Overview cannot be null or empty");

            // Build the metadataLine1
            final StringBuilder metadata = new StringBuilder();
            if (builder.releaseYear > 0) {
                metadata.append(String.valueOf(builder.releaseYear));
            }

            final String certification = ValueHelper.nullToEmpty(builder.certification).trim();
            if (!certification.isEmpty()) {
                if (metadata.length() > 0) {
                    metadata.append(METADATA_DELIMITER);
                }
                metadata.append(certification);

                final String countryCode = ValueHelper.emptyToNull(builder.countryCode);
                if (countryCode != null) {
                    metadata.append(" (");
                    metadata.append(countryCode);
                    metadata.append(")");
                }
            }
            if (builder.runtimeMs > 0) {
                if (metadata.length() > 0) {
                    metadata.append(METADATA_DELIMITER);
                }
                metadata.append(String.valueOf(builder.runtimeMs));
                metadata.append(" min.");
            }
            metadataLine1 = metadata.toString();

            final List genres = ValueHelper.nullToEmpty(builder.genres);
            metadataLine2 = genres.isEmpty() ? "" : StringHelper.delimited(", ", genres);

            posterImageUrls = ValueHelper.nullToDefault(builder.posterImageUrls, new ArrayList<>());
            backdropImageUrls = ValueHelper.nullToDefault(builder.backdropImageUrls, new ArrayList<>());
        } catch (final NullPointerException | IllegalArgumentException e) {
            throw new InvalidMovieException(e);
        }
    }

    private MovieDetailedPresentationModel(@NonNull final Parcel in) {
        id = in.readString();
        title = in.readString();
        overview = in.readString();
        metadataLine1 = in.readString();
        metadataLine2 = in.readString();
        in.readStringList(posterImageUrls = new ArrayList<>());
        in.readStringList(backdropImageUrls = new ArrayList<>());
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
    public String getOverview() {
        return overview;
    }

    @NonNull
    public List<String> getPosterImageUrls() {
        return posterImageUrls;
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
    //endregion Getters

    //region Builder
    public static final class Builder {
        @NonNull private String id = "";
        @NonNull private String title = "";
        @NonNull private String overview = "";
        @IntRange (from = 0) private int releaseYear = 0;
        @NonNull private String certification = "";
        @NonNull private String countryCode = "";
        @IntRange (from = 0) private long runtimeMs = 0L;
        @NonNull private List<String> genres = new ArrayList<>();
        @NonNull private List<String> posterImageUrls = new ArrayList<>();
        @NonNull private List<String> backdropImageUrls = new ArrayList<>();

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
        public final Builder releaseYear(@IntRange (from = 0) final int val) {
            releaseYear = val;
            return this;
        }

        @NonNull
        public final Builder certification(@NonNull final String val) {
            certification = val;
            return this;
        }

        @NonNull
        public final Builder countryCode(@NonNull final String val) {
            countryCode = val;
            return this;
        }

        @NonNull
        public final Builder runtimeMs(@IntRange (from = 0) final long val) {
            runtimeMs = val;
            return this;
        }

        @NonNull
        public final Builder genres(@NonNull final List<String> val) {
            genres = val;
            return this;
        }

        @NonNull
        public final Builder posterImageUrls(@NonNull final List<String> val) {
            posterImageUrls = val;
            return this;
        }

        @NonNull
        public final Builder backdropImageUrls(@NonNull final List<String> val) {
            backdropImageUrls = val;
            return this;
        }

        @NonNull
        public MovieDetailedPresentationModel build() throws InvalidMovieException {
            return new MovieDetailedPresentationModel(this);
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
        dest.writeStringList(this.posterImageUrls);
        dest.writeStringList(this.backdropImageUrls);
        dest.writeString(this.metadataLine1);
        dest.writeString(this.metadataLine2);
    }

    public static final Creator<MovieDetailedPresentationModel> CREATOR = new Creator<MovieDetailedPresentationModel>() {
        @Override
        public MovieDetailedPresentationModel createFromParcel(Parcel source) {
            return new MovieDetailedPresentationModel(source);
        }

        @Override
        public MovieDetailedPresentationModel[] newArray(int size) {
            return new MovieDetailedPresentationModel[size];
        }
    };
}