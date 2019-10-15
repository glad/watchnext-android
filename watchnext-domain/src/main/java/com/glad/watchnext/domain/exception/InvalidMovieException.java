package com.glad.watchnext.domain.exception;

/**
 * Created by Gautam Lad
 */
public final class InvalidMovieException extends Throwable {
    public InvalidMovieException(final Throwable throwable) {
        super("Failed to validate Movie model (" + throwable.getMessage() + ")", throwable);
    }
}
