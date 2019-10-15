package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gautam Lad
 */
@SuppressWarnings ({"WeakerAccess", "unused"})
public final class TmdbGenre {
    @SerializedName ("id")
    @Expose
    public long id;

    @SerializedName ("name")
    @Expose
    public String name;

    @Override
    public String toString() {
        return "TmdbGenre{" +
                "\n\tid=" + id +
                "\n\tname='" + name + '\'' +
                '}';
    }
}
