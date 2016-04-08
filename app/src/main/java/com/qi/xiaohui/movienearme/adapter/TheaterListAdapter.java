package com.qi.xiaohui.movienearme.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.aakira.expandablelayout.Utils;
import com.qi.xiaohui.movienearme.R;
import com.qi.xiaohui.movienearme.activity.MovieDetailActivity;
import com.qi.xiaohui.movienearme.model.theaters.Movie;
import com.qi.xiaohui.movienearme.model.theaters.Theater;
import com.qi.xiaohui.movienearme.service.LocationService;
import com.qi.xiaohui.movienearme.service.Util;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by TQi on 4/6/16.
 */
public class TheaterListAdapter extends RecyclerView.Adapter<TheaterListAdapter.ViewHolder>{
    final private String TAG = "TheaterListAdapter";
    private final List<Theater> theaters;
    private Context mContext;
    private Location location;
    private MovieDetailActivity movieDetailActivity;

    private SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();

    public TheaterListAdapter(final List<Theater> theaters, Location location){
        this.theaters = theaters;
        for(int i=0; i<theaters.size(); i++){
            sparseBooleanArray.append(i, false);
        }
        this.location = location;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public RelativeLayout buttonLayout;
        public ExpandableRelativeLayout expandableRelativeLayout;
        public TextView theaterAddress;
        public TextView phoneNumber;
        public ImageView map;
        public LinearLayout rowContainer;
        public ViewHolder(View v){
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button);
            expandableRelativeLayout = (ExpandableRelativeLayout) v.findViewById(R.id.expandableLayout);
            theaterAddress = (TextView) v.findViewById(R.id.theaterAddress);
            phoneNumber = (TextView) v.findViewById(R.id.theaterPhone);
            map = (ImageView) v.findViewById(R.id.map);
            rowContainer = (LinearLayout) v.findViewById(R.id.rowContainer);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_view_list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Theater theater = theaters.get(position);
        final Resources resources = mContext.getResources();
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        if(theaters.get(0).getMovie().get(0).getTrailer() != null && movieDetailActivity != null){
            movieDetailActivity.renderTrailer(theaters.get(0).getMovie().get(0).getTrailer());
        }
        holder.textView.setText(theater.getName());
        holder.theaterAddress.setText(theater.getAddress());
        holder.phoneNumber.setText(theater.getPhoneNumber());
        holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.lightGrey));
        holder.expandableRelativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.darkGrey));
        holder.expandableRelativeLayout.setInterpolator(Utils.createInterpolator(Utils.FAST_OUT_SLOW_IN_INTERPOLATOR));
        holder.expandableRelativeLayout.setExpanded(sparseBooleanArray.get(position));
        for(Movie movie : theater.getMovie()){
            TableLayout tableLayout = new TableLayout(mContext);
            TableLayout.LayoutParams tableParam = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tableLayout.setLayoutParams(tableParam);
            TableRow.LayoutParams rowParam = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TableRow row = new TableRow(mContext);
            row.setBackgroundResource(R.drawable.row_border);
            row.setLayoutParams(rowParam);
            TextView movieName = new TextView(mContext);
            movieName.setText(movie.getName() + "  " + movie.getRuntime() + "   " + movie.getRating());
            movieName.setTextColor(mContext.getResources().getColor(R.color.white));
            movieName.setPadding(5, 5, 5, 5);
            movieName.setTextSize(12);
            movieName.setWidth(Util.getScreenSize(mContext)[0]);
            row.addView(movieName);
            TableRow.LayoutParams cellLayout = (TableRow.LayoutParams)movieName.getLayoutParams();
            cellLayout.span = movie.getShowtimes().size();
            movieName.setLayoutParams(cellLayout);
            tableLayout.addView(row);

            TableRow.LayoutParams rowParam1 = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TableRow row1 = new TableRow(mContext);
            row1.setLayoutParams(rowParam1);
            int width = Util.getScreenSize(mContext)[0]/movie.getShowtimes().size();

            for(String showTime : movie.getShowtimes()){
                TextView showTimeText = new TextView(mContext);
                showTimeText.setText(showTime);
                showTimeText.setTextColor(mContext.getResources().getColor(R.color.white));
                showTimeText.setPadding(5, 5, 5, 5);
                showTimeText.setTextSize(12);
                showTimeText.setWidth(width);
                showTimeText.setBackgroundResource(R.drawable.row_border);
                row1.addView(showTimeText);
                TableRow.LayoutParams cellLayout1 = (TableRow.LayoutParams)showTimeText.getLayoutParams();
                cellLayout1.span = 1;
                showTimeText.setLayoutParams(cellLayout1);
            }
            tableLayout.addView(row1);
            holder.rowContainer.addView(tableLayout);
        }
        holder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Geocoder geocoder = new Geocoder(mContext);
                String map = "http://maps.google.co.in/maps?q=" + theater.getAddress();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                mContext.startActivity(intent);
            }
        });
        holder.expandableRelativeLayout.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                createRotationAnimation(holder.buttonLayout, 0f, 180f).start();
                sparseBooleanArray.put(position, true);
            }

            @Override
            public void onPreClose() {
                createRotationAnimation(holder.buttonLayout, 180f, 0f).start();
                sparseBooleanArray.put(position, false);
            }
        });

        holder.buttonLayout.setRotation(sparseBooleanArray.get(position)? 180f : 0f);
        holder.buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.expandableRelativeLayout.invalidate();
                holder.expandableRelativeLayout.toggle();
            }
        });
    }

    @Override
    public int getItemCount() {
        return theaters.size();
    }

    public void setMovieDetailActivity(MovieDetailActivity movieDetailActivity){
        this.movieDetailActivity = movieDetailActivity;
    }

    private ObjectAnimator createRotationAnimation(final View target, final float from, final float to){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        objectAnimator.setDuration(300);
        objectAnimator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return objectAnimator;
    }

}
