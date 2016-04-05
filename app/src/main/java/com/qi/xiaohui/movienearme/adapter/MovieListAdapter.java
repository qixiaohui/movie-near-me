package com.qi.xiaohui.movienearme.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qi.xiaohui.movienearme.R;
import com.qi.xiaohui.movienearme.model.movies.Movies;
import com.qi.xiaohui.movienearme.model.movies.Result;
import com.squareup.picasso.Picasso;

/**
 * Created by TQi on 4/4/16.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private Context mContext;
    private Movies movies;

    public MovieListAdapter(Context context, Movies data){
        mContext = context;
        movies = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_movies, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Result movie = movies.getResults().get(position);
        holder.movieName.setText(movie.getTitle());
        Picasso.with(mContext).load(mContext.getResources().getString(R.string.poster_base)+movie.getPosterPath()).into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return movies.getResults().size();
    }
}
