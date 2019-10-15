package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.person;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbImage;

import java.util.List;

/**
 * Created by Gautam Lad
 */
@SuppressWarnings ({"WeakerAccess", "unused"})
public final class TmdbProfileImages {
    @SerializedName ("profiles")
    @Expose
    public List<TmdbImage> profiles = null;
}
