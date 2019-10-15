package com.glad.watchnext.domain.exception;

/**
 * Created by Gautam Lad
 */
public final class InvalidPersonException extends Throwable {
    public InvalidPersonException(final Throwable throwable) {
        super("Failed to validate Person model (" + throwable.getMessage() + ")", throwable);
    }
}
