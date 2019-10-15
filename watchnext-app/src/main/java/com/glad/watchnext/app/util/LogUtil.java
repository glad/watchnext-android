package com.glad.watchnext.app.util;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;

/**
 * Created by Gautam Lad
 */
public final class LogUtil {
    /**
     * @return A log tag (within safe length limits) from the provided {@link Class}
     */
    @NonNull
    public static String getTag(@NonNull final Class c) {
        final String name = c.getSimpleName();
        if (VERSION.SDK_INT <= VERSION_CODES.M && name.length() > 23) {
            return name.substring(0, 23);
        }

        return name;
    }
}
