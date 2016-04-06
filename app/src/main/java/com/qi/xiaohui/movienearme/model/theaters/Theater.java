package com.qi.xiaohui.movienearme.model.theaters;

/**
 * Created by TQi on 4/6/16.
 */
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Theater {

    @SerializedName("id")
    @Expose
    private Boolean id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("movies")
    @Expose
    private Object movies;
    @SerializedName("movie")
    @Expose
    private List<Movie> movie = new ArrayList<Movie>();

    /**
     *
     * @return
     * The id
     */
    public Boolean getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Boolean id) {
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
     * The address
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     * The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return
     * The phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @param phoneNumber
     * The phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     *
     * @return
     * The movies
     */
    public Object getMovies() {
        return movies;
    }

    /**
     *
     * @param movies
     * The movies
     */
    public void setMovies(Object movies) {
        this.movies = movies;
    }

    /**
     *
     * @return
     * The movie
     */
    public List<Movie> getMovie() {
        return movie;
    }

    /**
     *
     * @param movie
     * The movie
     */
    public void setMovie(List<Movie> movie) {
        this.movie = movie;
    }

}
