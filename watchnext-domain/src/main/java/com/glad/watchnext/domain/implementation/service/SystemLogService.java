package com.glad.watchnext.domain.implementation.service;

import com.glad.watchnext.domain.service.LogService;

/**
 * Implementation of a {@link LogService} writing to system console
 * <p>
 * Created by Gautam Lad
 */
public class SystemLogService implements LogService {
    public static final LogService INSTANCE = new SystemLogService();

    private SystemLogService() {
    }

    @Override
    public void i(final String tag, final String msg) {
        System.out.println("I/" + tag + ": " + msg);
    }

    @Override
    public void d(final String tag, final String msg) {
        System.out.println("D/" + tag + ": " + msg);
    }

    @Override
    public void w(final String tag, final String msg) {
        System.out.println("W/" + tag + ": " + msg);
    }

    @Override
    public void e(final String tag, final String msg) {
        System.out.println("E/" + tag + ": " + msg);
    }

    @Override
    public void e(final String tag, final String msg, final Throwable throwable) {
        System.out.println("E/" + tag + ": " + msg + "\n" + throwable.getMessage());
    }
}
