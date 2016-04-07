package com.qi.xiaohui.movienearme.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.qi.xiaohui.movienearme.model.movies.Movies;
import com.qi.xiaohui.movienearme.model.theaters.Movie;

import java.util.Calendar;

/**
 * Created by TQi on 4/7/16.
 */
public class MoviePreference {
    public static void setMovies(Movies movies, Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MOVIES", new Gson().toJson(movies));
        Calendar calendar = Calendar.getInstance();
        int seconds = calendar.get(Calendar.SECOND);
        editor.putInt("DATE", seconds);
        editor.apply();
    }

    public static Movies getMovies(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return new Gson().fromJson(sharedPreferences.getString("MOVIES", ""), Movies.class);
    }

    public static int getDate(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt("DATE", 0);
    }
}
