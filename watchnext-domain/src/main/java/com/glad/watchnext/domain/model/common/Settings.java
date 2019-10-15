package com.glad.watchnext.domain.model.common;

import com.glad.watchnext.domain.exception.InvalidSettingsException;
import com.glad.watchnext.domain.util.ValueHelper;

import java.io.Serializable;
import java.util.Locale;

/**
 * Created by Gautam Lad
 */
public class Settings implements Serializable {
    private final String appVersion;
    private final String apiBaseUrl;
    private final String apiKey;
    private final String imagesBaseUrl;
    private final String languageCode;
    private final String countryCode;

    private long maxCacheSizeBytes;
    private long maxCacheAgeMs;
    private ImageQualityType imageQuality;

    public static Builder newBuilder() {
        return new Builder();
    }

    private Settings(final Builder builder) throws InvalidSettingsException {
        try {
            this.appVersion = ValueHelper.requireValue(builder.appVersion, "App version cannot be null or empty");
            this.apiBaseUrl = ValueHelper.requireValue(builder.apiBaseUrl, "Api base url cannot be null or empty");
            this.apiKey = ValueHelper.requireValue(builder.apiKey, "Api key cannot be null or empty");
            this.imagesBaseUrl = ValueHelper.requireValue(builder.imagesBaseUrl, "Image base url cannot be null or empty");
            this.languageCode = ValueHelper.nullToDefault(builder.languageCode, Locale.getDefault().getLanguage());
            this.countryCode = ValueHelper.nullToDefault(builder.countryCode, Locale.getDefault().getCountry());

            setMaxCacheSizeBytes(builder.maxCacheSizeBytes);
            setMaxCacheAgeMs(builder.maxCacheAgeMs);
            setImageQuality(builder.imageQuality);
        } catch (final NullPointerException | IllegalArgumentException | IllegalStateException e) {
            throw new InvalidSettingsException(e);
        }
    }

    public void from(final Settings src) {
        maxCacheSizeBytes = src.maxCacheSizeBytes;
        maxCacheAgeMs = src.maxCacheAgeMs;
        imageQuality = src.imageQuality;
    }

    //region Getters
    public String getAppVersion() {
        return appVersion;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getImagesBaseUrl() {
        return imagesBaseUrl;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public long getMaxCacheSizeBytes() {
        return maxCacheSizeBytes;
    }

    public void setMaxCacheSizeBytes(final long val) throws InvalidSettingsException {
        try {
            maxCacheSizeBytes = ValueHelper.verifyPositive(val, "Max cache size must be >= 0");
        } catch (final IllegalArgumentException e) {
            throw new InvalidSettingsException(e);
        }
    }

    public long getMaxCacheAgeMs() {
        return maxCacheAgeMs;
    }

    public void setMaxCacheAgeMs(final long val) throws InvalidSettingsException {
        try {
            maxCacheAgeMs = ValueHelper.verifyPositive(val, "Max cache age be >= 0");
        } catch (final IllegalArgumentException e) {
            throw new InvalidSettingsException(e);
        }
    }

    public ImageQualityType getImageQuality() {
        return imageQuality;
    }

    public void setImageQuality(final ImageQualityType val) throws InvalidSettingsException {
        try {
            imageQuality = ValueHelper.requireValue(val, "Image quality cannot be null or empty");
        } catch (final IllegalArgumentException e) {
            throw new InvalidSettingsException(e);
        }
    }

    //endregion Getters

    //region Builder
    public static final class Builder {
        private String appVersion;
        private String apiBaseUrl;
        private String apiKey;
        private String imagesBaseUrl;
        private String languageCode;
        private String countryCode;
        private long maxCacheSizeBytes;
        private long maxCacheAgeMs;
        private ImageQualityType imageQuality;

        private Builder() {
        }

        public Builder appVersion(final String val) {
            appVersion = val;
            return this;
        }

        public Builder apiBaseUrl(final String val) {
            apiBaseUrl = val;
            return this;
        }

        public Builder apiKey(final String val) {
            apiKey = val;
            return this;
        }

        public Builder imagesBaseUrl(final String val) {
            imagesBaseUrl = val;
            return this;
        }

        public Builder languageCode(final String val) {
            languageCode = val;
            return this;
        }

        public Builder countryCode(final String val) {
            countryCode = val;
            return this;
        }

        public Builder maxCacheSizeBytes(final long val) {
            maxCacheSizeBytes = val;
            return this;
        }

        public Builder maxCacheAgeMs(final long val) {
            maxCacheAgeMs = val;
            return this;
        }

        public Builder imageQuality(final String val) {
            imageQuality = ImageQualityType.valueOf(val);
            return this;
        }

        public Settings build() throws InvalidSettingsException {
            return new Settings(this);
        }
        //endregion Builder
    }

    //region Image Quality Type
    public enum ImageQualityType {
        ULTRA,
        HIGH,
        MEDIUM,
        LOW;

        public static String[] stringValues() {
            return new String[]{ULTRA.name(), HIGH.name(), MEDIUM.name(), LOW.name()};
        }
    }
    //endregion Image Quality Type
}
