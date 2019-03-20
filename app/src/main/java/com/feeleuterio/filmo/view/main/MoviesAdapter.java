package com.feeleuterio.filmo.view.main;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feeleuterio.filmo.R;
import com.feeleuterio.filmo.api.model.Images;
import com.feeleuterio.filmo.api.model.Movie;
import com.github.florent37.glidepalette.GlidePalette;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private List<Movie> movies;
    private Activity activity;
    private Images images;
    private ItemClickListener itemClickListener;

    public MoviesAdapter(List<Movie> movies, Activity activity, Images images, ItemClickListener itemClickListener) {
        this.movies = movies;
        this.activity = activity;
        this.images = images;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movie movie = movies.get(position);

        String fullImageUrl = getFullImageUrl(movie);
        if (!fullImageUrl.isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .error(R.drawable.movies_white_48dp);
            Glide.with(activity)
                    .load(fullImageUrl)
                    .listener(GlidePalette.with(fullImageUrl)
                            .use(GlidePalette.Profile.MUTED_DARK)
                            .use(GlidePalette.Profile.VIBRANT_DARK)
                            .intoBackground(holder.layoutItemDescription)
                            .crossfade(true))
                    .apply(options)
                    .transition(withCrossFade())
                    .into(holder.imageView);
        }
        holder.titleTextView.setText(movie.title);
        holder.releaseTitleTextView.setText(R.string.title_release_date);
        holder.releaseDateTextView.setText(getReleaseDate(movie.releaseDate));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(movie.id, movie.title);
            }
        });
    }

    private String getReleaseDate(String releaseDate) {
        return TextUtils.isEmpty(releaseDate) ? "-" : releaseDate;
    }

    @NonNull
    private String getFullImageUrl(Movie movie) {
        String imagePath;

        if (movie.posterPath != null && !movie.posterPath.isEmpty()) {
            imagePath = movie.posterPath;
        } else {
            imagePath = movie.backdropPath;
        }

        if (images != null && images.baseUrl != null && !images.baseUrl.isEmpty()) {
            if (images.posterSizes != null) {
                if (images.posterSizes.size() > 4) {
                    return images.baseUrl + images.posterSizes.get(4) + imagePath;
                } else {
                    return images.baseUrl + "w500" + imagePath;
                }
            }
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void clear() {
        movies.clear();
    }

    public void addAll(List<Movie> movies) {
        this.movies.addAll(movies);
    }

    public void setImages(Images images) {
        this.images = images;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        @BindView(R.id.titleTextView)
        TextView titleTextView;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.releaseTitleTextView)
        TextView releaseTitleTextView;
        @BindView(R.id.releaseDateTextView)
        TextView releaseDateTextView;
        @BindView(R.id.layoutItemDescription)
        LinearLayout layoutItemDescription;

        ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ItemClickListener {

        void onItemClick(int movieId, String title);
    }

}
