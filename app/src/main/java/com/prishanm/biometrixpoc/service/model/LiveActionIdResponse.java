package com.prishanm.biometrixpoc.service.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Prishan Maduka on 07,February,2019
 */
public class LiveActionIdResponse {

    @SerializedName("resultcode")
    @Expose
    private String resultcode;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("session_id")
    @Expose
    private String sessionId;
    @SerializedName("action_id")
    @Expose
    private Integer actionId;

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    @Override
    public String toString() {
        return "LiveActionIdResponse{" +
                "resultcode='" + resultcode + '\'' +
                ", result='" + result + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", actionId=" + actionId +
                '}';
    }
}
