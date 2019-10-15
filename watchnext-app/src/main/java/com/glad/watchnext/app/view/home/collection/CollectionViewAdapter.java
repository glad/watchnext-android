package com.glad.watchnext.app.view.home.collection;

import com.glad.watchnext.R;
import com.glad.watchnext.app.view.home.collection.CollectionViewAdapter.ViewHolder;
import com.glad.watchnext.platform.provider.ImageProvider;
import com.glad.watchnext.platform.provider.ImageProvider.Options;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Gautam Lad
 */
public final class CollectionViewAdapter extends Adapter<ViewHolder> {
    @NonNull private final ImageProvider imageProvider;
    @NonNull private final RecyclerView recyclerView;
    @DrawableRes private final int placeHolderResource;
    @NonNull private final List<CollectionViewModel> data = new ArrayList<>();
    @NonNull private final CompositeDisposable disposables = new CompositeDisposable();
    @ViewMode private int currentViewMode;

    //region Constants
    @IntRange (from = 1) private static final int MIN_COLUMNS = 1;
    @IntRange (from = 1) private static final int MAX_COLUMNS = 5;
    @FloatRange (from = 1) private static final float RATIO = 6f / 4f;
    @IntRange (from = MIN_COLUMNS, to = MAX_COLUMNS) private int columnCount = 3;
    //endregion Constants

    //region ViewMode
    @Retention (RetentionPolicy.SOURCE)
    @IntDef ({LIST_VIEWMODE, GRID_VIEWMODE})
    @interface ViewMode {
    }

    public static final int LIST_VIEWMODE = 0;
    public static final int GRID_VIEWMODE = 1;
    //endregion ViewMode

    @NonNull private final PublishSubject<String> clickObservable = PublishSubject.create();

    public CollectionViewAdapter(@NonNull final RecyclerView recyclerView,
            @NonNull final ImageProvider imageProvider,
            @DrawableRes final int placeHolderResource) {
        this.recyclerView = recyclerView;
        this.imageProvider = imageProvider;
        this.placeHolderResource = placeHolderResource;
        this.currentViewMode = GRID_VIEWMODE;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        @LayoutRes final int layout = currentViewMode == GRID_VIEWMODE ?
                R.layout.layout_collection_gridview_item : R.layout.layout_collection_listview_item;

        final View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        if (currentViewMode == GRID_VIEWMODE) {
            view.setMinimumHeight(Math.round((float) (parent.getMeasuredWidth() / MAX_COLUMNS) * RATIO));
            return new GridViewHolder(view);
        }

        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final CollectionViewModel model = data.get(position);
        holder.bind(model);
        holder.itemView.setOnClickListener(i -> clickObservable.onNext(model.getId()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    public Observable<String> getClickObservable() {
        return clickObservable;
    }

    public void updateView(@Nullable final Context context, @ViewMode final int viewMode) {
        if (context == null) {
            return;
        }

        currentViewMode = viewMode;
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);
        recyclerView.setAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(context, viewMode == CollectionViewAdapter.GRID_VIEWMODE ? columnCount : 1));
        recyclerView.setVisibility(View.VISIBLE);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void add(@NonNull final CollectionViewModel item) {
        data.add(item);
        notifyItemChanged(data.size() - 1);
    }

    //region ViewHolder
    abstract class ViewHolder extends RecyclerView.ViewHolder {
        @BindView (R.id.img_view) ImageView imageView;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(@NonNull final CollectionViewModel model) {
            disposables.add(imageProvider.load(Options.newBuilder()
                    .targetView(imageView)
                    .url(model.getImageUrl())
                    .placeholder(placeHolderResource)
                    .build())
                    .subscribe());
        }
    }

    class ListViewHolder extends ViewHolder {
        @BindView (R.id.txt_title) TextView titleTextView;
        @BindView (R.id.txt_body) TextView bodyTextView;

        ListViewHolder(@NonNull final View itemView) {
            super(itemView);
        }

        @Override
        void bind(@NonNull final CollectionViewModel model) {
            super.bind(model);
            titleTextView.setText(model.getTitle());
            bodyTextView.setText(model.getBody());
        }
    }

    class GridViewHolder extends ViewHolder {
        GridViewHolder(@NonNull final View itemView) {
            super(itemView);
        }
    }
    //endregion ViewHolder
}