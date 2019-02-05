package com.prishanm.biometrixpoc.service.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Prishan Maduka on 05,February,2019
 */
public class FaceDetectionRequest {

    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("session_id")
    @Expose
    private String sessionId;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "FaceDetectionRequest{" +
                "image='" + image + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
