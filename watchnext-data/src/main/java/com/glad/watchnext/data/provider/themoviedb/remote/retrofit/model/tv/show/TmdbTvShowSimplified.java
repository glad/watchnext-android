package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.tv.show;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gautam Lad
 */
@SuppressWarnings ({"WeakerAccess", "unused"})
public final class TmdbTvShowSimplified {
    @SerializedName ("id")
    @Expose
    public long id;

    @SerializedName ("name")
    @Expose
    public String name;

    @SerializedName ("overview")
    @Expose
    public String overview;

    @SerializedName ("poster_path")
    @Expose
    public String poster_path;

    @SerializedName ("backdrop_path")
    @Expose
    public String backdrop_path;

    @Override
    public String toString() {
        return "TmdbTvShowSimplified{" +
                "\n\tid=" + id +
                "\n\tname='" + name + '\'' +
                "\n\toverview='" + overview + '\'' +
                "\n\tposter_path='" + poster_path + '\'' +
                "\n\tbackdrop_path='" + backdrop_path + '\'' +
                "\n}";
    }
}