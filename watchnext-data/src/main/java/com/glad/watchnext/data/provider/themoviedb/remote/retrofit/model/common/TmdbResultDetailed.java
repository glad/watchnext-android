package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gautam Lad
 */
@SuppressWarnings ({"WeakerAccess", "unused"})
public final class TmdbResultDetailed<T> {
    @SerializedName ("page")
    @Expose
    public long page;

    @SerializedName ("total_results")
    @Expose
    public long total_results;

    @SerializedName ("total_pages")
    @Expose
    public long total_pages;

    @SerializedName ("results")
    @Expose
    public List<T> results;

    @Override
    public String toString() {
        return "Result{" +
                "\n\tpage=" + page +
                "\n\ttotal_results=" + total_results +
                "\n\ttotal_pages=" + total_pages +
                "\n\tresults=" + results +
                '}';
    }
}
