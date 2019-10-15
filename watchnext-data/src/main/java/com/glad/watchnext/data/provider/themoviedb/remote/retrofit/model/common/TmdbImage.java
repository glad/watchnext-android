package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gautam Lad
 */
@SuppressWarnings ({"WeakerAccess", "unused"})
public final class TmdbImage {
    @SerializedName ("iso_639_1")
    @Expose
    public Object iso_639_1;

    @SerializedName ("aspect_ratio")
    @Expose
    public float aspect_ratio;

    @SerializedName ("vote_count")
    @Expose
    public long vote_count;

    @SerializedName ("height")
    @Expose
    public int height;

    @SerializedName ("vote_average")
    @Expose
    public float vote_average;

    @SerializedName ("file_path")
    @Expose
    public String file_path;

    @SerializedName ("width")
    @Expose
    public int width;
}