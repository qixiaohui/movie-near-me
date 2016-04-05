package com.qi.xiaohui.movienearme.model.movies;

/**
 * Created by qixiaohui on 4/4/16.
 */

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movies {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("movies")
    @Expose
    private List<Movie> movies = new ArrayList<Movie>();
    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("link_template")
    @Expose
    private String linkTemplate;

    /**
     *
     * @return
     * The total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     *
     * @param total
     * The total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     *
     * @return
     * The movies
     */
    public List<Movie> getMovies() {
        return movies;
    }

    /**
     *
     * @param movies
     * The movies
     */
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    /**
     *
     * @return
     * The links
     */
    public Links getLinks() {
        return links;
    }

    /**
     *
     * @param links
     * The links
     */
    public void setLinks(Links links) {
        this.links = links;
    }

    /**
     *
     * @return
     * The linkTemplate
     */
    public String getLinkTemplate() {
        return linkTemplate;
    }

    /**
     *
     * @param linkTemplate
     * The link_template
     */
    public void setLinkTemplate(String linkTemplate) {
        this.linkTemplate = linkTemplate;
    }

}
