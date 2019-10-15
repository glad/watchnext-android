package com.glad.watchnext.domain.provider;

import com.glad.watchnext.domain.model.common.Settings.ImageQualityType;

import io.reactivex.Completable;

/**
 * Created by Gautam Lad
 */
public interface SettingsProvider {
    Completable init();

    Completable reset();

    String getAppVersion();

    String getApiBaseUrl();

    String getApiKey();

    String getImagesBaseUrl();

    String getPosterImageWidth();

    String getBackdropImageWidth();

    String getProfileImageWidth();

    String getLanguageCode();

    String getCountryCode();

    //region Max Cache Size Bytes
    String IMAGE_QUALITY_KEY = "imageQuality";

    ImageQualityType getImageQuality();

    Completable setImageQuality(final ImageQualityType quality);
    //endregion Max Cache Size Bytes

    //region Max Cache Size Bytes
    String MAX_CACHE_SIZE_BYTES_KEY = "maxCacheSizeBytes";

    long getMaxCacheSizeBytes();

    Completable setMaxCacheSizeBytes(final long val);
    //endregion Max Cache Size Bytes

    //region Max Cache Age Ms
    String MAX_CACHE_AGE_MS_KEY = "maxCacheAgeMs";

    long getMaxCacheAgeMs();

    Completable setMaxCacheAgeMs(final long val);
    //endregion Max Cache Age Ms
}
