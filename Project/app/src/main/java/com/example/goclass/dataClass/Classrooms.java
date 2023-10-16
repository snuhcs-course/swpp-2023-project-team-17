package com.example.goclass.dataClass;

import com.google.gson.annotations.SerializedName;

public class Classrooms {
    @SerializedName("building_number")
    String buildingNumber;
    @SerializedName("room_number")
    String roomNumber;
    @SerializedName("longitude")
    String longitude;
    @SerializedName("latitude")
    String latitude;
    @SerializedName("beacon_id")
    String beaconId;

    public Classrooms(String buildingNumber, String roomNumber, String longitude, String latitude, String beaconId) {
        this.buildingNumber = buildingNumber;
        this.roomNumber = roomNumber;
        this.longitude = longitude;
        this.latitude = latitude;
        this.beaconId = beaconId;
    }
}