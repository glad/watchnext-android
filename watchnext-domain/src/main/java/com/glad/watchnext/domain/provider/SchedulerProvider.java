package com.glad.watchnext.domain.provider;

import io.reactivex.Scheduler;

/**
 * Created by Gautam Lad
 */
public interface SchedulerProvider {
    Scheduler ui();

    Scheduler io();
}
