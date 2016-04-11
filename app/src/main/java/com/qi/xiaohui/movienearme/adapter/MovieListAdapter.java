package com.qi.xiaohui.movienearme.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qi.xiaohui.movienearme.R;
import com.qi.xiaohui.movienearme.model.movies.Movies;
import com.qi.xiaohui.movienearme.model.movies.Result;
import com.qi.xiaohui.movienearme.model.theaters.Movie;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by TQi on 4/4/16.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private Context mContext;
    private Movies movies;
    private OnItemClickListener itemClickListener;

    public MovieListAdapter(Context context, Movies data){
        mContext = context;
        movies = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public LinearLayout movieHolder;
        public LinearLayout movieNameHolder;
        public TextView movieName;
        public ImageView poster;
        public ViewHolder(View mView){
            super(mView);
            movieHolder = (LinearLayout) mView.findViewById(R.id.mainHolder);
            movieNameHolder = (LinearLayout) mView.findViewById(R.id.movieNameHolder);
            movieName = (TextView) mView.findViewById(R.id.movieName);
            poster = (ImageView) mView.findViewById(R.id.moviePoster);
            movieHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null){
                itemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setMovies(Movies movies){
        this.movies = movies;
    }

    public Movies getMovies(){
        return this.movies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_movies, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Palette.PaletteAsyncListener listener = new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        int bgColor = palette.getVibrantColor(mContext.getResources().getColor(R.color.cardview_dark_background));
                        holder.movieNameHolder.setBackgroundColor(bgColor);
                    }
                };
                Palette.from(bitmap).generate(listener);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        final Result movie = movies.getResults().get(position);
        holder.movieName.setText(movie.getTitle());
        Picasso.with(mContext).load(mContext.getResources().getString(R.string.poster_base)+movie.getPosterPath()).into(holder.poster);
        Picasso.with(mContext).load(mContext.getResources().getString(R.string.poster_base)+movie.getPosterPath()).into(target);

    }

    @Override
    public int getItemCount() {
        return movies.getResults().size();
    }

    public void setOnItemClickListener(final OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
