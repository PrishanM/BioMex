package com.prishanm.biometrixpoc.service.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Prishan Maduka on 08,February,2019
 */
public class LivenessDetectionRequest {

    @SerializedName("session_id")
    @Expose
    private String sessionId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("action_id")
    @Expose
    private Integer actionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    @Override
    public String toString() {
        return "LivenessDetectionRequest{" +
                "sessionId='" + sessionId + '\'' +
                ", image='" + image + '\'' +
                ", actionId=" + actionId +
                '}';
    }
}
