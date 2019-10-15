package com.glad.watchnext.domain.service;

/**
 * An interface contract for providing logging services
 * <p>
 * Created by Gautam Lad
 */
public interface LogService {
    /**
     * Write info related logs
     */
    void i(final String tag, final String msg);

    /**
     * Write debug related logs
     */
    void d(final String tag, final String msg);

    /**
     * Write warning related logs
     */
    void w(final String tag, final String msg);

    /**
     * Write error related logs
     */
    void e(final String tag, final String msg);

    /**
     * Write error related logs
     */
    void e(final String tag, final String msg, final Throwable throwable);
}
