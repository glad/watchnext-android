package com.glad.watchnext.domain.exception;

/**
 * Created by Gautam Lad
 */
public final class InvalidSettingsException extends Throwable {
    public InvalidSettingsException(final Throwable throwable) {
        super("Failed to validate AppConfig model (" + throwable.getMessage() + ")", throwable);
    }
}
