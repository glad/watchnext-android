package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.tv.show;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gautam Lad
 */
@SuppressWarnings ({"WeakerAccess", "unused"})
public final class TmdbTvRating {
    @SerializedName ("iso_3166_1")
    @Expose
    public String iso_3166_1;

    @SerializedName ("rating")
    @Expose
    public String rating;

    @Override
    public String toString() {
        return "TmdbTvRating{" +
                "\n\tiso_3166_1=" + iso_3166_1 +
                "\n\trating='" + rating + '\'' +
                '}';
    }
}
