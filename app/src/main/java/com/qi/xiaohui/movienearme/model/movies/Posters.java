package com.qi.xiaohui.movienearme.model.movies;

/**
 * Created by TQi on 4/4/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Posters {

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("detailed")
    @Expose
    private String detailed;
    @SerializedName("original")
    @Expose
    private String original;

    /**
     *
     * @return
     * The thumbnail
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     *
     * @param thumbnail
     * The thumbnail
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     *
     * @return
     * The profile
     */
    public String getProfile() {
        return profile;
    }

    /**
     *
     * @param profile
     * The profile
     */
    public void setProfile(String profile) {
        this.profile = profile;
    }

    /**
     *
     * @return
     * The detailed
     */
    public String getDetailed() {
        return detailed;
    }

    /**
     *
     * @param detailed
     * The detailed
     */
    public void setDetailed(String detailed) {
        this.detailed = detailed;
    }

    /**
     *
     * @return
     * The original
     */
    public String getOriginal() {
        return original;
    }

    /**
     *
     * @param original
     * The original
     */
    public void setOriginal(String original) {
        this.original = original;
    }

}
