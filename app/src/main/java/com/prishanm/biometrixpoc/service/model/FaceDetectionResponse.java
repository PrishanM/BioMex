package com.prishanm.biometrixpoc.service.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Prishan Maduka on 05,February,2019
 */
public class FaceDetectionResponse {

    @SerializedName("resultcode")
    @Expose
    private String resultcode;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("session_id")
    @Expose
    private String sessionId;
    @SerializedName("matching_percentages")
    @Expose
    private List<String> matchingPercentages = null;

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

    public List<String> getMatchingPercentages() {
        return matchingPercentages;
    }

    public void setMatchingPercentages(List<String> matchingPercentages) {
        this.matchingPercentages = matchingPercentages;
    }

    @Override
    public String toString() {
        return "FaceDetectionResponse{" +
                "resultcode='" + resultcode + '\'' +
                ", result='" + result + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", matchingPercentages=" + matchingPercentages +
                '}';
    }
}
