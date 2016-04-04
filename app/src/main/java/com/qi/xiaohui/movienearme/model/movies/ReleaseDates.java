package com.qi.xiaohui.movienearme.model.movies;

/**
 * Created by TQi on 4/4/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReleaseDates {

    @SerializedName("theater")
    @Expose
    private String theater;

    /**
     *
     * @return
     * The theater
     */
    public String getTheater() {
        return theater;
    }

    /**
     *
     * @param theater
     * The theater
     */
    public void setTheater(String theater) {
        this.theater = theater;
    }

}
