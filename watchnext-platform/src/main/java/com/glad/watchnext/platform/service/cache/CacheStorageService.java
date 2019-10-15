package com.glad.watchnext.platform.service.cache;

import com.glad.watchnext.domain.exception.StorageServiceException;
import com.glad.watchnext.domain.provider.SettingsProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.StorageService;
import com.glad.watchnext.platform.util.SerializationHelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Gautam Lad
 */
public class CacheStorageService implements StorageService {
    @NonNull private final Context context;
    @NonNull private final SettingsProvider settingsProvider;
    @NonNull private final LogService log;

    public CacheStorageService(
            @NonNull final Context context,
            @NonNull final SettingsProvider settingsProvider,
            @NonNull final LogService log) {
        this.context = context;
        this.settingsProvider = settingsProvider;
        this.log = log;
    }

    @Override
    public StorageService.Editor edit() {
        return edit(DEFAULT_STORAGE_NAME);
    }

    @Override
    public StorageService.Editor edit(final String name) {
        return new CacheStorageService.Editor(name);
    }

    public class Editor implements StorageService.Editor {
        @NonNull private final DiskLruCacheDelegate delegate;

        Editor(@NonNull final String name) {
            this.delegate = new DiskLruCacheDelegate(name, context, settingsProvider, log);
        }

        @Override
        public Completable clear() {
            try {
                delegate.clear();
                return Completable.complete();
            } catch (final IOException e) {
                return Completable.error(new StorageServiceException("Clear operation failed", e));
            }
        }

        @Override
        public Completable delete(@NonNull final String key) {
            try {
                if (delegate.delete(key)) {
                    return Completable.complete();
                }
                throw new IOException();
            } catch (final IOException e) {
                return Completable.error(new StorageServiceException("Delete operation failed for key = [" + key + "]", e));
            }
        }

        @Override
        public Single<Boolean> exists(@NonNull final String key) {
            try {
                return Single.just(delegate.exists(key));
            } catch (final IOException e) {
                return Single.error(new StorageServiceException("Exists operation failed for key = [" + key + "]", e));
            }
        }

        //region Write
        @Override
        public Completable writeBoolean(@NonNull final String key, final boolean value) {
            try {
                delegate.write(key, String.valueOf(value));
                return Completable.complete();
            } catch (@Nullable final IOException e) {
                return Completable.error(new StorageServiceException("Write(boolean) operation failed for key = [" + key + "], value = [" + value + "]", e));
            }
        }

        @Override
        public Completable writeString(@NonNull final String key, final String value) {
            try {
                delegate.write(key, value);
                return Completable.complete();
            } catch (@Nullable final IOException e) {
                return Completable.error(new StorageServiceException("Write(String) operation failed for key = [" + key + "], value = [" + value + "]", e));
            }
        }

        @Override
        public Completable writeInt(@NonNull final String key, final int value) {
            try {
                delegate.write(key, String.valueOf(value));
                return Completable.complete();
            } catch (@Nullable final IOException e) {
                return Completable.error(new StorageServiceException("Write(int) operation failed for key = [" + key + "], value = [" + value + "]", e));
            }
        }

        @Override
        public Completable writeLong(@NonNull final String key, final long value) {
            try {
                delegate.write(key, String.valueOf(value));
                return Completable.complete();
            } catch (@Nullable final IOException e) {
                return Completable.error(new StorageServiceException("Write(long) operation failed for key = [" + key + "], value = [" + value + "]", e));
            }
        }

        @Override
        public Completable writeFloat(@NonNull final String key, final float value) {
            try {
                delegate.write(key, String.valueOf(value));
                return Completable.complete();
            } catch (@Nullable final IOException e) {
                return Completable.error(new StorageServiceException("Write(float) operation failed for key = [" + key + "], value = [" + value + "]", e));
            }
        }

        @Override
        public Completable writeSerializable(@NonNull final String key, final Serializable value) {
            try {
                final String serializedValue = SerializationHelper.serialize(value);
                if (serializedValue != null) {
                    delegate.write(key, serializedValue);
                    return Completable.complete();
                }
                throw new IOException("Serialized value is null");
            } catch (@Nullable final IOException e) {
                return Completable
                        .error(new StorageServiceException("Write(Serializable) operation failed for key = [" + key + "], value = [" + value + "]", e));
            }
        }
        //endregion Write

        //region Read
        @Override
        public Observable<SimpleEntry<String, ? extends Serializable>> readAll() {
            return Observable.error(new UnsupportedOperationException());
        }

        @Override
        public Single<Boolean> readBoolean(@NonNull final String key) {
            try {
                final String value = delegate.read(key);
                if (value != null) {
                    return Single.just(Boolean.parseBoolean(value));
                }
                throw new IllegalStateException("Unknown error");
            } catch (final IllegalStateException | IOException | NumberFormatException e) {
                return Single.error(new StorageServiceException("Read(boolean) operation failed for key = [" + key + "]", e));
            }
        }

        @Override
        public Single<String> readString(@NonNull final String key) {
            try {
                final String value = delegate.read(key);
                if (value != null) {
                    return Single.just(value);
                }
                throw new IllegalStateException("Unknown error");
            } catch (final IllegalStateException | IOException | NumberFormatException e) {
                return Single.error(new StorageServiceException("Read(String) operation failed for key = [" + key + "]", e));
            }
        }

        @Override
        public Single<Integer> readInteger(@NonNull final String key) {
            try {
                final String value = delegate.read(key);
                if (value != null) {
                    return Single.just(Integer.parseInt(value));
                }
                throw new IllegalStateException("Unknown error");
            } catch (final IllegalStateException | IOException | NumberFormatException e) {
                return Single.error(new StorageServiceException("Read(int) operation failed for key = [" + key + "]", e));
            }
        }

        @Override
        public Single<Long> readLong(@NonNull final String key) {
            try {
                final String value = delegate.read(key);
                if (value != null) {
                    return Single.just(Long.parseLong(value));
                }
                throw new IllegalStateException("Unknown error");
            } catch (final IllegalStateException | IOException | NumberFormatException e) {
                return Single.error(new StorageServiceException("Read(long) operation failed for key = [" + key + "]", e));
            }
        }

        @Override
        public Single<Float> readFloat(@NonNull final String key) {
            try {
                final String value = delegate.read(key);
                if (value != null) {
                    return Single.just(Float.parseFloat(value));
                }
                throw new IllegalStateException("Unknown error");
            } catch (final IllegalStateException | IOException | NumberFormatException e) {
                return Single.error(new StorageServiceException("Read(float) operation failed for key = [" + key + "]", e));
            }
        }

        @Override
        public Single<Serializable> readSerializable(@NonNull final String key) {
            try {
                final String value = delegate.read(key);
                if (value != null) {
                    final Object object = SerializationHelper.deserialize(value);
                    if (object instanceof Serializable) {
                        return Single.just((Serializable) object);
                    }
                }

                throw new IllegalStateException("Unknown error");
            } catch (final IllegalStateException | IOException | ClassNotFoundException e) {
                return Single.error(new StorageServiceException("Read(Serializable) operation failed for key = [" + key + "]", e));
            }
        }
        //endregion Read
    }
}