package com.glad.watchnext.app.view.widget.networkstate;

import com.glad.watchnext.R;
import com.glad.watchnext.app.di.Injector;
import com.glad.watchnext.platform.util.AnimUtil;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.domain.provider.NetworkStateProvider;
import com.glad.watchnext.domain.provider.SchedulerProvider;
import com.glad.watchnext.domain.service.LogService;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Gautam Lad
 */
public final class NetworkStateWidgetView extends FrameLayout implements NetworkStateWidgetContract.View {
    @NonNull private static final String TAG = LogUtil.getTag(NetworkStateWidgetView.class);
    @IntRange (from = 0L) private static final long HIDE_ON_CONNECTION_RESTORED_DELAY = 2000L;

    @BindView (R.id.txt_network_state) TextView networkStateTextView;
    @BindView (R.id.img_network_state) View networkStateImageView;

    @Inject NetworkStateWidgetContract.Presenter presenter;
    @Inject SchedulerProvider schedulerProvider;
    @Inject NetworkStateProvider networkStateProvider;
    @Inject LogService log;

    @NonNull private final CompositeDisposable disposables = new CompositeDisposable();

    //region Constructor
    public NetworkStateWidgetView(@NonNull final Context context) {
        this(context, null);
    }

    public NetworkStateWidgetView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetworkStateWidgetView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public NetworkStateWidgetView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inflate(context, R.layout.layout_view_network_state, this);
        if (!isInEditMode()) {
            Injector.INSTANCE.inject(this);
            ButterKnife.bind(this);
        }

        setVisibility(GONE);
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
    }

    @Override
    public void onUnsubscribe() {
        log.d(TAG, "onUnsubscribe() called");
        disposables.clear();
        presenter.unsubscribe();
    }

    @Override
    public void onDestroy() {
        log.d(TAG, "onDestroy() called");
        disposables.clear();
        presenter.unsubscribe();
    }

    @Override
    public void onError(@NonNull final Throwable throwable) {
    }
    //endregion ViewPresenterContract.View

    //region NetworkStateWidgetContract.View
    @Override
    public void showNetworkRestored() {
        setBackgroundColor(getResources().getColor(R.color.colorNetworkStateRestored));
        networkStateImageView.setEnabled(true);
        networkStateTextView.setText(R.string.network_state_restored);
        disposables.add(Observable.timer(HIDE_ON_CONNECTION_RESTORED_DELAY, TimeUnit.MILLISECONDS)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(ignored -> AnimUtil.create(this, R.anim.slide_down,
                        ignore -> setVisibility(GONE))));
    }

    @Override
    public void showNetworkDisconnected() {
        disposables.clear();
        setBackgroundColor(getResources().getColor(R.color.colorNetworkStateDisconnected));
        networkStateImageView.setEnabled(false);
        networkStateTextView.setText(R.string.network_state_disconnected);
        AnimUtil.create(this, R.anim.slide_up, ignore -> setVisibility(VISIBLE));
    }
    //endregion NetworkStateWidgetContract.View
}