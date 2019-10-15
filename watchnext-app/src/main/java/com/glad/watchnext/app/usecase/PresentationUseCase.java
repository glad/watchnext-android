package com.glad.watchnext.app.usecase;

/**
 * An interface contract for working with use cases
 * <p>
 * Created by Gautam Lad
 */
public interface PresentationUseCase<Result, Arguments> {
    Result execute(final boolean ignoreCache, final Arguments... arguments);
}
