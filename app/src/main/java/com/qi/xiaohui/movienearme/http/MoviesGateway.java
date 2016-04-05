package com.qi.xiaohui.movienearme.http;

/**
 * Created by qixiaohui on 4/4/16.
 */

import com.qi.xiaohui.movienearme.model.movies.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by TQi on 2/18/16.
 */
public interface MoviesGateway {

    @GET("in_theaters.json?apikey=7waqfqbprs7pajbz28mqf6vz&page_limit=25")
    Call<Movies> getMovies();

}
