package com.glad.watchnext.platform.provider;

import com.glad.watchnext.domain.provider.SchedulerProvider;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Gautam Lad
 */
public final class AndroidSchedulerProvider implements SchedulerProvider {
    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}
