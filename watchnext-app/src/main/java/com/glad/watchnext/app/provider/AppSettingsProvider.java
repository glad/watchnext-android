package com.glad.watchnext.app.provider;

import com.glad.watchnext.domain.exception.InvalidSettingsException;
import com.glad.watchnext.domain.model.common.Settings;
import com.glad.watchnext.domain.model.common.Settings.ImageQualityType;
import com.glad.watchnext.domain.provider.SettingsProvider;
import com.glad.watchnext.domain.service.StorageService;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import io.reactivex.Completable;

/**
 * Created by Gautam Lad
 */
public final class AppSettingsProvider implements SettingsProvider {
    @NonNull private static final String STORAGE_NAME = "settings";

    @NonNull private final StorageService.Editor storageEditor;
    @NonNull private final Settings defaultSettings;
    @NonNull private final Settings settings;

    public AppSettingsProvider(
            @NonNull final StorageService storage,
            @NonNull final Settings settings) {
        this.storageEditor = storage.edit(STORAGE_NAME);
        this.settings = settings;
        this.defaultSettings = settings;
    }

    @Override
    public Completable init() {
        return storageEditor.readLong(SettingsProvider.MAX_CACHE_SIZE_BYTES_KEY)
                .onErrorReturnItem(settings.getMaxCacheSizeBytes())
                .doOnSuccess(val -> {
                    try {
                        settings.setMaxCacheSizeBytes(val);
                    } catch (@NonNull final InvalidSettingsException e) {
                        // No-op
                    }
                })
                .toCompletable()
                .mergeWith(storageEditor.readLong(SettingsProvider.MAX_CACHE_AGE_MS_KEY)
                        .onErrorReturnItem(settings.getMaxCacheAgeMs())
                        .doOnSuccess(val -> {
                            try {
                                settings.setMaxCacheAgeMs(val);
                            } catch (@NonNull final InvalidSettingsException e) {
                                // No-op
                            }
                        })
                        .toCompletable())
                .mergeWith(storageEditor.readString(SettingsProvider.IMAGE_QUALITY_KEY)
                        .map(ImageQualityType::valueOf)
                        .onErrorReturnItem(settings.getImageQuality())
                        .doOnSuccess(val -> {
                            try {
                                settings.setImageQuality(val);
                            } catch (@NonNull final InvalidSettingsException e) {
                                // No-op
                            }
                        })
                        .toCompletable())
                .onErrorComplete();
    }

    @Override
    public Completable reset() {
        settings.from(defaultSettings);
        return Completable.complete();
    }

    @Override
    @NonNull
    public String getAppVersion() {
        return settings.getAppVersion();
    }

    @Override
    @NonNull
    public String getApiBaseUrl() {
        return settings.getApiBaseUrl();
    }

    @Override
    @NonNull
    public String getApiKey() {
        return settings.getApiKey();
    }

    @Override
    @NonNull
    public String getImagesBaseUrl() {
        return settings.getImagesBaseUrl();
    }

    @Override
    @NonNull
    public String getPosterImageWidth() {
        switch (settings.getImageQuality()) {
            case ULTRA:
                return "original";
            case HIGH:
                return "w780";
            case MEDIUM:
                return "w342";
            case LOW:
            default:
                return "w92";
        }
    }

    @Override
    @NonNull
    public String getBackdropImageWidth() {
        switch (settings.getImageQuality()) {
            case ULTRA:
                return "original";
            case HIGH:
                return "w1280";
            case MEDIUM:
                return "w780";
            case LOW:
            default:
                return "w300";
        }
    }

    @Override
    @NonNull
    public String getProfileImageWidth() {
        switch (settings.getImageQuality()) {
            case ULTRA:
                return "original";
            case HIGH:
                return "w632";
            case MEDIUM:
                return "w185";
            case LOW:
            default:
                return "w45";
        }
    }

    @Override
    @NonNull
    public String getLanguageCode() {
        return settings.getLanguageCode();
    }

    @Override
    @NonNull
    public String getCountryCode() {
        return settings.getCountryCode();
    }

    @Override
    @IntRange (from = 0)
    public long getMaxCacheSizeBytes() {
        return settings.getMaxCacheSizeBytes();
    }

    @Override
    @NonNull
    public Completable setMaxCacheSizeBytes(@IntRange (from = 0) final long val) {
        try {
            settings.setMaxCacheSizeBytes(val);
            return storageEditor.writeLong(SettingsProvider.MAX_CACHE_SIZE_BYTES_KEY, val);
        } catch (final InvalidSettingsException e) {
            return Completable.error(e);
        }
    }

    @Override
    @IntRange (from = 0)
    public long getMaxCacheAgeMs() {
        return settings.getMaxCacheAgeMs();
    }

    @Override
    @NonNull
    public Completable setMaxCacheAgeMs(@IntRange (from = 0) final long val) {
        try {
            settings.setMaxCacheAgeMs(val);
            return storageEditor.writeLong(SettingsProvider.MAX_CACHE_AGE_MS_KEY, val);
        } catch (final InvalidSettingsException e) {
            return Completable.error(e);
        }
    }

    @Override
    @NonNull
    public ImageQualityType getImageQuality() {
        return settings.getImageQuality();
    }

    @Override
    @NonNull
    public Completable setImageQuality(@NonNull final ImageQualityType val) {
        try {
            settings.setImageQuality(val);
            return storageEditor.writeString(SettingsProvider.IMAGE_QUALITY_KEY, val.toString());
        } catch (final InvalidSettingsException e) {
            return Completable.error(e);
        }
    }
}