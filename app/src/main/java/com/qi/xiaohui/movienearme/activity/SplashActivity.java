package com.qi.xiaohui.movienearme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.qi.xiaohui.movienearme.R;
import com.qi.xiaohui.movienearme.SharedPreference.MoviePreference;
import com.qi.xiaohui.movienearme.http.MoviesGateway;
import com.qi.xiaohui.movienearme.http.RestClient;
import com.qi.xiaohui.movienearme.model.movies.Movies;
import com.qi.xiaohui.movienearme.model.theaters.Movie;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TQi on 4/8/16.
 */
public class SplashActivity extends AppCompatActivity {
    private ImageView splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splash = (ImageView) findViewById(R.id.splashIcon);
        splash.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));
        Calendar calendar = Calendar.getInstance();
        int current = calendar.get(Calendar.SECOND);
        int past = MoviePreference.getDate(SplashActivity.this);
        if(past != 0 && current - past > 0 && current - past <  86400){
            Movies movies = MoviePreference.getMovies(SplashActivity.this);
                    loadMovies(movies);
        }else {
            MoviesGateway moviesGateway = RestClient.getMoviesGateway();
            Call<Movies> moviesCall = moviesGateway.getMovies(1);
            moviesCall.enqueue(new Callback<Movies>() {
                @Override
                public void onResponse(Call<Movies> call, Response<Movies> response) {
                    MoviePreference.setMovies(response.body(), SplashActivity.this);
                    loadMovies(response.body());
                }

                @Override
                public void onFailure(Call<Movies> call, Throwable t) {
                    Log.e("HTTP ERROR", t.toString());
                }
            });
        }
    }

    private void loadMovies(final Movies movie){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("MOVIES",new Gson().toJson(movie));
                startActivity(intent);
                finish();
            }
        }, 20000);
    }

}
