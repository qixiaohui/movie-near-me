package com.qi.xiaohui.movienearme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.qi.xiaohui.movienearme.R;
import com.qi.xiaohui.movienearme.adapter.MovieListAdapter;
import com.qi.xiaohui.movienearme.http.MoviesGateway;
import com.qi.xiaohui.movienearme.http.RestClient;
import com.qi.xiaohui.movienearme.model.movies.Movies;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecycleView;
    private StaggeredGridLayoutManager mLayoutManager;
    private MovieListAdapter movieAdapter;
    private MoviesGateway moviesGateway;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecycleView = (RecyclerView) findViewById(R.id.list);
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(mLayoutManager);

        moviesGateway = RestClient.getGateway();
        Call<Movies> moviesCall = moviesGateway.getMovies();
        moviesCall.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                loadRows(response.body());
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Log.e("HTTP ERROR", t.toString());
            }
        });
    }

    private void loadRows(Movies movies){
        movieAdapter = new MovieListAdapter(getApplicationContext(), movies);
        mRecycleView.setAdapter(movieAdapter);
    }
}
