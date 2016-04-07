package com.qi.xiaohui.movienearme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.qi.xiaohui.movienearme.R;
import com.qi.xiaohui.movienearme.SharedPreference.MoviePreference;
import com.qi.xiaohui.movienearme.adapter.MovieListAdapter;
import com.qi.xiaohui.movienearme.http.MoviesGateway;
import com.qi.xiaohui.movienearme.http.RestClient;
import com.qi.xiaohui.movienearme.model.movies.Movies;
import com.qi.xiaohui.movienearme.model.theaters.Movie;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView mRecycleView;
    private LinearLayoutManager mLayoutManager;
    private MovieListAdapter movieAdapter;
    private MoviesGateway moviesGateway;
    private int pageNumber = 1;

    private int visibleItemCount;
    private int totalItemCount;
    private int pastVisiblesItems;

    private Movies movies;
    private Boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecycleView = (RecyclerView) findViewById(R.id.list);
        mLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);

        Calendar calendar = Calendar.getInstance();
        int current = calendar.get(Calendar.SECOND);
        int past = MoviePreference.getDate(MainActivity.this);
        if(past != 0 && current - past > 0 && current - past <  86400){
            Movies movies = MoviePreference.getMovies(MainActivity.this);
            loadRows(movies);
        }else {
            loadMovies();
        }

        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findLastVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            pageNumber++;
                            loadMovies();
                        }
                    }
                }
            }
        });
    }

    private void loadMovies(){
        moviesGateway = RestClient.getMoviesGateway();
        Call<Movies> moviesCall = moviesGateway.getMovies(pageNumber);
        moviesCall.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                if(pageNumber ==1) {
                    loadRows(response.body());
                }else if(pageNumber > 1){
                    Movies moreMovies = movieAdapter.getMovies();
                    moreMovies.getResults().addAll(response.body().getResults());
                    movieAdapter.setMovies(moreMovies);
                    movieAdapter.notifyItemRangeChanged(movieAdapter.getItemCount()+1, moreMovies.getResults().size());
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Log.e("HTTP ERROR", t.toString());
            }
        });
    }



    private void loadRows( final Movies movies){
        this.movies = movies;
        movieAdapter = new MovieListAdapter(getApplicationContext(), movies);
        mRecycleView.setAdapter(movieAdapter);
        MovieListAdapter.OnItemClickListener itemClickListener = new MovieListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImageView poster = (ImageView) view.findViewById(R.id.moviePoster);
                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                intent.putExtra(MovieDetailActivity.EXTRA_PARAM, (new Gson()).toJson(movies.getResults().get(position)));
                startActivity(intent);
            }
        };
        movieAdapter.setOnItemClickListener(itemClickListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(this.movies != null){
            MoviePreference.setMovies(this.movies, MainActivity.this);
        }
    }
}
