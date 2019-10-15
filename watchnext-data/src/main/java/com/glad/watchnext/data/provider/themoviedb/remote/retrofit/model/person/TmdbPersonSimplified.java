package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.person;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gautam Lad
 */
@SuppressWarnings ({"WeakerAccess", "unused"})
public final class TmdbPersonSimplified {
    @SerializedName ("popularity")
    @Expose
    public float popularity;

    @SerializedName ("id")
    @Expose
    public long id;

    @SerializedName ("profile_path")
    @Expose
    public String profile_path;

    @SerializedName ("name")
    @Expose
    public String name;

    @SerializedName ("known_for")
    @Expose
    public List<TmdbKnownFor> known_for = null;

    @SerializedName ("adult")
    @Expose
    public boolean adult;
}
