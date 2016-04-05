package com.qi.xiaohui.movienearme.http;

/**
 * Created by qixiaohui on 4/4/16.
 */

import com.qi.xiaohui.movienearme.model.movies.Movies;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by TQi on 2/18/16.
 */
public interface MoviesGateway {

    @GET("now_playing?api_key=bf1eaacae721f1e2efc3b0d0e0b96969")
    Call<Movies> getMovies();

}
