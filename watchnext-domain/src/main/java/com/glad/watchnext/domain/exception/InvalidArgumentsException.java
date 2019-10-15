package com.glad.watchnext.domain.exception;

/**
 * Created by Gautam Lad
 */
public final class InvalidArgumentsException extends Throwable {
    public InvalidArgumentsException(final Throwable throwable) {
        super("Invalid arguments (" + throwable.getMessage() + ")", throwable);
    }
}
