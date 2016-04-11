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
    private StaggeredGridLayoutManager mLayoutManager;
    private MovieListAdapter movieAdapter;
    private MoviesGateway moviesGateway;
    private int pageNumber = 2;

    private int visibleItemCount;
    private int totalItemCount;
    private int pastVisiblesItems;

    private Movies movies;
    private Boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movies = new Gson().fromJson(getIntent().getStringExtra("MOVIES"), Movies.class);
        setContentView(R.layout.activity_main);
        mRecycleView = (RecyclerView) findViewById(R.id.list);
        mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(mLayoutManager);
        loadRows(movies);
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    int [] into = new int[3];
                    mLayoutManager.findLastVisibleItemPositions(into);

                    if (loading) {
                        if (visibleItemCount >= 7 && movies.getResults().size()<30) {
                            loading = false;
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
                Movies moreMovies = movieAdapter.getMovies();
                moreMovies.getResults().addAll(response.body().getResults());
                movieAdapter.setMovies(moreMovies);
                movieAdapter.notifyItemRangeChanged(movieAdapter.getItemCount()+1, moreMovies.getResults().size());
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
