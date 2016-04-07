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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.qi.xiaohui.movienearme.adapter.TheaterListAdapter;
import com.qi.xiaohui.movienearme.http.RestClient;
import com.qi.xiaohui.movienearme.http.ShowTimesGateway;
import com.qi.xiaohui.movienearme.model.movies.Movies;
import com.qi.xiaohui.movienearme.model.movies.Result;
import com.qi.xiaohui.movienearme.model.theaters.Theater;
import com.qi.xiaohui.movienearme.ui.CircleDisplay;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.Console;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by TQi on 4/5/16.
 */
public class MovieDetailActivity extends AppCompatActivity implements LocationListener{
    private static final String TAG = "MovieDetailActivity";
    public static final String EXTRA_PARAM = "EXTRA_PARAM";

    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    private ImageView poster;
    private TextView movieName;
    private LinearLayout nameBanner;
    private Result movie;
    private TextView voteCount;
    private CircleDisplay voteAverage;
    private TextView movieDes;
    private CardView showButton;
    private TextView showText;
    private ExpandableRelativeLayout expandSummary;
    private RecyclerView listTheater;
    private ImageView trailer;

    private ShowTimesGateway showTimesGateway;
    private LocationManager locationManager;
    private Location location;

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
        showButton = (CardView) findViewById(R.id.showButton);
        showText = (TextView) findViewById(R.id.showText);
        listTheater = (RecyclerView) findViewById(R.id.theaterList);
        trailer = (ImageView) findViewById(R.id.trailer);
        listTheater.setHasFixedSize(true);
        listTheater.setLayoutManager(new LinearLayoutManager(this));

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
        ;       showText.setText("Hide Description");
            }

            @Override
            public void onClosed() {
                showText.setText("Show Description");
            }
        });

        expandSummary.setExpanded(true);

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
        checkLocation();
    }

    private void getShowtimes(Location location){
        showTimesGateway = RestClient.getShowTimesGateway();
        Call<List<Theater>> theaterCall = showTimesGateway.getTheaters(Double.toString(location.getLatitude()),Double.toString(location.getLongitude()), movie.getTitle().replaceAll("[-+.^:,]", "").replaceAll("\\s","").trim());
        theaterCall.enqueue(new Callback<List<Theater>>() {
            @Override
            public void onResponse(Call<List<Theater>> call, Response<List<Theater>> response) {
                if(response.body() != null) {
                    loadRows(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Theater>> call, Throwable t) {
                Log.e("HTTP ERROR", t.toString());
            }
        });
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

    private void checkLocation(){
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
        }
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
            getShowtimes(location);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    private void loadRows(final List<Theater> theaters){
        TheaterListAdapter theaterListAdapter = new TheaterListAdapter(theaters, location);
        theaterListAdapter.setMovieDetailActivity(MovieDetailActivity.this);
        listTheater.setAdapter(theaterListAdapter);
    }

    @Override
    public void onLocationChanged(Location location) {
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
        }

        if (location != null) {
            getShowtimes(location);
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
}
