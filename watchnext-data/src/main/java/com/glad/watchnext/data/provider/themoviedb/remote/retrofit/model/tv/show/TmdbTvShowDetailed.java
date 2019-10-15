package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.tv.show;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbGenre;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbPosterBackdropImages;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbResultSimplified;
import com.glad.watchnext.domain.util.StringHelper;

import java.util.List;

/**
 * Created by Gautam Lad
 */
@SuppressWarnings ({"WeakerAccess", "unused"})
public final class TmdbTvShowDetailed {
    @SerializedName ("id")
    @Expose
    public long id;

    @SerializedName ("name")
    @Expose
    public String name;

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

    @SerializedName ("runtime")
    @Expose
    public List<Long> runtime;

    @SerializedName ("origin_country")
    @Expose
    public List<String> origin_country;

    @SerializedName ("content_ratings")
    @Expose
    public TmdbResultSimplified<TmdbTvRating> content_ratings;

    @SerializedName ("networks")
    @Expose
    public List<TmdbTvNetwork> networks;

    @Override
    public String toString() {
        return "TmdbTvShowDetailed{" +
                "\n\tid=" + id +
                "\n\tname='" + name + '\'' +
                "\n\toverview='" + overview + '\'' +
                "\n\tposter_path='" + poster_path + '\'' +
                "\n\tbackdrop_path='" + backdrop_path + '\'' +
                "\n\tgenres=" + StringHelper.delimited(", ", genres) +
                "\n\truntime=" + runtime +
                "\n\torigin_country=" + StringHelper.delimited(", ", origin_country) +
                "\n\tcontent_ratings=" + content_ratings +
                '}';
    }
}