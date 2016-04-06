package com.qi.xiaohui.movienearme.model.theaters;

/**
 * Created by TQi on 4/6/16.
 */
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("runtime")
    @Expose
    private String runtime;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("genre")
    @Expose
    private List<String> genre = new ArrayList<String>();
    @SerializedName("imdb")
    @Expose
    private String imdb;
    @SerializedName("trailer")
    @Expose
    private String trailer;
    @SerializedName("showtimes")
    @Expose
    private List<String> showtimes = new ArrayList<String>();

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The runtime
     */
    public String getRuntime() {
        return runtime;
    }

    /**
     *
     * @param runtime
     * The runtime
     */
    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    /**
     *
     * @return
     * The rating
     */
    public String getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     * The rating
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     *
     * @return
     * The genre
     */
    public List<String> getGenre() {
        return genre;
    }

    /**
     *
     * @param genre
     * The genre
     */
    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

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

    /**
     *
     * @return
     * The trailer
     */
    public String getTrailer() {
        return trailer;
    }

    /**
     *
     * @param trailer
     * The trailer
     */
    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    /**
     *
     * @return
     * The showtimes
     */
    public List<String> getShowtimes() {
        return showtimes;
    }

    /**
     *
     * @param showtimes
     * The showtimes
     */
    public void setShowtimes(List<String> showtimes) {
        this.showtimes = showtimes;
    }

}
