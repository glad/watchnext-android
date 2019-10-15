package com.glad.watchnext.domain.exception;

/**
 * Created by Gautam Lad
 */
public final class InvalidImageException extends Throwable {
    public InvalidImageException(final Throwable throwable) {
        super("Failed to validate Image model (" + throwable.getMessage() + ")", throwable);
    }
}
