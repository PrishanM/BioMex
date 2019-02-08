package com.prishanm.biometrixpoc.service.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Prishan Maduka on 08,February,2019
 */
public class LivenessDetectionResponse {

    @SerializedName("resultcode")
    @Expose
    private String resultcode;
    @SerializedName("result")
    @Expose
    private String result;

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

    @Override
    public String toString() {
        return "LivenessDetectionResponse{" +
                "resultcode='" + resultcode + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
