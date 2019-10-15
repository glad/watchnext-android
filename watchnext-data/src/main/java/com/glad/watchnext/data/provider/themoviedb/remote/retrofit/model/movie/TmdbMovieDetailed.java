package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.movie;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbGenre;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbPosterBackdropImages;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbResultSimplified;

import java.util.Date;
import java.util.List;

/**
 * Created by Gautam Lad
 */
@SuppressWarnings ({"WeakerAccess", "unused"})
public final class TmdbMovieDetailed {
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

    @SerializedName ("images")
    @Expose
    public TmdbPosterBackdropImages images = null;

    @SerializedName ("genres")
    @Expose
    public List<TmdbGenre> genres = null;

    @SerializedName ("original_language")
    @Expose
    public String original_language;

    @SerializedName ("production_countries")
    @Expose
    public List<TmdbProductionCountry> production_countries = null;

    @SerializedName ("release_date")
    @Expose
    public Date release_date;

    @SerializedName ("release_dates")
    @Expose
    public TmdbResultSimplified<TmdbReleaseDateResult> release_dates;

    @SerializedName ("runtime")
    @Expose
    public long runtime;

    @Override
    public String toString() {
        return "TmdbMovieDetailed{" +
                "\n\tid=" + id +
                "\n\ttitle='" + title + '\'' +
                "\n\toverview='" + overview + '\'' +
                "\n\tposter_path='" + poster_path + '\'' +
                "\n\tbackdrop_path='" + backdrop_path + '\'' +
                "\n\timages=" + images +
                "\n\tgenres=" + genres +
                "\n\toriginal_language='" + original_language + '\'' +
                "\n\tproduction_countries=" + production_countries +
                "\n\trelease_date=" + release_date +
                "\n\trelease_dates=" + release_dates +
                "\n\truntime=" + runtime +
                '}';
    }
}