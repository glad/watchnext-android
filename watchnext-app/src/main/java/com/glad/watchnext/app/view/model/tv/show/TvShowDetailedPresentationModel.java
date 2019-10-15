package com.glad.watchnext.app.view.model.tv.show;

import com.glad.watchnext.app.view.detail.tv.show.TvShowDetailView;
import com.glad.watchnext.domain.exception.InvalidTvShowException;
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
 * A model used for presenting a tvshow with detailed information on {@link TvShowDetailView}
 * <p>
 * Created by Gautam Lad
 */
public final class TvShowDetailedPresentationModel implements Parcelable, Serializable {
    @NonNull private static final String METADATA_DELIMITER = " \u2022 ";

    @NonNull private final String id;
    @NonNull private final String name;
    @NonNull private final String overview;
    @NonNull private final String metadataLine1;
    @NonNull private final String metadataLine2;
    @NonNull private final List<String> posterImageUrls;
    @NonNull private final List<String> backdropImageUrls;

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    private TvShowDetailedPresentationModel(@NonNull final Builder builder) throws InvalidTvShowException {
        try {
            id = ValueHelper.requireValue(builder.id, "Id cannot be null or empty");
            name = ValueHelper.requireValue(builder.name, "Name cannot be null or empty");
            overview = ValueHelper.requireValue(builder.overview, "Overview cannot be null or empty");

            // Build the metadataLine1
            final StringBuilder metadata = new StringBuilder(ValueHelper.nullToEmpty(builder.network).trim());

            final String rating = ValueHelper.nullToEmpty(builder.rating).trim();
            if (!rating.isEmpty()) {
                if (metadata.length() > 0) {
                    metadata.append(METADATA_DELIMITER);
                }

                metadata.append(rating);

                final String countryCode = ValueHelper.nullToEmpty(builder.countryCode).trim();
                if (!countryCode.isEmpty()) {
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
            metadataLine1 = ValueHelper.nullToEmpty(metadata.toString());
            final List genres = ValueHelper.nullToEmpty(builder.genres);
            metadataLine2 = genres.isEmpty() ? "" : StringHelper.delimited(", ", genres);

            posterImageUrls = ValueHelper.nullToDefault(builder.posterImageUrls, new ArrayList<>());
            backdropImageUrls = ValueHelper.nullToDefault(builder.backdropImageUrls, new ArrayList<>());
        } catch (final NullPointerException | IllegalArgumentException e) {
            throw new InvalidTvShowException(e);
        }
    }

    private TvShowDetailedPresentationModel(@NonNull final Parcel in) {
        id = in.readString();
        name = in.readString();
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
    public String getName() {
        return name;
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
        @NonNull private String name = "";
        @NonNull private String overview = "";
        @NonNull private String rating = "";
        @IntRange (from = 0) private long runtimeMs = 0L;
        @NonNull private List<String> genres = new ArrayList<>();
        @NonNull private String countryCode = "";
        @NonNull private String network = "";
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
        public final Builder rating(@NonNull final String val) {
            rating = val;
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
        public final Builder countryCode(@NonNull final String val) {
            countryCode = val;
            return this;
        }

        @NonNull
        public final Builder network(@NonNull final String val) {
            network = val;
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
        public TvShowDetailedPresentationModel build() throws InvalidTvShowException {
            return new TvShowDetailedPresentationModel(this);
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
        dest.writeString(this.metadataLine1);
        dest.writeString(this.metadataLine2);
        dest.writeStringList(this.posterImageUrls);
        dest.writeStringList(this.backdropImageUrls);
    }

    public static final Creator<TvShowDetailedPresentationModel> CREATOR = new Creator<TvShowDetailedPresentationModel>() {
        @Override
        public TvShowDetailedPresentationModel createFromParcel(Parcel source) {
            return new TvShowDetailedPresentationModel(source);
        }

        @Override
        public TvShowDetailedPresentationModel[] newArray(int size) {
            return new TvShowDetailedPresentationModel[size];
        }
    };
}
