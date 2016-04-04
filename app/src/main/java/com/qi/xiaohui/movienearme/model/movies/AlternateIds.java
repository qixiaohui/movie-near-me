package com.qi.xiaohui.movienearme.model.movies;

/**
 * Created by TQi on 4/4/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlternateIds {

    @SerializedName("imdb")
    @Expose
    private String imdb;

    /**
     *
     * @return
     * The imdb
     */
    public String getImdb() {
        return imdb;
    }

    /**
     *
     * @param imdb
     * The imdb
     */
    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

}
