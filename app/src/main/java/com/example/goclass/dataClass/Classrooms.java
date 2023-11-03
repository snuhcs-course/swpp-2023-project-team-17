package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Classrooms {
    @SerializedName("buildingNumber")
    String buildingNumber;
    @SerializedName("roomNumber")
    String roomNumber;
    @SerializedName("longitude")
    float longitude;
    @SerializedName("latitude")
    float latitude;
    @SerializedName("beaconId")
    String beaconId;

    public Classrooms(String buildingNumber, String roomNumber, float longitude, float latitude, String beaconId) {
        this.buildingNumber = buildingNumber;
        this.roomNumber = roomNumber;
        this.longitude = longitude;
        this.latitude = latitude;
        this.beaconId = beaconId;
    }
}