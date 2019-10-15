package com.glad.watchnext.app.view.detail.adapter;

import com.glad.watchnext.R;
import com.glad.watchnext.platform.provider.ImageProvider;
import com.glad.watchnext.platform.provider.ImageProvider.Options;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
public final class HorizontalImageViewAdapter extends Adapter<HorizontalImageViewAdapter.ViewHolder> {
    @NonNull private final ImageProvider imageProvider;
    @DrawableRes private final int placeHolderResource;
    @NonNull private final List<HorizontalImageViewModel> data = new ArrayList<>();
    @NonNull private final CompositeDisposable disposables = new CompositeDisposable();

    @NonNull private final PublishSubject<String> clickObservable = PublishSubject.create();

    public HorizontalImageViewAdapter(@NonNull final ImageProvider imageProvider,
            @DrawableRes final int placeHolderResource) {
        this.imageProvider = imageProvider;
        this.placeHolderResource = placeHolderResource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_horizontal_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final HorizontalImageViewModel model = data.get(position);
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

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void add(@NonNull final HorizontalImageViewModel item) {
        data.add(item);
        notifyItemChanged(data.size() - 1);
    }

    //region ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView (R.id.img_view) ImageView imageView;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(@NonNull final HorizontalImageViewModel model) {
            disposables.add(imageProvider.load(Options.newBuilder()
                    .targetView(imageView)
                    .url(model.getImageUrl())
                    .placeholder(placeHolderResource)
                    .build())
                    .subscribe());
        }
    }
    //endregion ViewHolder
}