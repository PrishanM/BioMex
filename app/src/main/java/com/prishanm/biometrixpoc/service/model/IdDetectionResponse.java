package com.prishanm.biometrixpoc.service.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Prishan Maduka on 29,January,2019
 */
public class IdDetectionResponse {

    @SerializedName("resultcode")
    @Expose
    private String resultcode;

    @SerializedName("result")
    @Expose
    private String result;

    @SerializedName("id_number")
    @Expose
    private String idNumber;

    @SerializedName("session_id")
    @Expose
    private String sessionId;

    @SerializedName("id_type")
    @Expose
    private String idType;

    @SerializedName("other_id_number")
    @Expose
    private String otherIdNumber;

    @SerializedName("name")
    @Expose
    private String name;

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

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getOtherIdNumber() {
        return otherIdNumber;
    }

    public void setOtherIdNumber(String otherIdNumber) {
        this.otherIdNumber = otherIdNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "IdDetection{" +
                "resultcode='" + resultcode + '\'' +
                ", result='" + result + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", idType='" + idType + '\'' +
                ", otherIdNumber='" + otherIdNumber + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
