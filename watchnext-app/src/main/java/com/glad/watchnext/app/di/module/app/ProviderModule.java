package com.glad.watchnext.app.di.module.app;

import com.glad.watchnext.BuildConfig;
import com.glad.watchnext.app.provider.AppSettingsProvider;
import com.glad.watchnext.app.provider.image.glide.GlideImageProvider;
import com.glad.watchnext.data.provider.themoviedb.TheMovieDbDataProvider;
import com.glad.watchnext.data.provider.themoviedb.TheMovieDbSearchDataProvider;
import com.glad.watchnext.domain.exception.InvalidSettingsException;
import com.glad.watchnext.domain.model.common.Settings;
import com.glad.watchnext.domain.provider.DataProvider;
import com.glad.watchnext.domain.provider.SchedulerProvider;
import com.glad.watchnext.domain.provider.SearchDataProvider;
import com.glad.watchnext.domain.provider.SettingsProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.StorageService;
import com.glad.watchnext.platform.provider.AndroidSchedulerProvider;
import com.glad.watchnext.platform.provider.ImageProvider;

import android.app.Application;
import android.support.annotation.NonNull;

import java.util.Locale;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Gautam Lad
 */
@Module
public final class ProviderModule {
    @Singleton
    @Provides
    DataProvider providesDataProvider(
            @NonNull final SettingsProvider settingsProvider,
            @NonNull final LogService log) {
        return new TheMovieDbDataProvider(settingsProvider, log);
    }

    @Singleton
    @Provides
    SearchDataProvider providesSearchDataProvider(
            @NonNull final SettingsProvider settingsProvider,
            @NonNull final LogService log) {
        return new TheMovieDbSearchDataProvider(settingsProvider, log);
    }
    @Singleton
    @Provides
    SchedulerProvider providesSchedulerProvider() {
        return new AndroidSchedulerProvider();
    }

    @Provides
    ImageProvider providesImageProvider(
            @NonNull final Application application,
            @NonNull final SchedulerProvider schedulerProvider) {
        return new GlideImageProvider(application, schedulerProvider);
    }

    @Singleton
    @Provides
    SettingsProvider providesSettingsProvider(@NonNull @Named ("settings") final StorageService storage) {
        try {
            return new AppSettingsProvider(storage,
                    Settings.newBuilder()
                            .appVersion(BuildConfig.VERSION_NAME)
                            .apiBaseUrl(BuildConfig.THEMOVIEDB_API_BASE_URL)
                            .imagesBaseUrl(BuildConfig.THEMOVIEDB_IMAGES_BASE_URL)
                            .apiKey(BuildConfig.THEMOVIEDB_API_KEY)
                            .languageCode(Locale.getDefault().getLanguage())
                            .countryCode(Locale.getDefault().getCountry())
                            .maxCacheSizeBytes(BuildConfig.DEFAULT_MAX_CACHE_SIZE_BYTES)
                            .maxCacheAgeMs(BuildConfig.DEFAULT_MAX_CACHE_AGE_MS)
                            .imageQuality(BuildConfig.DEFAULT_IMAGE_QUALITY)
                            .build());
        } catch (final InvalidSettingsException e) {
            throw new RuntimeException(e);
        }
    }
}