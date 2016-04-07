package com.qi.xiaohui.movienearme.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.aakira.expandablelayout.Utils;
import com.qi.xiaohui.movienearme.R;
import com.qi.xiaohui.movienearme.activity.MovieDetailActivity;
import com.qi.xiaohui.movienearme.model.theaters.Theater;
import com.qi.xiaohui.movienearme.service.LocationService;

import java.util.List;

/**
 * Created by TQi on 4/6/16.
 */
public class TheaterListAdapter extends RecyclerView.Adapter<TheaterListAdapter.ViewHolder>{
    private final List<Theater> theaters;
    private Context mContext;

    private SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();

    public TheaterListAdapter(final List<Theater> theaters){
        this.theaters = theaters;
        for(int i=0; i<theaters.size(); i++){
            sparseBooleanArray.append(i, false);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public RelativeLayout buttonLayout;
        public ExpandableRelativeLayout expandableRelativeLayout;
        public ViewHolder(View v){
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button);
            expandableRelativeLayout = (ExpandableRelativeLayout) v.findViewById(R.id.expandableLayout);
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
        holder.textView.setText(theater.getName());
        holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.lightGrey));
        holder.expandableRelativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.darkGrey));
        holder.expandableRelativeLayout.setInterpolator(Utils.createInterpolator(Utils.FAST_OUT_SLOW_IN_INTERPOLATOR));
        holder.expandableRelativeLayout.setExpanded(sparseBooleanArray.get(position));
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
                holder.expandableRelativeLayout.toggle();
            }
        });
    }

    @Override
    public int getItemCount() {
        return theaters.size();
    }

    private ObjectAnimator createRotationAnimation(final View target, final float from, final float to){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        objectAnimator.setDuration(300);
        objectAnimator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return objectAnimator;
    }

}
