package com.glad.watchnext.app.view.splash;

import com.glad.watchnext.R;
import com.glad.watchnext.app.di.Injector;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.domain.provider.SchedulerProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.NavigationService;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

@SuppressWarnings ({"WeakerAccess", "ConstantConditions", "FieldCanBeLocal"})
public final class SplashView extends FrameLayout implements SplashContract.View {
    @NonNull private static final String TAG = LogUtil.getTag(SplashView.class);

    @Inject SplashContract.Presenter presenter;
    @Inject SchedulerProvider schedulerProvider;
    @Inject NavigationService navigation;
    @Inject LogService log;

    @BindView (R.id.progress) ProgressBar progressBar;

    @NonNull private final Random RANDOM = new Random();
    @NonNull private final CompositeDisposable disposables = new CompositeDisposable();

    //region Constructor
    public SplashView(@NonNull final Context context) {
        this(context, null);
    }

    public SplashView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplashView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SplashView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Injector.INSTANCE.inject(this);
        inflate(context, R.layout.layout_view_splash, this);
        ButterKnife.bind(this);
    }

    //endregion Constructor

    //region ViewPresenterContract.View
    @Override
    public void onCreated(@NonNull final Serializable... args) {
        log.d(TAG, "onCreated() called with: args = [" + args.length + "]");
        presenter.bind(this, args);
    }

    @Override
    public void onSubscribe() {
        log.d(TAG, "onSubscribe() called");
        presenter.subscribe();

        disposables.add(Observable.interval(100, TimeUnit.MILLISECONDS)
                .observeOn(schedulerProvider.ui())
                .doOnDispose(() -> navigation.navigateToHome())
                .takeWhile(ignore -> progressBar.getProgress() < 99)
                .subscribe(ignore -> {
                    final int progress = progressBar.getProgress();
                    if (progress < 100) {
                        progressBar.setProgress(Math.min(100, progress + Math.round(RANDOM.nextFloat() * 25)));
                    }
                }));
    }

    @Override
    public void onUnsubscribe() {
        log.d(TAG, "onUnsubscribe() called");
        presenter.unsubscribe();
    }

    @Override
    public void onDestroy() {
        log.d(TAG, "onDestroy() called");
    }

    @Override
    public void onError(@NonNull final Throwable throwable) {
        // TODO: Handle error
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
    //endregion ViewPresenterContract.View
}