package com.qi.xiaohui.movienearme.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qi.xiaohui.movienearme.R;

/**
 * Created by TQi on 4/4/16.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private Context mContext;

    public MovieListAdapter(Context context){
        mContext = context;
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
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
