package com.glad.watchnext.data.provider.themoviedb;

import com.google.gson.GsonBuilder;

import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.RetrofitContract;
import com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.person.TmdbKnownFor;
import com.glad.watchnext.domain.provider.SettingsProvider;
import com.glad.watchnext.domain.service.LogService;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Gautam Lad
 */
class TheMovieDbDelegate {
    final RetrofitContract retrofit;

    TheMovieDbDelegate(final SettingsProvider settingsProvider, final LogService log) {
        final OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    final Request request = chain.request();
                    final HttpUrl url = request.url()
                            .newBuilder()
                            .addQueryParameter("api_key", settingsProvider.getApiKey())
                            .addQueryParameter("language",
                                    String.format("%s-%s,null", settingsProvider.getLanguageCode(), settingsProvider.getCountryCode()))
                            .build();
                    return chain.proceed(request.newBuilder().url(url).build());
                }).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(settingsProvider.getApiBaseUrl())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .registerTypeAdapter(TmdbKnownFor.class, new TmdbKnownFor.Deserializer())
                        .create()))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .create()))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create()))
                .client(httpClient)
                .build()
                .create(RetrofitContract.class);
    }
}
