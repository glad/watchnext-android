package com.glad.watchnext.domain.implementation.provider;

import com.glad.watchnext.domain.provider.SchedulerProvider;

import io.reactivex.Scheduler;
import io.reactivex.internal.schedulers.ImmediateThinScheduler;

/**
 * Created by Gautam Lad
 */
public class TestSchedulerProvider implements SchedulerProvider {
    public static final TestSchedulerProvider INSTANCE = new TestSchedulerProvider();
    private static final Scheduler scheduler = ImmediateThinScheduler.INSTANCE;

    private TestSchedulerProvider() {
    }

    @Override
    public Scheduler ui() {
        return scheduler;
    }

    @Override
    public Scheduler io() {
        return scheduler;
    }
}
