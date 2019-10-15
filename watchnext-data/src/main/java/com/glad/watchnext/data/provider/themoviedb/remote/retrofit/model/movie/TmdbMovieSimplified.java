package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.movie;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gautam Lad
 */
@SuppressWarnings ({"WeakerAccess", "unused"})
public final class TmdbMovieSimplified {
    @SerializedName ("id")
    @Expose
    public long id;

    @SerializedName ("title")
    @Expose
    public String title;

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
        return "TmdbMovieSimplified{" +
                "\n\tid=" + id +
                "\n\ttitle='" + title + '\'' +
                "\n\toverview='" + overview + '\'' +
                "\n\tposter_path='" + poster_path + '\'' +
                "\n\tbackdrop_path='" + backdrop_path + '\'' +
                "\n}";
    }
}
