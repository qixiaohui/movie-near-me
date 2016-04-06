package com.qi.xiaohui.movienearme.activity;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.gson.Gson;
import com.qi.xiaohui.movienearme.R;
import com.qi.xiaohui.movienearme.model.movies.Movies;
import com.qi.xiaohui.movienearme.model.movies.Result;
import com.qi.xiaohui.movienearme.ui.CircleDisplay;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


/**
 * Created by TQi on 4/5/16.
 */
public class MovieDetailActivity extends AppCompatActivity {
    private static final String TAG = "MovieDetailActivity";
    public static final String EXTRA_PARAM = "EXTRA_PARAM";

    private ImageView poster;
    private TextView movieName;
    private LinearLayout nameBanner;
    private Result movie;
    private TextView voteCount;
    private CircleDisplay voteAverage;
    private TextView movieDes;
    private ExpandableRelativeLayout expandSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        poster = (ImageView) findViewById(R.id.poster);
        movieName = (TextView) findViewById(R.id.movieName);
        nameBanner = (LinearLayout) findViewById(R.id.movieNameHolder);
        voteAverage = (CircleDisplay) findViewById(R.id.voteAverage);
        voteCount = (TextView) findViewById(R.id.voteCount);
        movieDes = (TextView) findViewById(R.id.movieDes);
        expandSummary = (ExpandableRelativeLayout) findViewById(R.id.expandableSummary);

        movie = (new Gson()).fromJson(getIntent().getStringExtra(EXTRA_PARAM), Result.class);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                poster.setImageBitmap(bitmap);
                Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        int bgColor = palette.getVibrantColor(getApplicationContext().getResources().getColor(R.color.cardview_dark_background));
                        expandSummary.setBackgroundColor(bgColor);
                        //expandSummary.expand();
                    }
                });
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        movieName.setText(movie.getTitle());
        movieDes.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
        movieDes.setText(movie.getOverview());
        Picasso.with(MovieDetailActivity.this).load(this.getResources().getString(R.string.poster_base) + movie.getPosterPath()).into(target);
        voteCount.setText("Rate by " + movie.getVoteCount() + " people");
        voteAverage.setColor(getApplicationContext().getResources().getColor(R.color.white));
        voteAverage.setAnimDuration(1000);
        voteAverage.setDimAlpha(100);
        voteAverage.setColor(getApplicationContext().getResources().getColor(R.color.lightGrey));
        voteAverage.setValueWidthPercent(10f);
        voteAverage.setTextSize(20f);
        voteAverage.setDrawInnerCircle(true);
        voteAverage.setUnit("");
        voteAverage.setDrawText(true);
        voteAverage.showValue(movie.getVoteAverage().floatValue()*10 ,100f, true);
        //voteAverage.change(movie.getVoteAverage().intValue());
        startCount();
    }

    public void startCount(){
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, movie.getVoteCount());
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                voteCount.setText("Rate by " + (int) animation.getAnimatedValue()+" people");
            }
        });
        animator.start();
    }
}
