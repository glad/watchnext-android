package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.movie;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.glad.watchnext.domain.util.StringHelper;

import java.util.List;

/**
 * Created by Gautam Lad
 */
@SuppressWarnings ({"WeakerAccess", "unused"})
public final class TmdbReleaseDateResult {
    /**
     * Two letter country code - {@see "https://en.wikipedia.org/wiki/ISO_3166-1"}
     */
    @SerializedName ("iso_3166_1")
    @Expose
    public String iso_3166_1;

    @SerializedName ("release_dates")
    @Expose
    public List<TmdbReleaseDate> release_dates;

    @Override
    public String toString() {
        return "TmdbReleaseDateResult{" +
                "\n\tiso_3166_1=" + iso_3166_1 +
                "\n\trelease_dates=[" + StringHelper.delimited(", ", release_dates) + "]" +
                '}';
    }
}
