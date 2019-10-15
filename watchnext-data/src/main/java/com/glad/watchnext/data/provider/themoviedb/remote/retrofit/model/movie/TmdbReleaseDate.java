package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.movie;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common.TmdbEnum.ReleaseType;

import java.util.Date;

/**
 * Created by Gautam Lad
 */
@SuppressWarnings ({"WeakerAccess", "unused"})
public final class TmdbReleaseDate {
    @SerializedName ("certification")
    @Expose
    public String certification;

    /**
     * Two letter language code - {@see "https://en.wikipedia.org/wiki/ISO_639-1"}
     */
    @SerializedName ("iso_639_1")
    @Expose
    public String iso_639_1;

    @SerializedName ("note")
    @Expose
    public String note;

    @SerializedName ("release_date")
    @Expose
    public Date release_date;

    @SerializedName ("type")
    @Expose
    public ReleaseType type;

    @Override
    public String toString() {
        return "TmdbReleaseDate{" +
                "\n\tcertification='" + certification + '\'' +
                "\n\tiso_639_1='" + iso_639_1 + '\'' +
                "\n\tnote='" + note + '\'' +
                "\n\trelease_date='" + release_date + '\'' +
                "\n\ttype=" + type +
                '}';
    }
}