package com.glad.watchnext.domain.exception;

/**
 * Created by Gautam Lad
 */
public final class InvalidTvShowException extends Throwable {
    public InvalidTvShowException(final Throwable throwable) {
        super("Failed to validate TvShow model (" + throwable.getMessage() + ")", throwable);
    }
}
