package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gautam Lad
 */
@SuppressWarnings ({"WeakerAccess", "unused"})
public final class TmdbResultSimplified<T> {
    @SerializedName ("results")
    @Expose
    public List<T> results;

    @Override
    public String toString() {
        return "Result{" +
                "\n\tresults=" + results +
                '}';
    }
}
