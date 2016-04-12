package com.qi.xiaohui.movienearme.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.qi.xiaohui.movienearme.R;
import com.qi.xiaohui.movienearme.fragment.ShowtimeFragment;
import com.qi.xiaohui.movienearme.http.RestClient;
import com.qi.xiaohui.movienearme.http.ShowTimesGateway;
import com.qi.xiaohui.movienearme.model.movies.Movies;
import com.qi.xiaohui.movienearme.model.movies.Result;
import com.qi.xiaohui.movienearme.model.theaters.Theater;
import com.qi.xiaohui.movienearme.service.Util;
import com.qi.xiaohui.movienearme.ui.CircleDisplay;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.Console;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.jar.Manifest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by TQi on 4/5/16.
 */
public class MovieDetailActivity extends AppCompatActivity{
    private static final String TAG = "MovieDetailActivity";
    public static final String EXTRA_PARAM = "EXTRA_PARAM";

    private static final int MY_PERMISSION_CALL_PHONE = 13;

    private ImageView poster;
    private TextView movieName;
    private LinearLayout nameBanner;
    private Result movie;
    private TextView voteCount;
    private CircleDisplay voteAverage;
    private TextView movieDes;
    private ExpandableRelativeLayout expandSummary;
    private ImageView trailer;
    private TextView language;
    private TextView releaseDate;
    private ViewPager viewPager;
    private CardView showButton;
    private TextView showText;

    private String phone;
    private ArrayList<ShowtimeFragment> showtimeList = new ArrayList<>();

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
        trailer = (ImageView) findViewById(R.id.trailer);
        language = (TextView) findViewById(R.id.locale);
        showButton = (CardView) findViewById(R.id.showButton);
        releaseDate = (TextView) findViewById(R.id.releaseTime);
        viewPager = (ViewPager) findViewById(R.id.pager);
        showText = (TextView) findViewById(R.id.showText);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(showtimeList.get(position) != null){
                    resizeViewPager(showtimeList.get(position).getMovieCount());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        ShowtimeAdapter showtimeAdapter = new ShowtimeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(showtimeAdapter);
        viewPager.setOffscreenPageLimit(7);

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandSummary.toggle();
            }
        });

        expandSummary.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onPreOpen() {

            }

            @Override
            public void onPreClose() {

            }

            @Override
            public void onOpened() {
                ;
                showText.setText("Hide Description");
            }

            @Override
            public void onClosed() {
                showText.setText("Show Description");
            }
        });

        expandSummary.setExpanded(true);

        movie = (new Gson()).fromJson(getIntent().getStringExtra(EXTRA_PARAM), Result.class);

        language.setText("Language: " + Util.lanLocale(movie.getOriginalLanguage()));
        releaseDate.setText("Release data: "+movie.getReleaseDate());

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                poster.setImageBitmap(bitmap);
                Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        int bgColor = palette.getVibrantColor(getApplicationContext().getResources().getColor(R.color.cardview_dark_background));
                        expandSummary.setBackgroundColor(bgColor);
                        showButton.setCardBackgroundColor(bgColor);
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
        voteAverage.showValue(movie.getVoteAverage().floatValue() * 10, 100f, true);
        //voteAverage.change(movie.getVoteAverage().intValue());
        startCount();
    }

    public void startCount(){
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, movie.getVoteCount());
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                voteCount.setText("Rate by " + (int) animation.getAnimatedValue() + " people");
            }
        });
        animator.start();
    }

    public void renderTrailer(final String url){
        if(trailer.getVisibility() == View.GONE){
            trailer.setVisibility(View.VISIBLE);
            trailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            });
        }
    }

    public void resizeViewPager(int size){
        int height = size == 0? 300:150*size;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        viewPager.setLayoutParams(params);
    }

    private class ShowtimeAdapter extends FragmentStatePagerAdapter{

        public ShowtimeAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            ShowtimeFragment showtimeFragment = new ShowtimeFragment();
            showtimeFragment.setMovies(movie);
            showtimeFragment.setMovieDetailActivity(MovieDetailActivity.this);
            showtimeFragment.setPosition(position);
            showtimeList.add(showtimeFragment);
            return showtimeFragment;
        }


        @Override
        public int getCount() {
            return 7;
        }
    }

    public void nextShowTimePage(boolean move){
        //true: forward, false: back
        int currentPosition = viewPager.getCurrentItem();
        if(move && currentPosition<=5) {
            int next = currentPosition+1;
            viewPager.setCurrentItem(next);
        }else if(!move && currentPosition>=1){
            int pre = currentPosition-1;
            viewPager.setCurrentItem(pre);
        }
    }

    private void _call(){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
        startActivity(intent);
    }


    public void call(String phone){
        this.phone = phone;
        if ( ContextCompat.checkSelfPermission(MovieDetailActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(MovieDetailActivity.this, new String[]{android.Manifest.permission.CALL_PHONE},
                    MY_PERMISSION_CALL_PHONE);
        }else{
            _call();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSION_CALL_PHONE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    _call();
                }
            }
        }
    }
}
