package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gautam Lad
 */
public final class TmdbMovieCredit {
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

    @SerializedName ("character")
    @Expose
    public String character;
}
