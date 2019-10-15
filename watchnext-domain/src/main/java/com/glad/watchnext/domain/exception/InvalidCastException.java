package com.glad.watchnext.domain.exception;

/**
 * Created by Gautam Lad
 */
public final class InvalidCastException extends Throwable {
    public InvalidCastException(final Throwable throwable) {
        super("Failed to validate CastCredit model (" + throwable.getMessage() + ")", throwable);
    }
}
