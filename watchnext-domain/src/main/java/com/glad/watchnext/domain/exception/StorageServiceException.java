package com.glad.watchnext.domain.exception;

/**
 * Created by Gautam Lad
 */
public final class StorageServiceException extends Throwable {

    public StorageServiceException(final String message, final Throwable throwable) {
        super("Failed to perform storage operation :: " + message, throwable);
    }

    public StorageServiceException(final Throwable throwable) {
        this(throwable.getMessage(), throwable);
    }

    public StorageServiceException(final String message) {
        super(message);
    }
}