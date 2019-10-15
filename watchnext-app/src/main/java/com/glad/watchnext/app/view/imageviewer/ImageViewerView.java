package com.glad.watchnext.app.view.imageviewer;

import com.glad.watchnext.R;
import com.glad.watchnext.app.di.Injector;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.util.ValueHelper;
import com.glad.watchnext.platform.provider.ImageProvider;
import com.glad.watchnext.platform.provider.ImageProvider.Options;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public final class ImageViewerView extends FrameLayout implements ImageViewerContract.View {
    @NonNull private static final String TAG = LogUtil.getTag(ImageViewerView.class);

    @Inject ImageViewerContract.Presenter presenter;
    @Inject ImageProvider imageProvider;
    @Inject LogService log;
    @Inject AppCompatActivity activity;

    @BindView (R.id.viewpager) ViewPager viewPager;
    @BindView (R.id.toolbar) Toolbar toolbar;

    @NonNull private final OnPageChangeListener onPageChangeListener;
    @NonNull private final SparseArray<Disposable> disposables = new SparseArray<>();
    @NonNull private final List<String> imageUrls = new ArrayList<>();

    //region Constructor
    public ImageViewerView(@NonNull final Context context) {
        this(context, null);
    }

    public ImageViewerView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageViewerView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ImageViewerView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Injector.INSTANCE.inject(this);
        inflate(context, R.layout.layout_view_imageviewer, this);
        ButterKnife.bind(this);

        activity.setSupportActionBar(toolbar);
        final ActionBar actionBar = ValueHelper.requireNonNull(activity.getSupportActionBar());
        actionBar.setDisplayHomeAsUpEnabled(true);

        onPageChangeListener = new OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                // No-op
            }

            @Override
            public void onPageSelected(final int position) {
                actionBar.setTitle(String.format(Locale.getDefault(), "%d of %d", position + 1, imageUrls.size()));
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
                // No-op
            }
        };
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }
    //endregion Constructor

    //region ViewPresenterContract.View
    @SuppressWarnings ("unchecked")
    @Override
    public void onCreated(@NonNull final Serializable... args) {
        log.d(TAG, "onCreated() called with: args = [" + args.length + "]");
        presenter.bind(this, args);

        imageUrls.addAll((List) args[0]);
        viewPager.setAdapter(new ViewPagerAdapter());
        onPageChangeListener.onPageSelected(0); // Trigger initial page
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
        viewPager.removeOnPageChangeListener(onPageChangeListener);
        presenter.unsubscribe();
    }

    @Override
    public void onError(@NonNull final Throwable throwable) {
        // TODO: Handle error
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
    //endregion ViewPresenterContract.View

    //region ViewPager Adapter
    class ViewPagerAdapter extends PagerAdapter {
        private final LayoutInflater layoutInflater;
        @BindView (R.id.img_view) ImageView imageView;

        ViewPagerAdapter() {
            layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull final View view, @NonNull final Object object) {
            return view == object;
        }

        @Override
        public void startUpdate(@NonNull final ViewGroup container) {
            super.startUpdate(container);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
            final View view = layoutInflater.inflate(R.layout.layout_imageviewer_item, container, false);
            ButterKnife.bind(this, view);

            container.addView(view);

            disposables.put(position, imageProvider.load(Options.newBuilder()
                    .targetView(imageView)
                    .url(imageUrls.get(position))
                    .placeholder(R.drawable.ic_movie_poster_placeholder)
                    .build())
                    .subscribe());

            return view;
        }

        @Override
        public void destroyItem(@NonNull final ViewGroup container, int position, @NonNull final Object object) {
            container.removeView((FrameLayout) object);
            disposables.valueAt(position).dispose();
        }
    }
    //endregion ViewPager Adapter
}