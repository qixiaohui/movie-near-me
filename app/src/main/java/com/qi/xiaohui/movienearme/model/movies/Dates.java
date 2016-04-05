package com.qi.xiaohui.movienearme.model.movies;

/**
 * Created by TQi on 4/5/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dates {

    @SerializedName("maximum")
    @Expose
    private String maximum;
    @SerializedName("minimum")
    @Expose
    private String minimum;

    /**
     *
     * @return
     * The maximum
     */
    public String getMaximum() {
        return maximum;
    }

    /**
     *
     * @param maximum
     * The maximum
     */
    public void setMaximum(String maximum) {
        this.maximum = maximum;
    }

    /**
     *
     * @return
     * The minimum
     */
    public String getMinimum() {
        return minimum;
    }

    /**
     *
     * @param minimum
     * The minimum
     */
    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

}
