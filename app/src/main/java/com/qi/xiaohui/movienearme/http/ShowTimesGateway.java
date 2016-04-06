package com.qi.xiaohui.movienearme.http;

import com.qi.xiaohui.movienearme.model.theaters.Theater;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by TQi on 4/6/16.
 */
public interface ShowTimesGateway {
    @GET("{lat}/{longi}/{movie}")
    Call<List<Theater>> getTheaters(@Path("lat") String lat, @Path("longi")String longi, @Path("movie")String movie);
}
