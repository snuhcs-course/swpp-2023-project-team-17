package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Locations {
    @SerializedName("time_stamp")
    String timeStamp;
    @SerializedName("beacon_correct")
    String beaconCorrect;
    @SerializedName("longitude")
    String longitude;
    @SerializedName("latitude")
    String latitude;

    public Locations(String timeStamp, String beaconCorrect, String longitude, String latitude) {
        this.timeStamp = timeStamp;
        this.beaconCorrect = beaconCorrect;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}