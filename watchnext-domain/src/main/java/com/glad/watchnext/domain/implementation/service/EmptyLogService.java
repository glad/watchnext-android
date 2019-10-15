package com.glad.watchnext.domain.implementation.service;

import com.glad.watchnext.domain.service.LogService;

/**
 * Empty implementation of a {@link LogService}
 * <p>
 * Created by Gautam Lad
 */
public class EmptyLogService implements LogService {
    public static final LogService INSTANCE = new EmptyLogService();

    private EmptyLogService() {
    }

    @Override
    public void i(final String tag, final String msg) {
        // No-op
    }

    @Override
    public void d(final String tag, final String msg) {
        // No-op
    }

    @Override
    public void w(final String tag, final String msg) {
        // No-op
    }

    @Override
    public void e(final String tag, final String msg) {
        // No-op
    }

    @Override
    public void e(final String tag, final String msg, final Throwable throwable) {
        // No-op
    }
}
