package com.glad.watchnext.app.di.module.app;

import com.glad.watchnext.domain.implementation.service.DefaultSearchHistoryService;
import com.glad.watchnext.domain.provider.SettingsProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.SearchHistoryService;
import com.glad.watchnext.domain.service.StorageService;
import com.glad.watchnext.platform.service.AndroidLogService;
import com.glad.watchnext.platform.service.SharedPreferenceStorageService;
import com.glad.watchnext.platform.service.cache.CacheStorageService;

import android.app.Application;
import android.support.annotation.NonNull;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Gautam Lad
 */
@Module
public final class AppServiceModule {
    @NonNull private final Application application;

    public AppServiceModule(@NonNull final Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    LogService providesLogService() {
        return new AndroidLogService();
    }

    @Singleton
    @Provides
    @Named ("settings")
    StorageService providesSharedPreferenceStorageService(
            @NonNull final LogService log) {
        return new SharedPreferenceStorageService(application, log);
    }

    @Singleton
    @Provides
    @Named ("cache")
    StorageService providesCacheStorageService(
            @NonNull final SettingsProvider settingsProvider,
            @NonNull final LogService log) {
        return new CacheStorageService(application, settingsProvider, log);
    }

    @Singleton
    @Provides
    @Named ("search-history")
    StorageService providesSearchHistoryStorageService(
            @NonNull final LogService log) {
        return new SharedPreferenceStorageService(application, log);
    }

    @Singleton
    @Provides
    SearchHistoryService providesSearchHistoryService(
            @NonNull @Named ("search-history") final StorageService storageService,
            @NonNull final LogService log) {
        return new DefaultSearchHistoryService(storageService, log);
    }
}