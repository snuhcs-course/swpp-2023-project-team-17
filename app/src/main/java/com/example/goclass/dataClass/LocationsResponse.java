package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class LocationsResponse {
    @SerializedName("time_stamp")
    String timeStamp;
    @SerializedName("beacon_correct")
    String beaconCorrect;
    @SerializedName("longitude")
    String longitude;
    @SerializedName("latitude")
    String latitude;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getBeaconCorrect() {
        return beaconCorrect;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}