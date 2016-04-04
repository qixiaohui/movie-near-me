package com.qi.xiaohui.movienearme.model.movies;

/**
 * Created by TQi on 4/4/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("alternate")
    @Expose
    private String alternate;
    @SerializedName("cast")
    @Expose
    private String cast;
    @SerializedName("reviews")
    @Expose
    private String reviews;
    @SerializedName("similar")
    @Expose
    private String similar;

    /**
     *
     * @return
     * The self
     */
    public String getSelf() {
        return self;
    }

    /**
     *
     * @param self
     * The self
     */
    public void setSelf(String self) {
        this.self = self;
    }

    /**
     *
     * @return
     * The alternate
     */
    public String getAlternate() {
        return alternate;
    }

    /**
     *
     * @param alternate
     * The alternate
     */
    public void setAlternate(String alternate) {
        this.alternate = alternate;
    }

    /**
     *
     * @return
     * The cast
     */
    public String getCast() {
        return cast;
    }

    /**
     *
     * @param cast
     * The cast
     */
    public void setCast(String cast) {
        this.cast = cast;
    }

    /**
     *
     * @return
     * The reviews
     */
    public String getReviews() {
        return reviews;
    }

    /**
     *
     * @param reviews
     * The reviews
     */
    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    /**
     *
     * @return
     * The similar
     */
    public String getSimilar() {
        return similar;
    }

    /**
     *
     * @param similar
     * The similar
     */
    public void setSimilar(String similar) {
        this.similar = similar;
    }

}
