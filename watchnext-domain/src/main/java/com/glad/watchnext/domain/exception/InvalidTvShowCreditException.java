package com.glad.watchnext.domain.exception;

/**
 * Created by Gautam Lad
 */
public final class InvalidTvShowCreditException extends Throwable {
    public InvalidTvShowCreditException(final Throwable throwable) {
        super("Failed to validate TvShowCredit model (" + throwable.getMessage() + ")", throwable);
    }
}
