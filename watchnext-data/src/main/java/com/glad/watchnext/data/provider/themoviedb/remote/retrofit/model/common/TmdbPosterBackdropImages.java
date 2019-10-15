package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gautam Lad
 */
@SuppressWarnings ({"WeakerAccess", "unused"})
public final class TmdbPosterBackdropImages {
    @SerializedName ("backdrops")
    @Expose
    public List<TmdbImage> backdrops = null;

    @SerializedName ("posters")
    @Expose
    public List<TmdbImage> posters = null;
}
