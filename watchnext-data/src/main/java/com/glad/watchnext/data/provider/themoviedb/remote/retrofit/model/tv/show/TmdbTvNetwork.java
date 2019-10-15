package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.tv.show;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gautam Lad
 */
@SuppressWarnings ({"WeakerAccess", "unused"})
public final class TmdbTvNetwork {
    @SerializedName ("id")
    @Expose
    public String id;

    @SerializedName ("name")
    @Expose
    public String name;

    @SerializedName ("logo_path")
    @Expose
    public String logo_path;

    @SerializedName ("origin_country")
    @Expose
    public String origin_country;

    @Override
    public String toString() {
        return "TmdbTvNetwork{" +
                "\n\tid='" + id + '\'' +
                "\n\tname='" + name + '\'' +
                "\n\tlogo_path='" + logo_path + '\'' +
                "\n\torigin_country='" + origin_country + '\'' +
                '}';
    }
}
