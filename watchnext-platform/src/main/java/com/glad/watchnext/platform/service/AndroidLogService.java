package com.glad.watchnext.platform.service;

import com.glad.watchnext.domain.service.LogService;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by Gautam Lad
 */
public final class AndroidLogService implements LogService {
    @NonNull
    private static String safeTag(@NonNull final String tag) {
        return tag.length() <= 22 ? tag : tag.substring(0, 22);
    }

    /**
     * @see android.util.Log#i(String, String)
     */
    @Override
    public void i(@NonNull final String tag, @NonNull final String msg) {
        Log.i(safeTag(tag), msg);
    }

    /**
     * @see android.util.Log#d(String, String)
     */
    @Override
    public void d(@NonNull final String tag, @NonNull final String msg) {
        Log.d(safeTag(tag), msg);
    }

    /**
     * @see android.util.Log#w(String, String)
     */
    @Override
    public void w(@NonNull final String tag, @NonNull final String msg) {
        Log.w(safeTag(tag), msg);
    }

    /**
     * @see android.util.Log#e(String, String, Throwable)
     */
    @Override
    public void e(@NonNull final String tag, @NonNull final String msg, @NonNull final Throwable throwable) {
        Log.e(safeTag(tag), msg, throwable);
    }

    /**
     * @see android.util.Log#e(String, String)
     */
    @Override
    public void e(@NonNull final String tag, @NonNull final String msg) {
        Log.e(safeTag(tag), msg);
    }
}
