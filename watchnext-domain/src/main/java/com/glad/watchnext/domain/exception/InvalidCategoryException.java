package com.glad.watchnext.domain.exception;

/**
 * Created by Gautam Lad
 */
public final class InvalidCategoryException extends Throwable {
    public InvalidCategoryException(final Throwable throwable) {
        super("Failed to validate Category model (" + throwable.getMessage() + ")", throwable);
    }
}
