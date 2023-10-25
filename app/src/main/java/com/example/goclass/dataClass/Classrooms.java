package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Classrooms {
    @SerializedName("building_number")
    String buildingNumber;
    @SerializedName("room_number")
    String roomNumber;
    @SerializedName("longitude")
    float longitude;
    @SerializedName("latitude")
    float latitude;
    @SerializedName("beacon_id")
    String beaconId;

    public Classrooms(String buildingNumber, String roomNumber, float longitude, float latitude, String beaconId) {
        this.buildingNumber = buildingNumber;
        this.roomNumber = roomNumber;
        this.longitude = longitude;
        this.latitude = latitude;
        this.beaconId = beaconId;
    }
}