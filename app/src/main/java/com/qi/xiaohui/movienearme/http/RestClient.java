package com.qi.xiaohui.movienearme.http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by qixiaohui on 4/4/16.
 */
public class RestClient {
    static final String baseUrl = "https://api.themoviedb.org/3/movie/";
    static final String showTime = "http://10.143.28.86:8082/showtimes/";

    public static MoviesGateway getMoviesGateway(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(MoviesGateway.class);
    }

    public static ShowTimesGateway getShowTimesGateway(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(showTime)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ShowTimesGateway.class);
    }
}
