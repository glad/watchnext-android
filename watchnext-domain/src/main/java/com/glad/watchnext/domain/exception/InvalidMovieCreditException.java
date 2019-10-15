package com.glad.watchnext.domain.exception;

/**
 * Created by Gautam Lad
 */
public final class InvalidMovieCreditException extends Throwable {
    public InvalidMovieCreditException(final Throwable throwable) {
        super("Failed to validate MovieCredit model (" + throwable.getMessage() + ")", throwable);
    }
}
