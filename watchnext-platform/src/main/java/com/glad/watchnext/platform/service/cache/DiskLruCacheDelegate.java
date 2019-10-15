package com.glad.watchnext.platform.service.cache;

import com.glad.watchnext.domain.provider.SettingsProvider;
import com.glad.watchnext.domain.service.LogService;
import com.jakewharton.disklrucache.DiskLruCache;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;

/**
 * Created by Gautam Lad
 */
class DiskLruCacheDelegate {
    @NonNull private static final String TAG = DiskLruCacheDelegate.class.getSimpleName();
    @NonNull private static final String ROOT_PATH = "cache";
    @IntRange (from = 0) private static final int VALUE_INDEX = 0;
    @IntRange (from = 0) private static final int EXPIRY_INDEX = 1;
    @IntRange (from = 0) private static final int INDEX_SIZE = 2;

    @NonNull private final LogService log;
    @NonNull private final DiskLruCache cache;
    @IntRange (from = 0) private final long maxCacheAgeMs;

    /**
     * NOTE: Pattern taken from {@link DiskLruCache}
     */
    @NonNull private static final String VALID_KEY_PATTERN = "[^a-z0-9_-]";
    @IntRange (from = 1, to = 64) private static final int VALID_KEY_LENGTH = 64;

    DiskLruCacheDelegate(
            @NonNull final String name,
            @NonNull final Context context,
            @NonNull final SettingsProvider settingsProvider,
            @NonNull final LogService log) {

        final int version = Integer.parseInt(settingsProvider.getAppVersion().replaceAll("[^\\d]", ""));
        try {
            final String path = context.getFilesDir() + File.separator + ROOT_PATH + File.separator + name;
            final File file = new File(path);
            if (!file.exists() && !file.mkdirs()) {
                throw new IllegalStateException("Failed to create path: " + path);
            }

            maxCacheAgeMs = settingsProvider.getMaxCacheAgeMs();
            cache = DiskLruCache.open(file, version, INDEX_SIZE, settingsProvider.getMaxCacheSizeBytes());
            this.log = log;
        } catch (final NullPointerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    void clear() throws IOException {
        cache.delete();
    }

    boolean delete(@NonNull final String key) throws IOException {
        final String safeKey = safeKey(key);
        log.d(TAG, "delete() called with: key = [" + safeKey + "]");

        return cache.remove(safeKey);
    }

    boolean exists(@NonNull final String key) throws IOException {
        return cache.get(safeKey(key)) != null;
    }

    @Nullable
    String read(@NonNull final String key) throws IOException {
        final String safeKey = safeKey(key);
        log.d(TAG, "read() called with: key = [" + safeKey + "]");

        final DiskLruCache.Snapshot snapshot = cache.get(safeKey);
        if (snapshot == null) {
            log.w(TAG, "read: Snapshot is null for key = [" + safeKey + "]");
            return null;
        }

        try {
            final String value = snapshot.getString(VALUE_INDEX);
            final long expiryMs = Long.parseLong(snapshot.getString(EXPIRY_INDEX));
            snapshot.close();

            final long nowMs = new GregorianCalendar().getTimeInMillis();
            if (expiryMs - nowMs > 0) {
                return value;
            }

            log.w(TAG, "read: Value expired for key = [" + safeKey + "]");
            delete(safeKey);

            return null;
        } catch (final NumberFormatException e) {
            log.e(TAG, "read(" + safeKey + "): " + e.getMessage(), e);
            return null;
        }
    }

    void write(@NonNull final String key, @NonNull final String value) throws IOException {
        final String safeKey = safeKey(key);
        log.d(TAG, "write() called with: key = [" + safeKey + "], value = [" + value + "]");

        final DiskLruCache.Editor creator = cache.edit(safeKey);
        final long expiryMs = new GregorianCalendar().getTimeInMillis() + maxCacheAgeMs;
        creator.set(VALUE_INDEX, value);
        creator.set(EXPIRY_INDEX, String.valueOf(expiryMs));
        creator.commit();
    }

    /**
     * @param key The key to sanitize
     *
     * @return A sanitized version of the provided key value
     */
    private String safeKey(@NonNull final String key) {
        final String newKey = key.toLowerCase().replaceAll(VALID_KEY_PATTERN, "-");
        return newKey.substring(0, Math.min(newKey.length(), VALID_KEY_LENGTH));
    }
}