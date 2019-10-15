package com.glad.watchnext.domain.exception;

/**
 * Created by Gautam Lad
 */
public final class EmptyCollectionException extends Throwable {
    public EmptyCollectionException(final Throwable throwable) {
        super("Collection is empty (" + throwable.getMessage() + ")", throwable);
    }
}
