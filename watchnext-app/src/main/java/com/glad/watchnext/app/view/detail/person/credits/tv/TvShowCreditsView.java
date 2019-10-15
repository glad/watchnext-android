package com.glad.watchnext.app.view.detail.person.credits.tv;

import com.glad.watchnext.R;
import com.glad.watchnext.app.di.Injector;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.app.view.detail.adapter.HorizontalImageViewAdapter;
import com.glad.watchnext.app.view.detail.adapter.HorizontalImageViewModel;
import com.glad.watchnext.app.view.model.person.TvShowCreditPresentationModel;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.platform.provider.ImageProvider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.Serializable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gautam Lad
 */
public final class TvShowCreditsView extends FrameLayout implements TvShowCreditsContract.View {
    @NonNull private static final String TAG = LogUtil.getTag(TvShowCreditsView.class);

    @Inject TvShowCreditsContract.Presenter presenter;
    @Inject ImageProvider imageProvider;
    @Inject LogService log;

    @BindView (R.id.recycler_view) RecyclerView recyclerView;

    @NonNull private final HorizontalImageViewAdapter viewAdapter;

    //region Constructor
    public TvShowCreditsView(@NonNull final Context context) {
        this(context, null);
    }

    public TvShowCreditsView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvShowCreditsView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TvShowCreditsView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Injector.INSTANCE.inject(this);
        inflate(context, R.layout.layout_view_tvshow_credits, this);
        ButterKnife.bind(this);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(viewAdapter = new HorizontalImageViewAdapter(imageProvider, R.drawable.ic_person_profile_placeholder));
        viewAdapter.getClickObservable()
                .subscribe(presenter::onClicked);
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
        presenter.unsubscribe();
    }

    @Override
    public void onDestroy() {
        log.d(TAG, "onDestroy() called");
        presenter.unsubscribe();
    }

    @Override
    public void onError(@NonNull final Throwable throwable) {
        // TODO: Handle error
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
    //endregion ViewPresenterContract.View

    //region TvShowCreditsContract.View
    @Override
    public void onLoadStarted() {
        viewAdapter.clear();
    }

    @Override
    public void onLoaded(@NonNull final TvShowCreditPresentationModel credit) {
        viewAdapter.add(HorizontalImageViewModel.newBuilder()
                .id(credit.getId())
                .imageUrl(credit.getPosterImageUrl())
                .build());
    }

    @Override
    public void onLoadComplete() {
        recyclerView.setAdapter(viewAdapter);
    }
    //endregion TvShowCreditsContract.View
}