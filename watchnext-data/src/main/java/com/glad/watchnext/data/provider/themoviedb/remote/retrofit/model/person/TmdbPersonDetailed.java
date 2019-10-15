package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.person;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbEnum.GenderType;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbMovieCredits;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbTvCredits;

import java.util.Date;
import java.util.List;

/**
 * Created by Gautam Lad
 */
@SuppressWarnings ({"WeakerAccess", "unused"})
public final class TmdbPersonDetailed {
    @SerializedName ("birthday")
    @Expose
    public Date birthday;

    @SerializedName ("deathday")
    @Expose
    public Date deathday;

    @SerializedName ("id")
    @Expose
    public long id;

    @SerializedName ("name")
    @Expose
    public String name;

    @SerializedName ("also_known_as")
    @Expose
    public List<String> also_known_as = null;

    @SerializedName ("gender")
    @Expose
    public GenderType gender;

    @SerializedName ("biography")
    @Expose
    public String biography;

    @SerializedName ("popularity")
    @Expose
    public float popularity;

    @SerializedName ("place_of_birth")
    @Expose
    public String place_of_birth;

    @SerializedName ("profile_path")
    @Expose
    public String profile_path;

    @SerializedName ("adult")
    @Expose
    public boolean adult;

    @SerializedName ("imdb_id")
    @Expose
    public String imdb_id;

    @SerializedName ("images")
    @Expose
    public TmdbProfileImages images = null;

    @SerializedName ("movie_credits")
    @Expose
    public TmdbMovieCredits movie_credits = null;

    @SerializedName ("tv_credits")
    @Expose
    public TmdbTvCredits tv_credits = null;
}
