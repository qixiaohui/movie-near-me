package com.qi.xiaohui.movienearme.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.google.android.gms.maps.LocationSource;
import com.qi.xiaohui.movienearme.R;
import com.qi.xiaohui.movienearme.activity.MovieDetailActivity;
import com.qi.xiaohui.movienearme.adapter.TheaterListAdapter;
import com.qi.xiaohui.movienearme.http.RestClient;
import com.qi.xiaohui.movienearme.http.ShowTimesGateway;
import com.qi.xiaohui.movienearme.model.movies.Movies;
import com.qi.xiaohui.movienearme.model.movies.Result;
import com.qi.xiaohui.movienearme.model.theaters.Theater;
import com.qi.xiaohui.movienearme.service.Util;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TQi on 4/11/16.
 */
public class ShowtimeFragment extends android.support.v4.app.Fragment implements LocationListener{
    private RecyclerView listTheater;
    private LinearLayout linearLayout;

    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    private LocationManager locationManager;
    private Location location;
    private ShowTimesGateway showTimesGateway;
    private Result movie;
    private MovieDetailActivity movieDetailActivity;
    private TextView date;
    private DotProgressBar dotProgressBar;
    private List<Theater> theaters;
    private ShimmerTextView noResult;
    private ImageView arrowBack;
    private ImageView arrowForward;

    private int position = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        linearLayout = (LinearLayout)inflater.inflate(R.layout.showtime_fragment, container, false);
        listTheater = (RecyclerView) linearLayout.findViewById(R.id.theaterList);
        date = (TextView) linearLayout.findViewById(R.id.date);
        dotProgressBar = (DotProgressBar) linearLayout.findViewById(R.id.dot_progress_bar);
        noResult = (ShimmerTextView) linearLayout.findViewById(R.id.noResult);
        arrowBack = (ImageView) linearLayout.findViewById(R.id.arrowBack);
        arrowForward = (ImageView) linearLayout.findViewById(R.id.arrowForward);
        date.setText(Util.getDate(position));
        if(position<=0){
            arrowBack.setVisibility(View.GONE);
        }else if(position>=6){
            arrowForward.setVisibility(View.GONE);
        }

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(movieDetailActivity != null){
                    movieDetailActivity.nextShowTimePage(false);
                }
            }
        });

        arrowForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(movieDetailActivity != null){
                    movieDetailActivity.nextShowTimePage(true);
                }
            }
        });

        listTheater.setHasFixedSize(true);
        listTheater.setNestedScrollingEnabled(true);
        listTheater.setLayoutManager(new LinearLayoutManager(getContext()));

        return linearLayout;
    }

    public void setMovies(Result movie){
        this.movie = movie;
    }

    public void setMovieDetailActivity(MovieDetailActivity movieDetailActivity){
        this.movieDetailActivity = movieDetailActivity;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkLocation();
    }

    private void checkLocation(){
        if ( ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
        }else{
            getLocation();
        }
    }

    private void getLocation(){
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
            getShowtimes(location);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if ( ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
        }

        if (location != null) {
            getShowtimes(location);
            locationManager.removeUpdates(ShowtimeFragment.this);
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

    private void loadRows(final List<Theater> theaters){
        dotProgressBar.setVisibility(View.GONE);
        listTheater.setVisibility(View.VISIBLE);
        this.theaters = theaters;
        if (position == 0) {
            movieDetailActivity.resizeViewPager(theaters.size());
        }
        if(theaters.size() != 0) {
            TheaterListAdapter theaterListAdapter = new TheaterListAdapter(theaters, location);
            theaterListAdapter.setMovieDetailActivity(movieDetailActivity);
            listTheater.setAdapter(theaterListAdapter);
        }else{
            noResult.setVisibility(View.VISIBLE);
            (new Shimmer()).setDuration(3000).start(noResult);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSION_ACCESS_FINE_LOCATION:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getLocation();
                }else{
                    Toast.makeText(getActivity(), "Without location access we can't get latest movie shoetimes :-(", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void getShowtimes(Location location){
        showTimesGateway = RestClient.getShowTimesGateway();
        Call<List<Theater>> theaterCall = showTimesGateway.getTheaters(Double.toString(location.getLatitude()),Double.toString(location.getLongitude()), movie.getTitle().replaceAll("[-+.^:,]", "").replaceAll("\\s","").trim(), position);
        theaterCall.enqueue(new Callback<List<Theater>>() {
            @Override
            public void onResponse(Call<List<Theater>> call, Response<List<Theater>> response) {
                if (response.body() != null) {
                    loadRows(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Theater>> call, Throwable t) {
                Log.e("HTTP ERROR", t.toString());
            }
        });
    }

    public void setPosition(int position){
        this.position = position;
    }

    public int getMovieCount(){
        if(theaters != null){
            return theaters.size();
        }else{
            return 0;
        }
    }

}
