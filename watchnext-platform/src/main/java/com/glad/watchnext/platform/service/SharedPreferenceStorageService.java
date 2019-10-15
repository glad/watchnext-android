package com.glad.watchnext.platform.service;

import com.glad.watchnext.domain.exception.StorageServiceException;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.StorageService;
import com.glad.watchnext.domain.util.ValueHelper;
import com.glad.watchnext.platform.util.SerializationHelper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Gautam Lad
 */
public final class SharedPreferenceStorageService implements StorageService {
    @NonNull private final Application application;

    @NonNull private final LogService log;

    public SharedPreferenceStorageService(
            @NonNull final Application application,
            @NonNull final LogService log) {
        this.application = application;
        this.log = log;
    }

    @Override
    public StorageService.Editor edit() {
        return edit(DEFAULT_STORAGE_NAME);
    }

    @Override
    public StorageService.Editor edit(final String name) {
        return new Editor(name);
    }

    //region Editor
    public class Editor implements StorageService.Editor {
        @NonNull private final SharedPreferences sharedPreferences;

        Editor(@NonNull final String groupKey) {
            sharedPreferences = application.getSharedPreferences(groupKey, Context.MODE_PRIVATE);
        }

        @Override
        public Completable clear() {
            sharedPreferences.edit().clear().apply();
            return Completable.complete();
        }

        @Override
        public Completable delete(@NonNull final String key) {
            if (sharedPreferences.contains(key)) {
                sharedPreferences.edit()
                        .remove(key)
                        .apply();
                return Completable.complete();
            } else {
                return Completable.error(new StorageServiceException("Delete operation failed.  Key '" + key + "' does not exist"));
            }
        }

        @Override
        public Single<Boolean> exists(@NonNull final String key) {
            return Single.just(sharedPreferences.contains(key));
        }

        //region Write
        @Override
        public Completable writeString(@NonNull final String key, @NonNull final String value) {
            sharedPreferences.edit().putString(key, ValueHelper.nullToEmpty(value)).apply();
            return Completable.complete();
        }

        @Override
        public Completable writeBoolean(@NonNull final String key, final boolean value) {
            return writeString(key, String.valueOf(value));
        }

        @Override
        public Completable writeInt(@NonNull final String key, final int value) {
            return writeString(key, String.valueOf(value));
        }

        @Override
        public Completable writeLong(@NonNull final String key, final long value) {
            return writeString(key, String.valueOf(value));
        }

        @Override
        public Completable writeFloat(@NonNull final String key, final float value) {
            return writeString(key, String.valueOf(value));
        }

        @Override
        public Completable writeSerializable(@NonNull final String key, @NonNull final Serializable value) {
            try {
                final String serializedValue = SerializationHelper.serialize(value);
                if (serializedValue != null) {
                    return writeString(key, serializedValue);
                }
                throw new IOException("Serialized value is null");
            } catch (final IOException e) {
                return Completable.error(e);
            }
        }
        //endregion Write

        //region Read
        @Override
        public Observable<SimpleEntry<String, ? extends Serializable>> readAll() {
            return Observable.fromIterable(sharedPreferences.getAll().keySet())
                    .flatMap(key -> {
                        if (sharedPreferences.contains(key)) {
                            final String value = sharedPreferences.getString(key, null);
                            if (value != null) {
                                return Observable.just(new SimpleEntry<>(key, value));
                            }
                        }

                        return Observable.empty();
                    });
        }

        @Override
        public Single<String> readString(@NonNull final String key) {
            try {
                if (sharedPreferences.contains(key)) {
                    final String value = sharedPreferences.getString(key, null);
                    if (value != null) {
                        return Single.just(value);
                    }
                    throw new IOException("Value is null");
                }
                throw new IOException("Key does ont exist");
            } catch (final NumberFormatException | IOException e) {
                return Single.error(new StorageServiceException("Read(int) operation failed for key = [" + key + "]"));
            }
        }

        @Override
        public Single<Boolean> readBoolean(@NonNull final String key) {
            try {
                if (sharedPreferences.contains(key)) {
                    final String value = sharedPreferences.getString(key, null);
                    if (value != null) {
                        return Single.just(Boolean.parseBoolean(value));
                    }
                    throw new IOException("Value is null");
                }
                throw new IOException("Key does ont exist");
            } catch (final IOException e) {
                return Single.error(new StorageServiceException("Read(int) operation failed for key = [" + key + "]"));
            }
        }

        @Override
        public Single<Integer> readInteger(@NonNull final String key) {
            try {
                if (sharedPreferences.contains(key)) {
                    final String value = sharedPreferences.getString(key, null);
                    if (value != null) {
                        return Single.just(Integer.parseInt(value));
                    }
                    throw new IOException("Value is null");
                }
                throw new IOException("Key does ont exist");
            } catch (final NumberFormatException | IOException e) {
                return Single.error(new StorageServiceException("Read(int) operation failed for key = [" + key + "]"));
            }
        }

        @Override
        public Single<Long> readLong(@NonNull final String key) {
            try {
                if (sharedPreferences.contains(key)) {
                    final String value = sharedPreferences.getString(key, null);
                    if (value != null) {
                        return Single.just(Long.parseLong(value));
                    }
                    throw new IOException("Value is null");
                }
                throw new IOException("Key does ont exist");
            } catch (final NumberFormatException | IOException e) {
                return Single.error(new StorageServiceException("Read(long) operation failed for key = [" + key + "]"));
            }
        }

        @Override
        public Single<Float> readFloat(@NonNull final String key) {
            try {
                if (sharedPreferences.contains(key)) {
                    final String value = sharedPreferences.getString(key, null);
                    if (value != null) {
                        return Single.just(Float.parseFloat(value));
                    }
                    throw new IOException("Value is null");
                }
                throw new IOException("Key does ont exist");
            } catch (final NumberFormatException | IOException e) {
                return Single.error(new StorageServiceException("Read(float) operation failed for key = [" + key + "]"));
            }
        }

        @Override
        public Single<Serializable> readSerializable(@NonNull final String key) {
            try {
                if (sharedPreferences.contains(key)) {
                    final Object value = SerializationHelper.deserialize(sharedPreferences.getString(key, ""));
                    if (value != null && value instanceof Serializable) {
                        return Single.just((Serializable) value);
                    }
                    throw new IOException("Value is null");
                }
                throw new IOException("Key does ont exist");
            } catch (final ClassNotFoundException | IOException e) {
                return Single.error(new StorageServiceException("Read(Serializable) operation failed for key = [" + key + "]"));
            }
        }
        //endregion Read
    }
    //endregion Editor
}