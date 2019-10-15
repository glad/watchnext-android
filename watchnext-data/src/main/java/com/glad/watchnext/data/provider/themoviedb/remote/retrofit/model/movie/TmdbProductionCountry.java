package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.movie;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gautam Lad
 */
@SuppressWarnings ({"WeakerAccess", "unused"})
public final class TmdbProductionCountry {
    @SerializedName ("iso_3166_1")
    @Expose
    public String iso_3166_1;

    @SerializedName ("name")
    @Expose
    public String name;

    @Override
    public String toString() {
        return "TmdbProductionCountry{" +
                "\n\tiso_3166_1='" + iso_3166_1 + '\'' +
                "\n\tname='" + name + '\'' +
                '}';
    }
}
