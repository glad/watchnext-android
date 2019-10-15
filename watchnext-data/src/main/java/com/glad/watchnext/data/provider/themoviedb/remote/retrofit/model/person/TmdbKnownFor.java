package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.person;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;

import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.movie.TmdbMovieSimplified;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.tv.show.TmdbTvShowSimplified;

import java.lang.reflect.Type;

/**
 * Created by Gautam Lad
 */
public final class TmdbKnownFor<T> {
    private TmdbKnownFor(final T value) {
        this.value = value;
    }

    @Expose
    public final T value;

    public static class Deserializer implements JsonDeserializer<TmdbKnownFor> {
        @Override
        public TmdbKnownFor deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
            switch (json.getAsJsonObject().get("media_type").getAsString().toLowerCase()) {
                case "movie":
                    return new TmdbKnownFor<>(new Gson().fromJson(json, TmdbMovieSimplified.class));
                case "tv":
                    return new TmdbKnownFor<>(new Gson().fromJson(json, TmdbTvShowSimplified.class));
                default:
                    return null;
            }
        }
    }
}