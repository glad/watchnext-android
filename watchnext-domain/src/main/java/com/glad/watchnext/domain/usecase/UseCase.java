package com.glad.watchnext.domain.usecase;

/**
 * An interface contract for working with use cases
 * <p>
 * Created by Gautam Lad
 */
public interface UseCase<Result, Arguments> {
    Result execute(final Arguments... arguments);
}