package com.prishanm.biometrixpoc.service.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Prishan Maduka on 29,January,2019
 */
public class IdDetectionRequest {

    @SerializedName("image")
    @Expose
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "IdDetectionRequest{" +
                "image='" + image + '\'' +
                '}';
    }

}
