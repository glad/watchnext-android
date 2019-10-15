package com.glad.watchnext.platform.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Gautam Lad
 */
public final class SerializationHelper {
    /**
     * @return A deserialized object or null if it cannot be converted
     */
    @Nullable
    public static Object deserialize(@NonNull final String s) throws IOException, ClassNotFoundException {
        return new ObjectInputStream(new ByteArrayInputStream(Base64.decode(s, Base64.DEFAULT)))
                .readObject();
    }

    /**
     * @return A serialized string of an object or null if it cannot be converted
     */
    @Nullable
    public static String serialize(@NonNull final Serializable o) throws IOException {
        final ByteArrayOutputStream bo = new ByteArrayOutputStream();
        final ObjectOutputStream so = new ObjectOutputStream(bo);
        so.writeObject(o);
        so.flush();
        return Base64.encodeToString(bo.toByteArray(), Base64.DEFAULT);
    }
}
