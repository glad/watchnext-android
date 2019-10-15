package com.glad.watchnext.domain.exception;

/**
 * Created by Gautam Lad
 */
public final class InvalidGenreException extends Throwable {
    public InvalidGenreException(final Throwable throwable) {
        super("Failed to validate Genre model (" + throwable.getMessage() + ")", throwable);
    }
}
