package com.prishanm.biometrixpoc.service.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Prishan Maduka on 30,January,2019
 */
public class CustomerDetailsModel implements Parcelable {

    private String identityType;
    private String idNumber;
    private String name;
    private String otherIdNumber;
    private String sessionId;

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOtherIdNumber() {
        return otherIdNumber;
    }

    public void setOtherIdNumber(String otherIdNumber) {
        this.otherIdNumber = otherIdNumber;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.identityType);
        dest.writeString(this.idNumber);
        dest.writeString(this.name);
        dest.writeString(this.otherIdNumber);
        dest.writeString(this.sessionId);
    }

    public CustomerDetailsModel() {
    }

    protected CustomerDetailsModel(Parcel in) {
        this.identityType = in.readString();
        this.idNumber = in.readString();
        this.name = in.readString();
        this.otherIdNumber = in.readString();
        this.sessionId = in.readString();
    }

    public static final Parcelable.Creator<CustomerDetailsModel> CREATOR = new Parcelable.Creator<CustomerDetailsModel>() {
        @Override
        public CustomerDetailsModel createFromParcel(Parcel source) {
            return new CustomerDetailsModel(source);
        }

        @Override
        public CustomerDetailsModel[] newArray(int size) {
            return new CustomerDetailsModel[size];
        }
    };
}
