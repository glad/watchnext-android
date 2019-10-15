package com.glad.watchnext.domain.service;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * An interface for a simple CRUD storage service
 * <p>
 * Created by Gautam Lad
 */
public interface StorageService {
    String DEFAULT_STORAGE_NAME = "default";

    /**
     * @return An {@link Editor} instance for a particular named file
     */
    Editor edit();

    /**
     * @param name The name of the file to open an editor for
     *
     * @return An {@link Editor} instance for a particular named file
     */
    Editor edit(final String name);

    /**
     * An interface for performing read/write operations on the storage service
     */
    interface Editor {
        /**
         * Clears all records from storage
         *
         * @return {@link Completable#complete()}) if the operation completes, otherwise {@link Completable#error(Throwable)} on failure
         */
        Completable clear();

        /**
         * @param key The key to delete
         *
         * @return {@link Completable#complete()}) if the operation completes, otherwise {@link Completable#error(Throwable)} on failure
         */
        Completable delete(final String key);

        /**
         * @param key The key to check if it exists
         *
         * @return {@link Single<Boolean>} returning true if it exists or false otherwise (including errors)
         */
        Single<Boolean> exists(final String key);

        /**
         * @return {@link Observable<SimpleEntry>} returning all values in storage
         */
        Observable<SimpleEntry<String, ? extends Serializable>> readAll();

        //region Write
        /**
         * @param key   The key to write. If the key exists, it will be overwritten
         * @param value The boolean value to write
         *
         * @return {@link Completable#complete()}) if the operation completes, otherwise {@link Completable#error(Throwable)} on failure
         */
        Completable writeBoolean(final String key, final boolean value);

        /**
         * @param key   The key to write. If the key exists, it will be overwritten
         * @param value The char value to write
         *
         * @return {@link Completable#complete()}) if the operation completes, otherwise {@link Completable#error(Throwable)} on failure
         */
        Completable writeString(final String key, final String value);

        /**
         * @param key   The key to write. If the key exists, it will be overwritten
         * @param value The int value to write
         *
         * @return {@link Completable#complete()}) if the operation completes, otherwise {@link Completable#error(Throwable)} on failure
         */
        Completable writeInt(final String key, final int value);

        /**
         * @param key   The key to write. If the key exists, it will be overwritten
         * @param value The long value to write
         *
         * @return {@link Completable#complete()}) if the operation completes, otherwise {@link Completable#error(Throwable)} on failure
         */
        Completable writeLong(final String key, final long value);

        /**
         * @param key   The key to write. If the key exists, it will be overwritten
         * @param value The float value to write
         *
         * @return {@link Completable#complete()}) if the operation completes, otherwise {@link Completable#error(Throwable)} on failure
         */
        Completable writeFloat(final String key, final float value);

        /**
         * @param key   The key to write. If the key exists, it will be overwritten
         * @param value The serializable value to write
         *
         * @return {@link Completable#complete()}) if the operation completes, otherwise {@link Completable#error(Throwable)} on failure
         */
        Completable writeSerializable(final String key, final Serializable value);
        //endregion Write

        //region Read
        /**
         * @param key The key to retrieve the boolean value for
         *
         * @return {@link Single<Boolean>} containing a value
         */
        Single<Boolean> readBoolean(final String key);

        /**
         * @param key The key to retrieve the String value for
         *
         * @return {@link Single<String>} containing a value
         */
        Single<String> readString(final String key);

        /**
         * @param key The key to retrieve the int value for
         *
         * @return {@link Single<Integer>} containing a value
         */
        Single<Integer> readInteger(final String key);

        /**
         * @param key The key to retrieve the long value for
         *
         * @return {@link Single<Long>} containing a value
         */
        Single<Long> readLong(final String key);

        /**
         * @param key The key to retrieve the float value for
         *
         * @return {@link Single<Float>} containing a value
         */
        Single<Float> readFloat(final String key);

        /**
         * @param key The key to retrieve the serializable value for
         *
         * @return {@link Single<Serializable>} containing a value
         */
        Single<Serializable> readSerializable(final String key);

        //endregion Read
    }
}