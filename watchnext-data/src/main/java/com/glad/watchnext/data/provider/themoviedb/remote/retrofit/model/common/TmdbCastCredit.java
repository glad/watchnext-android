package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbEnum.GenderType;

/**
 * Created by Gautam Lad
 */
@SuppressWarnings ({"WeakerAccess", "unused"})
public final class TmdbCastCredit {
    @SerializedName ("id")
    @Expose
    public long id;

    @SerializedName ("cast_id")
    @Expose
    public long cast_id;

    @SerializedName ("credit_id")
    @Expose
    public String credit_id;

    @SerializedName ("character")
    @Expose
    public String character;

    @SerializedName ("name")
    @Expose
    public String name;

    @SerializedName ("profile_path")
    @Expose
    public String profile_path;

    @SerializedName ("gender")
    @Expose
    public GenderType gender;
}
